package br.com.dio.service;

import br.com.dio.model.Board;
import br.com.dio.model.BoardColumn;
import br.com.dio.persistence.config.ConnectionConfig;
import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardColumnDAOImpl;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.dao.BoardDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
public class BoardService {
    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO;


    public BoardService() {
        this.boardDAO = new BoardDAOImpl();
        this.boardColumnDAO = new BoardColumnDAOImpl();
    }
    public void createNewBoard(String boardName) {
        // O try-with-resources gerencia o fechamento da conexão
        try (Connection connection = ConnectionConfig.getConnection()) {
            try {
                Board newBoard = new Board(boardName);
                boardDAO.save(newBoard, connection); // Passa a conexão para o DAO


                boardColumnDAO.save(new BoardColumn("A Fazer", newBoard.getId()), connection);
                boardColumnDAO.save(new BoardColumn("Em Progresso", newBoard.getId()), connection);
                boardColumnDAO.save(new BoardColumn("Concluído", newBoard.getId()), connection);

                connection.commit();
                System.out.println("Board '" + boardName + "' criado com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao criar o board. Desfazendo a transação (rollback).");
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter ou fechar a conexão com o banco.");
            e.printStackTrace();
        }
    }


    public Optional<Board> findBoardById(int id) {
        return boardDAO.findById(id);
    }

    public List<Board> findAllBoards() {
        return boardDAO.findAll();
    }
}

