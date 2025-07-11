package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Principal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Navegador.setStage(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("/gui/TelaMenu.fxml"));
        primaryStage.setTitle("Gerenciador de Biblioteca PDF");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
