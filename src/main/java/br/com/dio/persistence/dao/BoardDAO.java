package br.com.dio.persistence.dao;


import br.com.dio.model.Board;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

public interface BoardDAO {
    Board save(Board board, Connection connection) throws SQLException;

    Optional<Board> findById(int id);

    List<Board> findAll();

    void update(Board board, Connection connection) throws SQLException;

    void deleteById(int id, Connection connection) throws SQLException;
}
