package br.com.dio.service;

import br.com.dio.model.BoardColumn;
import br.com.dio.persistence.dao.BoardColumnDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardColumnQueryService {
    private final BoardColumnDAO boardColumnDAO;

    public BoardColumnQueryService(BoardColumnDAO boardColumnDAO) {

        this.boardColumnDAO =  boardColumnDAO;
    }

    public List<BoardColumn> findAllByBoardId(int boardId) {

        return boardColumnDAO.findAllByBoardId(boardId);
    }
}
