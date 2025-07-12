package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelos.*;
import gerenciador.GerenciadorBiblioteca;
import excecoes.Excecoes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TelaAdicionarPDFController {

    // Campos Comuns
    @FXML
    private ComboBox<String> comboTipo;
    @FXML
    private TextField campoTitulo;
    @FXML
    private TextField campoAutores;
    @FXML
    private TextField campoCaminho;

    // Campos Específicos
    @FXML
    private Label labelSubtitulo;
    @FXML
    private TextField campoSubtitulo;
    @FXML
    private Label labelDisciplina;
    @FXML
    private TextField campoDisciplina;
    @FXML
    private Label labelArea;
    @FXML
    private TextField campoArea;
    @FXML
    private Label labelAno;
    @FXML
    private TextField campoAno;

    private final GerenciadorBiblioteca gerenciador = GerenciadorBiblioteca.getInstancia();

    @FXML
    public void initialize() {
        // Popula o ComboBox
        comboTipo.getItems().addAll("Livro", "Nota de Aula", "Slide");

        // Adiciona um listener para mostrar/esconder campos
        comboTipo.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toggleCampos(newValue));

        // Inicia com todos os campos específicos escondidos
        toggleCampos(null);
    }

    private void toggleCampos(String tipo) {
        // Esconde todos por padrão
        labelSubtitulo.setVisible(false);
        campoSubtitulo.setVisible(false);
        labelDisciplina.setVisible(false);
        campoDisciplina.setVisible(false);
        labelArea.setVisible(false);
        campoArea.setVisible(false);
        labelAno.setVisible(false);
        campoAno.setVisible(false);

        if (tipo == null)
            return;

        // Mostra os campos com base no tipo selecionado
        switch (tipo) {
            case "Livro":
                labelSubtitulo.setVisible(true);
                campoSubtitulo.setVisible(true);
                labelArea.setVisible(true);
                campoArea.setVisible(true);
                labelAno.setVisible(true);
                campoAno.setVisible(true);
                break;
            case "Nota de Aula":
                labelSubtitulo.setVisible(true);
                campoSubtitulo.setVisible(true);
                labelDisciplina.setVisible(true);
                campoDisciplina.setVisible(true);
                break;
            case "Slide":
                labelDisciplina.setVisible(true);
                campoDisciplina.setVisible(true);
                break;
        }
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
        String tipo = comboTipo.getValue();
        if (tipo == null) {
            mostrarAlerta("Erro", "Por favor, selecione um tipo de documento.");
            return;
        }

        try {
            // Coleta os dados dos campos
            String titulo = campoTitulo.getText();
            List<String> autores = Arrays.asList(campoAutores.getText().split(",\\s*"));
            String caminho = campoCaminho.getText();

            ArquivoPDF pdf = null;
            // Cria o objeto correto com base no tipo
            switch (tipo) {
                case "Livro":
                    pdf = new Livro(autores, titulo, campoSubtitulo.getText(), campoArea.getText(),
                            Integer.parseInt(campoAno.getText()), caminho);
                    break;
                case "Nota de Aula":
                    pdf = new NotaDeAula(autores, titulo, campoSubtitulo.getText(), campoDisciplina.getText(), caminho);
                    break;
                case "Slide":
                    pdf = new Slide(autores, titulo, campoDisciplina.getText(), caminho);
                    break;
            }

            if (pdf != null) {
                gerenciador.adicionarArquivo(pdf);
                mostrarAlerta("Sucesso", "PDF adicionado com sucesso!");
                limparCampos();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro de Formato", "O ano de publicação deve ser um número válido.");
        } catch (IOException | Excecoes e) {
            mostrarAlerta("Erro", "Não foi possível adicionar o PDF: " + e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro",
                    "Ocorreu um erro inesperado. Verifique se todos os campos obrigatórios estão preenchidos.");
        }
    }

    @FXML
    private void voltarMenu() {
        Navegador.cenaMenu();
    }

    private void limparCampos() {
        comboTipo.getSelectionModel().clearSelection();
        campoTitulo.clear();
        campoAutores.clear();
        campoCaminho.clear();
        campoSubtitulo.clear();
        campoDisciplina.clear();
        campoArea.clear();
        campoAno.clear();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}