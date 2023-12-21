package org.zerock.d01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLOps;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QuerydslRepositoryInvokerAdapter;
import org.zerock.d01.domain.Board;
import org.zerock.d01.domain.QBoard;
import org.zerock.d01.domain.QReply;
import org.zerock.d01.dto.BoardListReplyCountDTO;

import java.util.List;
@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search(Pageable pageable) {
        QBoard board = QBoard.board; //select * from board
        JPQLQuery<Board> query = from(board);
        query.where(board.title.contains("1"));//where title like "'%'1'%'"

        this.getQuerydsl().applyPagination(pageable,query);

        List<Board> list = query.fetch();
        Long count = query.fetchCount();
        list.forEach(board1 -> log.info(board1));
        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);//select * from board

        BooleanBuilder booleanBuilder = null;
        if ((types != null && types.length > 0) && keyword != null) {
            booleanBuilder = new BooleanBuilder();

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;

                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;

                }
            }
            query.where(booleanBuilder);
        }
        query.where(board.bno.gt(0L)); //where bno > 0
        log.info("query: "+ query);

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        Long count = query.fetchCount();

        log.info("count: " + count);
        list.forEach(board1 -> log.info(board1));
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);
        query.fetch();
        BooleanBuilder booleanBuilder = null;
        if ((types != null && types.length > 0) && keyword != null) {
            booleanBuilder = new BooleanBuilder();

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;

                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;

                }
            }
            query.where(booleanBuilder);
        }
        query.where(board.bno.gt(0L)); //where bno > 0 속도를 빠르게 검색하게 하기위한 조건문

        JPQLQuery<BoardListReplyCountDTO> dtojpqlQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
                                                                        board.bno,
                                                                        board.title,
                                                                        board.writer,
                                                                        board.regDate,
                                                                        reply.count().as("replyCount"))
        );

        this.getQuerydsl().applyPagination(pageable, dtojpqlQuery);
        List<BoardListReplyCountDTO> dtoList = dtojpqlQuery.fetch();
        Long count = dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

}
