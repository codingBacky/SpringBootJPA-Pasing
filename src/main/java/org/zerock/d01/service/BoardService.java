package org.zerock.d01.service;

import org.springframework.data.domain.PageRequest;
import org.zerock.d01.domain.Board;
import org.zerock.d01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO dto);
    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
    //게시글의 이미지와 댓글 숫자까지 자리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    default Board dtoToEntity(BoardDTO dto){
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        //uuid_originalFileName
        if(dto.getFileNames() != null){
            dto.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImages(arr[0], arr[1]);//0번쩨 uuid 1번째 original filename
            });
        }
        return board;
    }
    default Board boardDTOToEntity(BoardDTO dto){
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        return board;
    }
    default  BoardDTO entityToDTO(Board board){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().map(
                boardImage -> boardImage.getUuid()+"_"+boardImage.getFileName())
                                        .collect(Collectors.toList());
        boardDTO.setFileNames(fileNames);

        return boardDTO;

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
