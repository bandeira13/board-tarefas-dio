package br.com.dio.service;

import br.com.dio.model.BoardColumn;
import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardColumnDAOImpl;
import java.util.List;

public class BoardColumnQueryService {
    private final BoardColumnDAO boardColumnDAO;

    public BoardColumnQueryService() {
        this.boardColumnDAO = new BoardColumnDAOImpl();
    }

    public List<BoardColumn> findAllByBoardId(int boardId) {
        return boardColumnDAO.findAllByBoardId(boardId);
    }
}
