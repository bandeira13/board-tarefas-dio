package br.com.dio.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ConnectionConfig {


    private ConnectionConfig() {
    }

    public static Connection getConnection() throws SQLException {

        var url = "jdbc:h2:mem:board;DB_CLOSE_DELAY=-1;MODE=MySQL";

        var user = "sa";
        var password = "";

        var connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
    }
}