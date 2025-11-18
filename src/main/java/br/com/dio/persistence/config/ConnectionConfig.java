package br.com.dio.persistence.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
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
           Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));

            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml",
                    new liquibase.resource.ClassLoaderResourceAccessor(),
                    database);

            liquibase.update(new Contexts(), new LabelExpression());

            System.out.println("Banco de dados (Liquibase) inicializado com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter ou fechar a conexão com o banco.", e);
        } catch (LiquibaseException e) {
            throw new RuntimeException("Erro ao executar as migrações do Liquibase.", e);
        }
    }
}