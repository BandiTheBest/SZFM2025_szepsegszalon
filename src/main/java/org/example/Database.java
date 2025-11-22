package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:h2:./data/appdb"; // a projekt gyökerében jön létre /data/appdb.mv.db
    private static final String USER = "sa";
    private static final String PASS = "";

    private static Connection connection;

    public static void init() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id IDENTITY PRIMARY KEY,
                        username VARCHAR(255) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL
                    );
                """);
            }

            System.out.println("H2 database initialized.");

        } catch (SQLException e) {
            throw new RuntimeException("Nem sikerült inicializálni az adatbázist!", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
