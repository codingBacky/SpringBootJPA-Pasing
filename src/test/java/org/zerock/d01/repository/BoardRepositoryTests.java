package org.zerock.d01.repository;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.OptionalIntAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.d01.domain.Board;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert(){
        for(int i = 0; i <= 100; i++) {
            Board board = Board.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .writer("user" + (i%10))
                    .build();
            Board result = boardRepository.save(board);
            log.info("BNO : " + result.getBno());
        }
    }
    @Test
    public void testSelect(){
        Long bno = 100L;

       // Optional<Board> result = boardRepository.findById(bno);
       // Board board = result.orElseThrow();
        Board board = boardRepository.findById(bno).orElseThrow();
        log.info(board);
    }
    @Test
    public void testUpdate(){
        Long bno =100L;
        Board board = boardRepository.findById(bno).orElseThrow();
        board.change("update","update content");
        boardRepository.save(board);
    }
    @Test
    public void testDelete(){
        boardRepository.deleteById(100L);
    }
    @Test
    public void testGetList(){
        //boardRepository.findAll().forEach(List -> log.info(List));

        List<Board> list =boardRepository.findAll();
        for(Board b : list)
            log.info(b);
    }
    @Test
    public void testPaggin(){
        Pageable pageable = PageRequest.of(1,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);
        log.info(result.getTotalElements());

        result.getContent().forEach(list -> log.info(list));
    }
    @Test
    public void testWriter(){
        boardRepository.findByWriter("user0")
                .forEach(list -> log.info(list));
    }
    @Test
    public void testWriterAndTitle(){
        boardRepository.findByWriterAndTitle("user1","title1")
                .forEach(list -> log.info(list));
    }
    @Test
    public void testTitleLike(){
        boardRepository.findByTitleLike("%1%")
                .forEach(list -> log.info(list));
    }
    @Test
    public void testWriter2(){
        boardRepository.findByWriter2("user1")
                .forEach(list -> log.info(list));
    }
    @Test
    public void testTitle(){
        boardRepository.findByTitle("1")
                .forEach(list -> log.info(list));
    }
    @Test
    public void testKeyword(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findKeyword("1", pageable);

        log.info(result.getTotalPages());
        log.info(result.getTotalElements());
        result.getContent().forEach(board -> log.info(board));
    }
    @Test
    public void testTitle2(){
        boardRepository.findByTitle2("2")
                .forEach(list -> log.info(list));
    }
    @Test
    public void testSearch(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
        boardRepository.search(pageable);
    }
    @Test
    public void testSearchAll(){
        String[] types = {"t","c","w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types,keyword,pageable);
        log.info("-------=-=-=-=-=-=--------");
        log.info(result.getTotalElements());
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasNext());
        log.info(result.hasPrevious());
    }
}