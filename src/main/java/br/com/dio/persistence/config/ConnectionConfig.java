package br.com.dio.persistence.config;


import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public final class ConnectionConfig{


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar o driver JDBC do MySQL", e);
        }
    }

    public static Connection getConnection() throws SQLException{
        var url = "jdbc:mysql://localhost/board";
        var user = "board";
        var password = "board";
        var connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;

    }
}