package br.com.dio.service;

import br.com.dio.model.Board;
import br.com.dio.model.BoardColumn;
import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardColumnDAOImpl;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.dao.BoardDAOImpl;

import java.util.List;
import java.util.Optional;


public class BoardQueryService {
    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO;

    public BoardQueryService() {
        this.boardDAO = new BoardDAOImpl();
        this.boardColumnDAO = new BoardColumnDAOImpl();
    }


    public Optional<Board> findBoardById(int id) {
        return boardDAO.findById(id);
    }

    public List<Board> findAllBoards() {
        return boardDAO.findAll();
    }

    public List<BoardColumn> findAllColumnsByBoardId(int boardId) {
        return boardColumnDAO.findAllByBoardId(boardId);
    }
}
