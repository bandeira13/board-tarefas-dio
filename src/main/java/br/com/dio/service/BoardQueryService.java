package br.com.dio.service;

import br.com.dio.model.Board;
import br.com.dio.model.BoardColumn;
import br.com.dio.model.Card;
import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardColumnDAOImpl;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.dao.BoardDAOImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardQueryService {
    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO;
    private final CardQueryService cardQueryService;

    public BoardQueryService() {
        this.boardDAO = new BoardDAOImpl();
        this.boardColumnDAO = new BoardColumnDAOImpl();
        this.cardQueryService = new CardQueryService();
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

    public Optional<Board> findBoardByIdWithColumnsAndCards(int boardId) {
        Optional<Board> boardOptional = boardDAO.findById(boardId);

        if (boardOptional.isEmpty()) {
            return Optional.empty();
        }

        Board board = boardOptional.get();

        List<BoardColumn> columns = boardColumnDAO.findAllByBoardId(boardId);

        for (BoardColumn column : columns) {
            List<Card> cards = cardQueryService.findAllCardsByColumnId(column.getId());
        }

        return Optional.of(board);
    }
}
