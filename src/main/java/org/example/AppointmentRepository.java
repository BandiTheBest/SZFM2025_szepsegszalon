package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AppointmentRepository {

    public boolean save(String serviceName, LocalDate date, String time) {
        // Elkérjük a már létező kapcsolatot a Database osztálytól
        Connection conn = Database.getConnection();

        String sql = "INSERT INTO appointments (service_name, booking_date, booking_time) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, serviceName);
            // A LocalDate-et átalakítjuk java.sql.Date típusra
            pstmt.setString(2, date.toString());
            pstmt.setString(3, time);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}