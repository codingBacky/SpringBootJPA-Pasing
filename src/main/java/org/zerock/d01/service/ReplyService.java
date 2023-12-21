package org.zerock.d01.service;

import org.zerock.d01.domain.Reply;
import org.zerock.d01.dto.PageRequestDTO;
import org.zerock.d01.dto.PageResponseDTO;
import org.zerock.d01.dto.ReplyDTO;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);
    //특정 게시글(bno) 전체 댓글 리스트 출력
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
