package br.com.dio.persistence.dao;

import br.com.dio.model.Card;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CardDAO {
    void save(Card card, Connection connection) throws SQLException;

    void update(Card card, Connection connection) throws SQLException;

    void delete(int cardId, Connection connection) throws SQLException;

    List<Card> findByColumnId(int columnId);
}
