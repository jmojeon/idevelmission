package com.idevel.service;

import com.idevel.dto.BoardDTO;
import com.idevel.entity.BoardEntity;
import com.idevel.entity.BoardFileEntity;
import com.idevel.repository.BoardFileRepository;
import com.idevel.repository.BoardRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;


    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if (boardDTO.getBoardFile().isEmpty()) {        // 첨부파일이 없는 경우
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        } else{                                         // 첨부 파일이 있는 경우
//            // 1. DTO에 담긴 파일을 꺼냄
//            MultipartFile boardFile = boardDTO.getBoardFile();
//            // 2. 파일의 이름을 가져오기
//            String originalFilename = boardFile.getOriginalFilename();
//            // 3. 서버 저장용 이름을 만든다
//            String storedFilename =  System.currentTimeMillis() + "_" + originalFilename;
//            // 4. 저장 경로 설정
//            String savePath = "C:/springboot_img/" + storedFilename;
//            // 5. 해당 경로에 파일 저장
//            boardFile.transferTo(new File(savePath));
//            // 6. board_table에 해당 데이터 save처리
//            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
//            // 7. board_file_table에 해당 데이터 save 처리
//            Long saveId = boardRepository.save(boardEntity).getId();
//            BoardEntity board = boardRepository.findById(saveId).get();         // 위에 boardEntity에는 id값이 없어...  단일파일일경우

            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long saveId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(saveId).get();
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {

                String originalFilename = boardFile.getOriginalFilename();
                String storedFilename = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/springboot_img/" + storedFilename;
                boardFile.transferTo(new File(savePath));

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFilename);
                boardFileRepository.save(boardFileEntity);
            }


        }
    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }else {
            return null;
        }
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntityEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber()-1;
        int pageLimit=5;                            // 한페이지에 보여줄 글 갯수
        // 한페이지당 글 5개씩 보여주고 id를 기준으로 내림차순
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

//        System.out.println("boardEntities.getContent() = " + boardEntities.getContent());               // 요청 페이지에 해당하는 글
//        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements());   // 전체 글갯수
//        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber());                 // DB로 요청한 페이지 번호
//        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages());         // 전체 페이지 갯수
//        System.out.println("boardEntities.getSize() = " + boardEntities.getSize());                     // 한 페이지에 보여지는 글 갯수
//        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious());             // 이전 페이지 존재 여부
//        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst());                     // 첫 페이지 여부
//        System.out.println("boardEntities.isLast() = " + boardEntities.isLast());                       // 마지막 페이지 여부

        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime(), board.getBoardEmail(), board.getBoardTel()));
        return boardDTOS;
    }
}
