package org.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MassageBookingController {

    // FXML elemek (megfelel a massage_booking.fxml-ben szereplő fx:id-knak)
    @FXML private ComboBox<String> serviceCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private Button btnBook;
    @FXML private Label messageLabel;
    @FXML private Button btnBack;

    private final AppointmentRepository appointmentRepository = new AppointmentRepository();

    // Ez a masszázs szolgáltatás neve, amit a lekérdezéshez és mentéshez használunk
    private static final String SERVICE_NAME = "Masszázs";

    // Állandó időpontok
    private static final List<String> ALL_TIMES = List.of(
            "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00"
    );

    @FXML
    public void initialize() {
        // 1. Szolgáltatások ComboBox feltöltése
        serviceCombo.setItems(FXCollections.observableArrayList(
                "60 perces Hátmasszázs", "30 perces Frissítő masszázs", "Teljes testmasszázs"
        ));
        serviceCombo.getSelectionModel().selectFirst();

        // 2. Dátumválasztás eseménykezelője
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                updateAvailableTimeSlots(newDate);
            } else {
                timeCombo.getItems().clear();
                btnBook.setDisable(true);
            }
        });

        // 3. Foglalás gomb inaktiválása, amíg nincs időpont kiválasztva
        timeCombo.valueProperty().addListener((obs, oldTime, newTime) -> {
            btnBook.setDisable(newTime == null);
        });

        // 4. Múltbeli dátumok tiltása
        setupDatePickerConstraints();
    }

    // Csak jövőbeni dátum engedélyezése
    private void setupDatePickerConstraints() {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    /**
     * Megjeleníti az időpontokat a listában.
     * MÓDOSÍTVA: Nem szűrjük ki a foglaltakat, mindig mindet mutatja.
     */
    private void updateAvailableTimeSlots(LocalDate date) {
        timeCombo.getItems().clear();

        // Egyszerűen betöltjük az összes fix időpontot
        timeCombo.setItems(FXCollections.observableArrayList(ALL_TIMES));
        timeCombo.setPromptText("Válasszon időpontot");
    }

    @FXML
    private void handleBooking() {
        // 1. Validáció: Ellenőrizzük, hogy minden ki van-e töltve
        if (datePicker.getValue() == null || timeCombo.getValue() == null || serviceCombo.getValue() == null) {
            showAlert("Hiba!", "Kérem töltsön ki minden mezőt!");
            return;
        }

        // --- ELLENŐRZÉS: Itt nézzük meg az adatbázisban, hogy foglalt-e ---
        boolean isTaken = appointmentRepository.isAppointmentTaken(
                SERVICE_NAME,         // "Masszázs"
                datePicker.getValue(),
                timeCombo.getValue()
        );

        if (isTaken) {
            // Ha foglalt, feldobjuk a hibaüzenetet és KILÉPÜNK
            showAlert("Sikertelen foglalás", "Ez az időpont sajnos már foglalt! Kérem válasszon másikat.");
            return;
        }
        // --------------------------------------------------

        // 2. Mentés (Ha szabad volt az időpont)
        try {
            int currentUserId = 1;

            Appointment newAppointment = new Appointment(
                    SERVICE_NAME,
                    currentUserId,
                    datePicker.getValue(),
                    timeCombo.getValue()
            );

            if (appointmentRepository.saveAppointment(newAppointment)) {
                // Siker üzenet
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Sikeres Foglalás");
                alert.setHeaderText("A masszázs időpont lefoglalva!");
                alert.setContentText(String.format(
                        "Sikeres foglalás!\nSzolgáltatás: %s\nDátum: %s\nIdőpont: %s",
                        serviceCombo.getValue(), datePicker.getValue().toString(), timeCombo.getValue()
                ));
                alert.showAndWait();

                // Visszalépés a főmenübe
                goBack();

            } else {
                showAlert("Hiba!", "Adatbázis hiba történt a mentéskor.");
            }

        } catch (Exception e) {
            showAlert("Hiba!", "Váratlan hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("services.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Szolgáltatások Kiválasztása");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Segédmetódus a felugró ablakokhoz
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}