package gui;

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

public class TelaVisualizarColecaoController {

    @FXML
    private Label labelNomeColecao;
    @FXML
    private TableView<ArquivoPDF> tabelaEntradas;
    @FXML
    private TableColumn<ArquivoPDF, String> colunaTitulo;
    @FXML
    private TableColumn<ArquivoPDF, String> colunaAutores;
    @FXML
    private Button btnRemover;

    private Colecao<? extends ArquivoPDF> colecaoAtual;
    private final GerenciadorColecoes gerenciador = GerenciadorColecoes.getInstancia();

    public void initialize() {
        // Configura as colunas da tabela
        colunaTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        colunaAutores
                .setCellValueFactory(data -> new SimpleStringProperty(String.join(", ", data.getValue().getAutores())));
    }

    /**
     * Este método é chamado para passar a coleção selecionada da tela anterior para
     * esta.
     */
    public void setColecao(Colecao<? extends ArquivoPDF> colecao) {
        this.colecaoAtual = colecao;
        labelNomeColecao.setText("Entradas da Coleção: " + colecao.getNome());
        atualizarTabela();
    }

    private void atualizarTabela() {
        if (colecaoAtual != null) {
            ObservableList<ArquivoPDF> entradas = FXCollections.observableArrayList(colecaoAtual.getEntradas());
            tabelaEntradas.setItems(entradas);
        }
    }

    @FXML
    private void removerEntrada() {
        ArquivoPDF selecionado = tabelaEntradas.getSelectionModel().getSelectedItem();
        if (selecionado != null && colecaoAtual != null) {
            boolean sucesso = gerenciador.removerEntrada(colecaoAtual.getNome(), selecionado);
            if (sucesso) {
                atualizarTabela(); // Atualiza a tabela nesta tela
            } else {
                mostrarAlerta("Não foi possível remover a entrada.");
            }
        } else {
            mostrarAlerta("Selecione uma entrada para remover.");
        }
    }

    @FXML
    private void fecharJanela() {
        // Pega a referência do Stage (janela) e a fecha
        Stage stage = (Stage) btnRemover.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}