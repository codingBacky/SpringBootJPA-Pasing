package org.zerock.d01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.d01.domain.Board;
import org.zerock.d01.dto.BoardListReplyCountDTO;

public interface BoardSearch {
    Page<Board> search(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);
}
