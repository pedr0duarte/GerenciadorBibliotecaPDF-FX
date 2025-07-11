package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import modelos.ArquivoPDF;
import gerenciador.GerenciadorBiblioteca;

public class TelaListarPDFsController {

    @FXML
    private TableView<ArquivoPDF> tabelaPDFs;
    @FXML
    private TableColumn<ArquivoPDF, String> colunaTitulo;
    @FXML
    private TableColumn<ArquivoPDF, String> colunaAutor;
    @FXML
    private TableColumn<ArquivoPDF, String> colunaTipo;

    private GerenciadorBiblioteca gerenciador = GerenciadorBiblioteca.getInstancia();

    @FXML
    public void initialize() {
        colunaTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        colunaAutor
                .setCellValueFactory(data -> new SimpleStringProperty(String.join(", ", data.getValue().getAutores())));
        colunaTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo()));

        ObservableList<ArquivoPDF> lista = FXCollections.observableArrayList(gerenciador.listarRegistros());
        tabelaPDFs.setItems(lista);
    }

    @FXML
    private void voltarMenu() {
        Navegador.cenaMenu();
    }

    @FXML
    private void editarPDF() {
        ArquivoPDF selecionado = tabelaPDFs.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            boolean salvo = Navegador.abrirJanelaEdicao(selecionado);
            if (salvo) {
                // Atualiza a tabela se as alterações foram salvas
                initialize();
            }
        } else {
            // (Opcional) Mostrar um alerta se nada estiver selecionado
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Por favor, selecione um PDF na tabela para editar.");
            alert.showAndWait();
        }
    }
}
