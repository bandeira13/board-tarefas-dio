package br.com.dio.service;

import br.com.dio.model.Board;
import br.com.dio.model.BoardColumn;
import br.com.dio.persistence.config.ConnectionConfig;
import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardColumnDAOImpl;
import br.com.dio.persistence.dao.BoardDAO;
import br.com.dio.persistence.dao.BoardDAOImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);
    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO;


    public BoardService() {
        this.boardDAO = new BoardDAOImpl();
        this.boardColumnDAO = new BoardColumnDAOImpl();
    }
    public void createNewBoard(String boardName) {
        try (Connection connection = ConnectionConfig.getConnection()) {
            try {
                Board newBoard = new Board(boardName);
                boardDAO.save(newBoard, connection);
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

    public boolean deleteBoard(int boardId) {

        if (boardDAO.findById(boardId).isEmpty()) {
            System.err.println("Não foi encontrado um board com id " + boardId);
            return false;
        }

        try (Connection connection = ConnectionConfig.getConnection()) {
            try {
                boardDAO.deleteById(boardId, connection);
                connection.commit();
                System.out.println("Board com ID " + boardId + " foi deletado com sucesso.");
                return true;
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Erro ao deletar o board. Desfazendo a transação (rollback).");
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter ou fechar a conexão com o banco.");
            e.printStackTrace();
            return false;
        }
    }
}

