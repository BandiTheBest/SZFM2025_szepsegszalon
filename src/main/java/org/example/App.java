package org.example;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Database.init();

        var loader = new FXMLLoader(getClass().getResource("login.fxml"));
        var scene = new Scene(loader.load());

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Bejelentkezés");
        stage.show();
    }
}
