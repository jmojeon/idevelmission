package com.idevel.controller;

import com.idevel.dto.BoardDTO;
import com.idevel.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;


    @GetMapping("/save")
    public String saveForm( @PageableDefault(page=1) Pageable pageable, Model model) {

        model.addAttribute("page", pageable.getPageNumber());

        return "alliance/alliance_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO,
                       @PageableDefault(page=1) Pageable pageable, Model model) throws IOException {
        System.out.println("게시글 작성시 boardDTO = " + boardDTO);
        boardService.save(boardDTO);

        Page<BoardDTO> boardList = boardService.paging(pageable);

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("page", pageable.getPageNumber());

        System.out.println("여기는 안나놀거 아니야?게시글 작성시 boardDTO = " + boardDTO);

        return "alliance/listPaging";
    }

//    @GetMapping("/")
//    public String findAll(Model model) {
//        List<BoardDTO> boardDTOList = boardService.findAll();
//        model.addAttribute("boardList", boardDTOList);
//        return "list";
//    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        // 해당 게시글의 조회수를 하나 올기고 게시글 데이터를 가져와서 detail.html에 출력

        boardService.updateHits(id);
        BoardDTO boardDTO =boardService.findById(id);
        model.addAttribute("board", boardDTO);

        model.addAttribute("page", pageable.getPageNumber());

        System.out.println("id = " + id + ", model = " + model + ", pageable = " + pageable);

        return "alliance/alliance_detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model, @PageableDefault(page=1) Pageable pageable){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);

        model.addAttribute("page", pageable.getPageNumber());

        System.out.println("id = " + id + ", model = " + model);
        return "alliance/alliance_update";
    }

    @PostMapping("/update")
    public String updateById(@ModelAttribute BoardDTO boardDTO,
                             @PageableDefault(page=1) Pageable pageable, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);

        model.addAttribute("page", pageable.getPageNumber());

        return "redirect:/board/" +boardDTO.getId();
//        return "redirect:/board/" +boardDTO.getId(); 조회수가 올라간다...
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/paging";
    }

    @GetMapping("/paging")
    public String paging(@PageableDefault(page=1) Pageable pageable, Model model) {
//        pageable.getPageNumber();

        Page<BoardDTO> boardList = boardService.paging(pageable);

        //페이지 제한 갯수
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        // 페이지 갯수가 총 20개 ->
        // 현재 사용자가 3페이지를 본다고 가정
        // 1 2 3
        // 하단에 보여지는 페이지는 3개
        // 다음 페이지는 4 5 6

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("page", pageable.getPageNumber());

        System.out.println("pageable = " + pageable + ", model 여기가 나오는건 맞아  = " + model);

        return "alliance/listPaging";
    }


}

