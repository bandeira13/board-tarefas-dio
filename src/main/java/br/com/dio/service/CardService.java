package br.com.dio.service;

import br.com.dio.model.Card;
import br.com.dio.persistence.config.ConnectionConfig;
import br.com.dio.persistence.dao.CardDAO;
import br.com.dio.persistence.dao.CardDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class CardService {
    private final CardDAO cardDAO;

    public CardService() {
        this.cardDAO = new CardDAOImpl();
    }


    public void createNewCard(String title, String description, int columnId) {
        try (Connection connection = ConnectionConfig.getConnection()) {
            try {
                Card newCard = new Card(title, description, columnId);
                cardDAO.save(newCard, connection);
                connection.commit();
                System.out.println("Card '" + title + "' criado com sucesso!");
            } catch (SQLException e) {
                System.err.println("Erro ao criar o card. Desfazendo a transação.");
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Erro de conexão com o banco de dados.");
            e.printStackTrace();
        }
    }

    public void moveCard(Card card, int newColumnId) {

        if (card == null || card.getId() == 0) {
            System.err.println("Erro: Card inválido ou não salvo no banco.");
            return;
        }

        try (Connection connection = ConnectionConfig.getConnection()) {
            try {
                card.setColumnId(newColumnId);
                cardDAO.update(card, connection);
                connection.commit();
                System.out.println("Card '" + card.getTitle() + "' movido com sucesso!");
            } catch (SQLException e) {
                System.err.println("Erro ao mover o card. Desfazendo a transação.");
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Erro de conexão com o banco de dados.");
            e.printStackTrace();
        }
    }
    public void deleteCard(int cardId) {
        try (Connection connection = ConnectionConfig.getConnection()) {
            try {
                cardDAO.delete(cardId, connection);
                connection.commit();
                System.out.println("Card com ID " + cardId + " deletado com sucesso.");
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Erro ao deletar o card. Desfazendo a transação.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Erro de conexão com o banco de dados.");
            e.printStackTrace();
        }
    }
}
