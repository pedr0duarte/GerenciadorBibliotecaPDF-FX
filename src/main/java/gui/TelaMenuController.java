package gui;

import javafx.fxml.FXML;
import javafx.application.Platform;

public class TelaMenuController {

    @FXML
    private void abrirTelaAdicionar() {
        Navegador.cenaAdicionarPDF();
    }

    @FXML
    private void abrirTelaListar() {
        Navegador.cenaListarPDFs();
    }

    @FXML
    private void abrirTelaBuscar() {
        Navegador.cenaBuscarPDF();
    }

    @FXML
    private void abrirTelaRemover() {
        Navegador.cenaRemoverPDF();
    }

    @FXML
    private void abrirTelaColecoes() {
        Navegador.cenaColecoes();
    }

    @FXML
    private void fecharAplicacao() {
        Platform.exit();
    }

    @FXML
    private void abrirTelaGerenciarBibliotecas() {
        Navegador.cenaGerenciarBibliotecas();
    }
}
