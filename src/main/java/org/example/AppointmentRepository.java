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
        // Lekérjük az összes foglalt időpontot az adott napra és szolgáltatásra.
        // A user_id-t is lekérjük, bár a masszázs logikájához csak a booking_time kell.
        String sql = "SELECT id, service_name, user_id, booking_date, booking_time FROM appointments WHERE booking_date = ? AND service_name = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // A DATE paraméter beállítása
            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, serviceName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Appointment objektum létrehozása az egyszerűsített sémából
                    occupied.add(new Appointment(
                        rs.getInt("id"),
                        rs.getString("service_name"),
                        rs.getInt("user_id"), // Felhasználó ID-je
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
        // user_id hozzáadva a mentési SQL-hez a sémának megfelelően
        String sql = "INSERT INTO appointments (service_name, user_id, booking_date, booking_time) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, app.getServiceName());
            stmt.setInt(2, app.getUserId()); 
            stmt.setDate(3, Date.valueOf(app.getBookingDate()));
            stmt.setString(4, app.getBookingTime());

            int affectedRows = stmt.executeUpdate();
            
            // Ha sikeres a mentés, beállítjuk az ID-t az objektumra
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
}