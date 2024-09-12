package com.idevel.dto;

import com.idevel.entity.BoardEntity;
import com.idevel.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor  //기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {

    private Long id;
    private String boardWriter;
    private String boardTitle;
    private String boardContents;

    private String boardTel;
    private String boardEmail;

    private int boardHits;
    private LocalDate boardCreatedTime;
    private LocalDate boardUpdatedTime;

    private List<MultipartFile> boardFile;            // save.html에서 넘어올때 파일을 담는 용도
    private List<String> originalFileName;            // 원본 파일 이름
    private List<String> storedFileName;              // 서버 저장용 파일 이름
    private int fileAttached;                   // 파일 첨부 여부 (첨부1, 미첨부 0)

    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDate boardCreatedTime, String boardEmail, String boardTel) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;

        this.boardEmail = boardEmail;
        this.boardTel = boardTel;

        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardEmail(boardEntity.getBoardEmail());
        boardDTO.setBoardTel(boardEntity.getBoardTel());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        if(boardEntity.getFileAttached() == 0){
            boardDTO.setFileAttached(boardEntity.getFileAttached());
        }else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            boardDTO.setFileAttached(boardEntity.getFileAttached());
            // originalFileName, storedFileName : board_file_table(BoardFileEntity)
            // join 문법을 이용해 다른테이블의 컬럼값을 가져온다...

//            boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());            //entity에 있는 리스트에 접근...? 해서 첫번재 인덱스 -> 첨부파일이 하나
//            boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());                //  단일 파일

            for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }

        return boardDTO;
    }
}
