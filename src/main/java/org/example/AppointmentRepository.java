package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {

    /**
     * Lekéri az összes lefoglalt időpontot egy adott napra és szolgáltatásra.
     * (Az egyszerűsített séma alapján).
     */
    public List<Appointment> getOccupiedAppointments(LocalDate date, String serviceName) {
        List<Appointment> occupied = new ArrayList<>();
        String sql = "SELECT id, service_name, user_id, booking_date, booking_time FROM appointments WHERE booking_date = ? AND service_name = ?";

        Connection conn = Database.getConnection(); // Connection lekérése

        try (PreparedStatement stmt = conn.prepareStatement(sql)) { // Csak a Statement kerül a try-ba

            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, serviceName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    occupied.add(new Appointment(
                            rs.getInt("id"),
                            rs.getString("service_name"),
                            rs.getInt("user_id"),
                            rs.getDate("booking_date").toLocalDate(),
                            rs.getString("booking_time")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return occupied;
    }
    
    /**
     * Új időpont lefoglalása és mentése az adatbázisba.
     *
     * @param app A mentendő Appointment objektum (Tartalmazza az userId-t!)
     * @return true, ha sikeres a mentés.
     */
    public boolean saveAppointment(Appointment app) {
        String sql = "INSERT INTO appointments (service_name, user_id, booking_date, booking_time) VALUES (?, ?, ?, ?)";

        Connection conn = Database.getConnection(); // Connection lekérése

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Csak a Statement kerül a try-ba

            stmt.setString(1, app.getServiceName());
            stmt.setInt(2, app.getUserId());
            stmt.setDate(3, Date.valueOf(app.getBookingDate()));
            stmt.setString(4, app.getBookingTime());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        app.setId(rs.getInt(1));
                    }
                }
            }
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getAppointmentsByUserId(int userId) {
        List<Appointment> appointments = new ArrayList<>();
        // Időrendben kérjük le (ORDER BY)
        String sql = "SELECT * FROM appointments WHERE user_id = ? ORDER BY booking_date, booking_time";

        Connection conn = Database.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                            rs.getInt("id"),
                            rs.getString("service_name"),
                            rs.getInt("user_id"),
                            rs.getDate("booking_date").toLocalDate(),
                            rs.getString("booking_time")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * Töröl egy foglalást az adatbázisból ID alapján.
     * Erre van szükség a "Lemondás" gombhoz.
     */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        Connection conn = Database.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0; // Ha 0-nál több sort törölt, akkor sikeres volt
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}