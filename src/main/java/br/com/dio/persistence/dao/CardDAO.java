package br.com.dio.persistence.dao;

import br.com.dio.model.Card;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardDAO {
    void save(Card card, Connection connection) throws SQLException;

    void update(Card card, Connection connection) throws SQLException;

    void delete(int cardId, Connection connection) throws SQLException;

    List<Card> findByColumnId(int columnId);
    Optional<Card> findById(int cardId);
}
