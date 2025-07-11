package gui;

import gerenciador.GerenciadorBiblioteca;
import gerenciador.GerenciadorColecoes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import modelos.ArquivoPDF;
import modelos.Colecao;

import java.util.List;
import java.util.stream.Collectors;

public class TelaAdicionarEntradaController {

    @FXML
    private Label labelTitulo;
    @FXML
    private TableView<ArquivoPDF> tabelaPdfs;
    @FXML
    private TableColumn<ArquivoPDF, String> colunaTitulo;
    @FXML
    private TableColumn<ArquivoPDF, String> colunaAutores;
    @FXML
    private Button btnAdicionar;

    private Colecao<? extends ArquivoPDF> colecaoAtual;
    private final GerenciadorColecoes gerenciadorColecoes = GerenciadorColecoes.getInstancia();
    private final GerenciadorBiblioteca gerenciadorBiblioteca = GerenciadorBiblioteca.getInstancia();

    public void initialize() {
        colunaTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        colunaAutores
                .setCellValueFactory(data -> new SimpleStringProperty(String.join(", ", data.getValue().getAutores())));
    }

    /**
     * Recebe a coleção da tela anterior e filtra os PDFs compatíveis.
     */
    public void setColecao(Colecao<? extends ArquivoPDF> colecao) {
        this.colecaoAtual = colecao;
        labelTitulo.setText("Adicionar à Coleção: " + colecao.getNome());
        carregarPDFsCompativeis();
    }

    private void carregarPDFsCompativeis() {
        if (colecaoAtual == null)
            return;

        // Filtra os PDFs da biblioteca principal
        List<ArquivoPDF> compativeis = gerenciadorBiblioteca.listarRegistrosBibliotecaAtual().stream()
                .filter(pdf -> colecaoAtual.getTipo().isInstance(pdf)) // Mesmo tipo
                .filter(pdf -> pdf.getAutores().contains(colecaoAtual.getAutor())) // Mesmo autor
                .filter(pdf -> !colecaoAtual.getEntradas().contains(pdf)) // Que ainda não estão na coleção
                .collect(Collectors.toList());

        ObservableList<ArquivoPDF> lista = FXCollections.observableArrayList(compativeis);
        tabelaPdfs.setItems(lista);
    }

    @FXML
    private void adicionarEntrada() {
        ArquivoPDF selecionado = tabelaPdfs.getSelectionModel().getSelectedItem();
        if (selecionado != null && colecaoAtual != null) {
            boolean sucesso = gerenciadorColecoes.adicionarEntrada(colecaoAtual.getNome(), selecionado);
            if (sucesso) {
                mostrarAlerta("Sucesso", "Entrada adicionada com sucesso!");
                // Remove o item da tabela para não ser adicionado de novo
                tabelaPdfs.getItems().remove(selecionado);
            } else {
                mostrarAlerta("Erro", "Não foi possível adicionar a entrada. Verifique o limite da coleção.");
            }
        } else {
            mostrarAlerta("Aviso", "Selecione um PDF para adicionar.");
        }
    }

    @FXML
    private void fecharJanela() {
        Stage stage = (Stage) btnAdicionar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}