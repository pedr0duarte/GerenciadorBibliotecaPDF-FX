package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navegador {

    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    private static void carregarCena(String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(Navegador.class.getResource("/gui/" + caminhoFXML));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cenaMenu() {
        carregarCena("TelaMenu.fxml");
    }

    public static void cenaAdicionarPDF() {
        carregarCena("TelaAdicionarPDF.fxml");
    }

    public static void cenaListarPDFs() {
        carregarCena("TelaListarPDFs.fxml");
    }

    public static void cenaBuscarPDF() {
        carregarCena("TelaBuscarPDF.fxml");
    }

    public static void cenaRemoverPDF() {
        carregarCena("TelaRemoverPDF.fxml");
    }

    public static void cenaColecoes() {
        carregarCena("TelaColecoes.fxml");
    }
}
