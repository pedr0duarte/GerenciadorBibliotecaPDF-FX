package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelos.*;
import gerenciador.GerenciadorBiblioteca;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
        List<String> autores = Arrays.asList(autor.split(","));

        ArquivoPDF pdf = null;
        switch (tipo) {
            case "Livro":
                // Supondo que você tenha campos para subtitulo, area de conhecimento e ano
                pdf = new Livro(autores, titulo, "Subtitulo Padrão", "Área Padrão", 2024, caminho);
                break;
            case "Slide":
                pdf = new Slide(autores, titulo, "Disciplina Padrão", caminho);
                break;
            case "Nota de Aula":
                pdf = new NotaDeAula(autores, titulo, "Subtitulo Padrão", "Disciplina Padrão", caminho);
                break;
        }

        if (pdf != null) {
            try {
                gerenciador.adicionarArquivo(pdf);
                mostrarAlerta("PDF adicionado com sucesso!");
                limparCampos();
            } catch (Exception e) {
                mostrarAlerta("Erro ao adicionar PDF: " + e.getMessage());
            }
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
