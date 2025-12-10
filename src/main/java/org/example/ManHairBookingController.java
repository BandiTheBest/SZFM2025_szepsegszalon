package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class ManHairBookingController {
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private Button btnBook;
    @FXML private Button btnBack;

    private AppointmentRepository appointmentRepo;

    @FXML
    public void initialize() {
        appointmentRepo = new AppointmentRepository();

        timeCombo.getItems().addAll(
                "08:00", "09:00", "10:00", "11:00","12:00",
                "14:00", "15:00", "16:00", "17:00", "18:00", "19:00"
        );

    }

    @FXML
    private void handleBooking() {
        if (datePicker.getValue() == null || timeCombo.getValue() == null) {
            showAlert("Hiba!", "Kérem töltsön ki minden mezőt!");
            return;
        }

        boolean success = appointmentRepo.save(
                "Férfi Hajvágás",
                datePicker.getValue(),
                timeCombo.getValue()
        );

        if (success) {
            showAlert("Sikeres foglalás!", "Időpont rögzítve!");
            goBack();
        } else {
            showAlert("Hiba!", "Adatbázis hiba történt a mentéskor.");
        }
    }

    @FXML
    private void goBack() {
        try {
            // Itt ellenőrizd, hogy a services.fxml fájlnév helyes-e!
            FXMLLoader loader = new FXMLLoader(getClass().getResource("services.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnBook.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Szolgáltatásaink");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}