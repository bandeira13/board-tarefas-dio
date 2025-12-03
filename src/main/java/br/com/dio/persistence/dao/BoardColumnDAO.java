package br.com.dio.persistence.dao;

import br.com.dio.model.BoardColumn;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Repository
public interface BoardColumnDAO {
    void save(BoardColumn column, Connection connection) throws SQLException;

    List<BoardColumn> findAllByBoardId(int boardId);

}
