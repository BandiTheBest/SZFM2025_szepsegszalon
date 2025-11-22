package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordConfirmField;

    @FXML
    private Label errorLabel;

    @FXML
    private void onRegister() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText();
        String pass2 = passwordConfirmField.getText();

        if (user.isEmpty() || pass.isEmpty() || pass2.isEmpty()) {
            errorLabel.setText("Minden mező kitöltése kötelező!");
            return;
        }

        if (!pass.equals(pass2)) {
            errorLabel.setText("A jelszavak nem egyeznek!");
            return;
        }

        UserRepository repo = new UserRepository();

        if (repo.usernameExists(user)) {
            errorLabel.setText("A felhasználónév már foglalt!");
            return;
        }

        repo.saveUser(user, pass);

        goToLogin();
    }

    @FXML
    private void onBack() {
        goToLogin();
    }

    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Bejelentkezés");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
