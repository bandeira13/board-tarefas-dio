package br.com.dio.persistence.dao;

import br.com.dio.model.Card;
import br.com.dio.persistence.config.ConnectionConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDAOImpl implements CardDAO{
    @Override
    public void save(Card card, Connection connection) throws SQLException {
        String sql = "INSERT INTO cards (title, description, column_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, card.getTitle());
            pstmt.setString(2, card.getDescription());
            pstmt.setInt(3, card.getColumnId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void update(Card card, Connection connection) throws SQLException {
        // Você pode atualizar título, descrição ou mover o card (alterando column_id)
        String sql = "UPDATE cards SET title = ?, description = ?, column_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, card.getTitle());
            pstmt.setString(2, card.getDescription());
            pstmt.setInt(3, card.getColumnId());
            pstmt.setInt(4, card.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int cardId, Connection connection) throws SQLException {
        String sql = "DELETE FROM cards WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cardId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Card> findByColumnId(int columnId) {
        String sql = "SELECT id, title, description, column_id FROM cards WHERE column_id = ?";
        List<Card> cards = new ArrayList<>();
        try (Connection conn = ConnectionConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, columnId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card();
                    card.setId(rs.getInt("id"));
                    card.setTitle(rs.getString("title"));
                    card.setDescription(rs.getString("description"));
                    card.setColumnId(rs.getInt("column_id"));
                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }
}
