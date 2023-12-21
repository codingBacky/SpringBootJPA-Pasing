package org.zerock.d01.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.d01.dto.BoardDTO;
import org.zerock.d01.dto.BoardListReplyCountDTO;
import org.zerock.d01.dto.PageRequestDTO;
import org.zerock.d01.dto.PageResponseDTO;
import org.zerock.d01.service.BoardService;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/board/*")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "list")
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
      /*  PageResponseDTO<BoardDTO> list = boardService.list(pageRequestDTO);
        log.info(list);*/
        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);
        model.addAttribute("responseDTO",responseDTO);

    }
    @Operation(summary = "register", method = "GetMapping")
    @GetMapping("/register")
    public void registerGET(){

    }
    @PostMapping("/register")
    public String registerPOST(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes rttr){
        log.info("boardPost register");
        if(bindingResult.hasErrors()){
            log.info("boardPost register hasError");

            rttr.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register";
        }
        log.info("boardPost register success" + boardDTO);
        Long bno = boardService.register(boardDTO);
        rttr.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }
    @GetMapping({"/read", "/modify"})
    public void read(@RequestParam("bno") Long bno, PageRequestDTO pageRequestDTO, Model model){
        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        log.info("board modify post..........." + boardDTO);

        if(bindingResult.hasErrors()){
            log.info("has error........................................................ㅗ");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?"+link;
        }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam("bno") Long bno, RedirectAttributes rttr) {
        boardService.remove(bno);
        rttr.addFlashAttribute("result", "remove");
        return "redirect:/board/list";
    }
}


