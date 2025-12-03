package br.com.dio.persistence.dao;

import br.com.dio.model.BoardColumn;
import br.com.dio.persistence.config.ConnectionConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BoardColumnDAOImpl implements BoardColumnDAO {
    @Override
    public void save(BoardColumn column, Connection connection) throws SQLException {
        String sql = "INSERT INTO board_columns (name, board_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, column.getName());
            pstmt.setInt(2, column.getBoardId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<BoardColumn> findAllByBoardId(int boardId) {
        String sql = "SELECT id, name, board_id FROM board_columns WHERE board_id = ?";
        List<BoardColumn> columns = new ArrayList<>();

        try (Connection conn = ConnectionConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, boardId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BoardColumn column = new BoardColumn();
                    column.setId(rs.getInt("id"));
                    column.setName(rs.getString("name"));
                    column.setBoardId(rs.getInt("board_id"));
                    columns.add(column);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }
}
