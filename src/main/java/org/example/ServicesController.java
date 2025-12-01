package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ServicesController {

    @FXML
    private Button btnNails;

    @FXML
    private Button btnFemaleHair;

    @FXML
    private Button btnMaleHair;

    @FXML
    private Button btnMassage;

    @FXML
    public void initialize() {

        btnNails.setOnAction(e -> handleNails());

        btnFemaleHair.setOnAction(e ->
                System.out.println("Noi fodraszat kivalasztva!")
        );

        btnMaleHair.setOnAction(e ->
                System.out.println("Ferfi fodraszat kivalasztva!")
        );

        btnMassage.setOnAction(e ->
                System.out.println("Masszazs kivalasztva!")
        );
    }

    @FXML
    private void handleNails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nails_booking.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btnNails.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Műköröm szolgáltatás – Időpontfoglalás");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
