package org.example;

import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class MassageBookingController {
    
    // FXML elemek (megfelel a massage_booking.fxml-ben szereplő fx:id-knak)
    @FXML private ComboBox<String> serviceCombo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeCombo;
    @FXML private Button btnBook;
    @FXML private Label messageLabel;

    private final AppointmentRepository appointmentRepository = new AppointmentRepository();
    
    // Ez a masszázs szolgáltatás neve, amit a lekérdezéshez használunk
    private static final String SERVICE_NAME = "Masszázs";
    
    // Állandó időpontok (egyszerűsített séma)
    private static final List<String> ALL_TIMES = List.of(
        "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00"
    );

    @FXML
    public void initialize() {
        // 1. Szolgáltatások ComboBox feltöltése (egyszerűsített séma, csak a Masszázs típusok)
        // Mivel nincs Service táblánk, most csak fix értékkel töltjük fel a ComboBox-ot.
        serviceCombo.setItems(FXCollections.observableArrayList(
            "60 perces Hátmasszázs", "30 perces Frissítő masszázs", "Teljes testmasszázs"
        ));
        serviceCombo.getSelectionModel().selectFirst(); // Válassza ki az elsőt alapértelmezetten

        // 2. Eseménykezelők beállítása a dátumváltáshoz
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                // Kalkuláció a következő commitban!
                updateAvailableTimeSlots(newDate);
            } else {
                timeCombo.getItems().clear();
                btnBook.setDisable(true);
            }
        });

        // 3. Foglalás gomb inaktiválása, amíg nincs időpont kiválasztva
        timeCombo.valueProperty().addListener((obs, oldTime, newTime) -> {
            btnBook.setDisable(newTime == null);
            messageLabel.setText("");
        });
        
        // 4. Dátumválasztás korlátozása
        setupDatePickerConstraints();
    }
    
    // Csak jövőbeni dátum engedélyezése
    private void setupDatePickerConstraints() {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now())); // Nem lehet ma és korábbi nap
            }
        });
    }

    // A masszázs logikája a 6-os, 7-es és 8-as committokban jön
    private void updateAvailableTimeSlots(LocalDate date) {
        // Logika a következő commitban!
    }

    @FXML
    private void handleBooking() {
        // Logika a 8-as commitban!
    }
    
    @FXML
    private void goBack() {
        // TODO: A Launcher osztállyal való visszaváltás a services.fxml-re
    }
}