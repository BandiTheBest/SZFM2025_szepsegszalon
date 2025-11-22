package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void onLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        UserRepository repo = new UserRepository();

        if (repo.validateUser(user, pass)) {
            loadMainWindow();
        } else {
            errorLabel.setText("Hibás felhasználónév vagy jelszó!");
        }
    }

    @FXML
    private void onRegisterClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registration.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Regisztráció");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("YourBeauty Szépségszalon - Időpontfoglalás");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
