package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {

    private final Connection conn = Database.getConnection();

    public boolean usernameExists(String username) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = ?"
        )) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String username, String password) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password) VALUES (?, ?)"
        )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateUser(String username, String password) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?"
        )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) == 1;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
