package org.zerock.d01.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.d01.domain.Board;
import org.zerock.d01.dto.BoardDTO;
import org.zerock.d01.dto.PageRequestDTO;
import org.zerock.d01.dto.PageResponseDTO;
import org.zerock.d01.repository.BoardRepository;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ModelMapper modelMapper;
//    @Override
//    public Long register(BoardDTO boardDTO) {
//        Board board = boardDTOToEntity(boardDTO);
//        Long bno = boardRepository.save(board).getBno();
//        return bno;
//    }

    @Override
    public Long register(BoardDTO boardDTO) {
        Board board = modelMapper.map(boardDTO, Board.class);
        log.info(board);

        Long bno = boardRepository.save(board).getBno();
        return bno;
    }
//    @Override
//    public BoardDTO readOne(Long bno) {
//        Board board = boardRepository.findById(bno).orElseThrow();
//        return entityToBoardDTO(board);
//    }
    @Override
    public BoardDTO readOne(Long bno) {
       // Optional<Board> board = boardRepository.findById(bno);
        Board board = boardRepository.findById(bno).orElseThrow();

        return modelMapper.map(board, BoardDTO.class);
    }

    @Override
    public void modify(BoardDTO dto) {
        Board board = boardRepository.findById(dto.getBno()).orElseThrow();
        board.change(dto.getTitle(), dto.getContent());
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();//tcw를 나눠서 배열로
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result= boardRepository.searchAll(types, keyword, pageable);
        List<BoardDTO> dtoList = result.getContent().stream().map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }


}
