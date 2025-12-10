package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:h2:./data/appdb";
    private static final String USER = "sa";
    private static final String PASS = "";

    private static Connection connection;

    public static void init() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);

            try (Statement stmt = connection.createStatement()) {
                // 1. Felhasználók tábla (Eredeti)
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id IDENTITY PRIMARY KEY,
                        username VARCHAR(255) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL
                    );
                """);

                // 2. APPOINTMENTS tábla (Egyszerűsített séma, minden foglalásnak ez lesz az alapja)
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS appointments (
                        id IDENTITY PRIMARY KEY,
                        service_name VARCHAR(50),      -- Pl: 'Masszázs' vagy 'Női Hajvágás'
                        user_id INT,                   -- Ki foglalt
                        booking_date DATE,             -- A nap dátuma
                        booking_time VARCHAR(10),      -- Az időpont (pl. 10:30)
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    );
                """);
            }

            System.out.println("H2 database initialized (users + appointments - simple).");

        } catch (SQLException e) {
            throw new RuntimeException("Nem sikerült inicializálni az adatbázist!", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}