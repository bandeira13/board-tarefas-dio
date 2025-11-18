package br.com.dio.persistence.config;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ConnectionConfig {

    static {
        try {

            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar o driver JDBC do H2", e);
        }
    }

    public static Connection getConnection() throws SQLException {

        var url = "jdbc:h2:./board_db;DB_CLOSE_DELAY=-1;MODE=MySQL";
        var user = "sa";
        var password = "";

        var connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String createBoards = "CREATE TABLE IF NOT EXISTS boards (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL);";
            String createColumns = "CREATE TABLE IF NOT EXISTS board_columns (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, board_id INT, FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE);";
            String createCards = "CREATE TABLE IF NOT EXISTS cards (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255) NOT NULL, description TEXT, column_id INT, FOREIGN KEY (column_id) REFERENCES board_columns(id) ON DELETE CASCADE);";

            stmt.execute(createBoards);
            stmt.execute(createColumns);
            stmt.execute(createCards);
            conn.commit();
            System.out.println("Banco de dados em memória inicializado com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar o banco de dados.", e);
        }
    }
}