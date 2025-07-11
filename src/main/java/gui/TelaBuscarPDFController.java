package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelos.ArquivoPDF;
import gerenciador.GerenciadorBiblioteca;

public class TelaBuscarPDFController {

    @FXML
    private TextField campoBusca;
    @FXML
    private TextArea resultadoBusca;

    private GerenciadorBiblioteca gerenciador = GerenciadorBiblioteca.getInstancia();

    @FXML
    private void buscarPDF() {
        String titulo = campoBusca.getText();
        ArquivoPDF pdf = gerenciador.buscarPorTitulo(titulo);

        if (pdf != null) {
            resultadoBusca.setText(pdf.toString());
        } else {
            resultadoBusca.setText("PDF não encontrado.");
        }
    }

    @FXML
    private void voltarMenu() {
        Navegador.cenaMenu();
    }
}
