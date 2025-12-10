package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label; // Új import

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
        serviceCombo.setItems(FXCollections.observableArrayList(
            "60 perces Hátmasszázs", "30 perces Frissítő masszázs", "Teljes testmasszázs"
        ));
        serviceCombo.getSelectionModel().selectFirst(); // Válassza ki az elsőt alapértelmezetten

        // 2. Eseménykezelők beállítása a dátumváltáshoz
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                updateAvailableTimeSlots(newDate);
            } else {
                timeCombo.getItems().clear();
                btnBook.setDisable(true);
            }
        });
        
        // A szolgáltatás kiválasztásának változásakor is frissíteni kell a szabad időpontokat
        serviceCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (datePicker.getValue() != null) {
                updateAvailableTimeSlots(datePicker.getValue());
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
                // Nem lehet ma és korábbi nap (Csak holnaptól engedi)
                setDisable(empty || date.isBefore(LocalDate.now())); 
            }
        });
    }

    /**
     * Kiszámítja és megjeleníti a szabad időpontokat a kiválasztott dátum alapján.
     */
    private void updateAvailableTimeSlots(LocalDate date) {
        timeCombo.getItems().clear();
        messageLabel.setText("");

        // 1. Lekérjük az összes foglalt időpontot az adott napra és szolgáltatásra
        // Mivel az egyszerűsített séma alapján minden masszázst a "Masszázs" kategóriába teszünk:
        List<Appointment> occupied = appointmentRepository.getOccupiedAppointments(date, SERVICE_NAME);

        // 2. Kigyűjtjük a foglalt időpontokat egy String listába (pl. ["09:00", "14:00"])
        List<String> occupiedTimes = occupied.stream()
            .map(Appointment::getBookingTime) // Csak az időt kérjük le
            .collect(Collectors.toList());

        // 3. Összevetjük az összes lehetséges időpontot a foglaltakkal
        List<String> availableTimes = new ArrayList<>();
        for (String time : ALL_TIMES) {
            if (!occupiedTimes.contains(time)) {
                // Csak akkor adjuk hozzá, ha még nincs lefoglalva
                availableTimes.add(time);
            }
        }
        
        // 4. Megjelenítjük a szabad időpontokat
        if (availableTimes.isEmpty()) {
            timeCombo.setPromptText("Nincs szabad időpont ezen a napon.");
        } else {
            timeCombo.setItems(FXCollections.observableArrayList(availableTimes));
            timeCombo.setPromptText("Válasszon időpontot");
        }
        
        btnBook.setDisable(true);
    }
    
    @FXML
    private void handleBooking() {
        // Logika a következő commitban!
    }
    
    @FXML
    private void goBack() {
        // TODO: A Launcher osztállyal való visszaváltás a services.fxml-re
    }
}