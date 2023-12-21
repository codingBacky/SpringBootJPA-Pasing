package org.zerock.d01.service;

import org.zerock.d01.domain.Board;
import org.zerock.d01.dto.BoardDTO;
import org.zerock.d01.dto.BoardListReplyCountDTO;
import org.zerock.d01.dto.PageRequestDTO;
import org.zerock.d01.dto.PageResponseDTO;

public interface BoardService {
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO dto);
    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    default Board boardDTOToEntity(BoardDTO dto){
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        return board;
    }
    default  BoardDTO entityToBoardDTO(Board board){

        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
    }
}
