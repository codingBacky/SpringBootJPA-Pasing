package org.zerock.d01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.d01.domain.Board;
import org.zerock.d01.domain.Reply;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;
    @Test
    public void testInsert(){
        Board board = Board.builder().bno(101L).build();
        Reply reply = Reply.builder()
                .replyer("둥ㅇㅣ")
                .replyText("퍼가요 ~ ❤")
                .board(board)
                .build();
        replyRepository.save(reply);
    }
    @Test
    public void testSelectOne(){
        Reply reply = replyRepository.findById(1L).orElseThrow();

        log.info(reply);
    }
    @Test
    public void testBoardReply(){
        Long bno = 101L;
        Pageable pageable = PageRequest.of(0,10, Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        result.getContent().forEach(reply -> log.info(reply));
    }
}

