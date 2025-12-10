package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class MyAppointmentsController {

    @FXML private ListView<Appointment> appointmentsListView;
    @FXML private Label statusLabel;

    private AppointmentRepository appointmentRepo;
    private final int currentUserId = 1; // Fix felhasználó

    @FXML
    public void initialize() {
        appointmentRepo = new AppointmentRepository();
        loadAppointments();
    }

    private void loadAppointments() {
        List<Appointment> myApps = appointmentRepo.getAppointmentsByUserId(currentUserId);
        appointmentsListView.getItems().clear();
        appointmentsListView.getItems().addAll(myApps);

        if (myApps.isEmpty()) {
            statusLabel.setText("Jelenleg nincs aktív foglalása.");
            statusLabel.setStyle("-fx-text-fill: #666;");
        } else {
            statusLabel.setText("");
        }
    }

    @FXML
    private void handleDelete() {
        Appointment selected = appointmentsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Kérem válasszon ki egy foglalást a törléshez!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean success = appointmentRepo.deleteById(selected.getId());
        if (success) {
            statusLabel.setText("Foglalás sikeresen törölve.");
            statusLabel.setStyle("-fx-text-fill: green;");
            loadAppointments(); // Lista frissítése
        } else {
            statusLabel.setText("Hiba történt a törlés során.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("services.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) appointmentsListView.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Szolgáltatásaink");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}