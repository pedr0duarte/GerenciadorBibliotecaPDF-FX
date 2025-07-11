package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelos.ArquivoPDF;
import modelos.Colecao;

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

    /**
     * Abre uma nova janela (diálogo) de forma modal, ou seja, ela bloqueia a janela
     * principal.
     * É usada para visualizar ou adicionar entradas a uma coleção.
     *
     * @param caminhoFXML O nome do arquivo FXML da nova janela.
     * @param titulo      O título que a nova janela terá.
     * @param colecao     A coleção que será passada para o novo controlador.
     */
    public static void novaJanela(String caminhoFXML, String titulo, Colecao<? extends ArquivoPDF> colecao) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/gui/" + caminhoFXML));
            Parent root = loader.load();

            // Pega o controller da nova janela para passar os dados
            TelaVisualizarColecaoController controller = loader.getController();
            controller.setColecao(colecao);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(titulo);
            dialogStage.initModality(Modality.WINDOW_MODAL); // Bloqueia a janela de trás
            dialogStage.initOwner(stage); // Define a janela principal como "dona"
            dialogStage.setScene(new Scene(root));

            // Mostra a janela e espera ela ser fechada para continuar
            dialogStage.showAndWait();

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

    public static void novaJanelaAdicionar(String caminhoFXML, String titulo, Colecao<? extends ArquivoPDF> colecao) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/gui/" + caminhoFXML));
            Parent root = loader.load();

            // Pega o controller da nova janela para passar os dados
            TelaAdicionarEntradaController controller = loader.getController();
            controller.setColecao(colecao);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(titulo);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}