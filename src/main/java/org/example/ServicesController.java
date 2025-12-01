package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

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

        btnNails.setOnAction(e ->
                System.out.println("Mukorom szolgaltatas kivalasztva!")
        );

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
}
