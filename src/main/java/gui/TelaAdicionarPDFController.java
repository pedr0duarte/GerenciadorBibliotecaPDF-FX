package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelos.*;
import gerenciador.GerenciadorBiblioteca;

import java.io.File;

public class TelaAdicionarPDFController {

    @FXML
    private TextField campoTitulo;
    @FXML
    private TextField campoAutor;
    @FXML
    private ComboBox<String> comboTipo;
    @FXML
    private TextField campoCaminho;

    private GerenciadorBiblioteca gerenciador = GerenciadorBiblioteca.getInstancia();

    @FXML
    public void initialize() {
        comboTipo.getItems().addAll("Livro", "Slide", "Nota de Aula");
    }

    @FXML
    private void selecionarArquivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            campoCaminho.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void adicionarPDF() {
        String titulo = campoTitulo.getText();
        String autor = campoAutor.getText();
        String tipo = comboTipo.getValue();
        String caminho = campoCaminho.getText();

        ArquivoPDF pdf = switch (tipo) {
            case "Livro" -> new Livro(titulo, autor, caminho);
            case "Slide" -> new Slide(titulo, autor, caminho);
            case "Nota de Aula" -> new NotaDeAula(titulo, autor, caminho);
            default -> null;
        };

        if (pdf != null) {
            gerenciador.adicionarArquivo(pdf);
            mostrarAlerta("PDF adicionado com sucesso!");
            limparCampos();
        } else {
            mostrarAlerta("Selecione um tipo válido.");
        }
    }

    @FXML
    private void voltarMenu() {
        Navegador.cenaMenu();
    }

    private void limparCampos() {
        campoTitulo.clear();
        campoAutor.clear();
        campoCaminho.clear();
        comboTipo.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
