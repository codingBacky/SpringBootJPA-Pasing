package org.zerock.d01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.d01.domain.Reply;
import org.zerock.d01.dto.PageRequestDTO;
import org.zerock.d01.dto.PageResponseDTO;
import org.zerock.d01.dto.ReplyDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ReplyServiceTests {
    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister(){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .bno(107L)
                .replyText("댓글 달아줄게")
                .replyer("1004")
                .build();
        Long rno = replyService.register(replyDTO);
        log.info("rno : " + rno);
    }
    @Test
    public void testRead(){
        ReplyDTO replyDTO = replyService.read(7L);
        log.info(replyDTO);
    }
    @Test
    public void testModify(){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(7L)
                .replyText("댓글 수정했어요")
                .build();
        replyService.modify(replyDTO);
    }
    @Test
    public void testRemove(){
        replyService.remove(7L);
    }
    @Test
    public void testGetListOfBoard(){
        PageRequestDTO dto = PageRequestDTO.builder()
                                            .page(1)
                                            .size(10)
                                            .build();
        replyService.getListOfBoard(101L, dto);
    }
}