package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import gerenciador.GerenciadorBiblioteca;

public class TelaRemoverPDFController {

    @FXML
    private TextField campoTituloRemover;

    private GerenciadorBiblioteca gerenciador = GerenciadorBiblioteca.getInstancia();

    @FXML
    private void removerPDF() {
        String titulo = campoTituloRemover.getText();
        boolean sucesso = gerenciador.removerPorTitulo(titulo);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (sucesso) {
            alert.setContentText("PDF removido com sucesso.");
        } else {
            alert.setContentText("PDF não encontrado.");
        }
        alert.showAndWait();
    }

    @FXML
    private void voltarMenu() {
        Navegador.cenaMenu();
    }
}
