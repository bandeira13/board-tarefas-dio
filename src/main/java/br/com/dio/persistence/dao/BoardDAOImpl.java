package br.com.dio.persistence.dao;

import br.com.dio.model.Board;
import br.com.dio.persistence.config.ConnectionConfig;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardDAOImpl implements BoardDAO{
    @Override
    public Board save(Board board, Connection connection) throws SQLException {
        String sql = "INSERT INTO boards (name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, board.getName());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    board.setId(rs.getInt(1));
                }
            }
        }
        return board;
    }

    @Override
    public Optional<Board> findById(int id) {
        String sql = "SELECT id, name FROM boards WHERE id = ?";
        try (Connection conn = ConnectionConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Board board = new Board(rs.getString("name"));
                    board.setId(rs.getInt("id"));
                    return Optional.of(board);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Em um projeto real, use um logger
        }
        return Optional.empty();
    }

    @Override
    public List<Board> findAll() {
        String sql = "SELECT id, name FROM boards";
        List<Board> boards = new ArrayList<>();
        try (Connection conn = ConnectionConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Board board = new Board(rs.getString("name"));
                board.setId(rs.getInt("id"));
                boards.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    @Override
    public void update(Board board, Connection connection) throws SQLException {
        String sql = "UPDATE boards SET name = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, board.getName());
            pstmt.setInt(2, board.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(int id, Connection connection) throws SQLException {
        String sql = "DELETE FROM boards WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

}
