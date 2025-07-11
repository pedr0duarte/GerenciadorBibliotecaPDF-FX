package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import gerenciador.GerenciadorColecoes;

public class TelaColecoesController {

    @FXML
    private ListView<String> listaColecoes;
    @FXML
    private TextField campoNomeColecao;

    private GerenciadorColecoes gerenciador = GerenciadorColecoes.getInstancia();

    @FXML
    public void initialize() {
        atualizarLista();
    }

    private void atualizarLista() {
        ObservableList<String> nomes = FXCollections.observableArrayList(gerenciador.getNomesColecoes());
        listaColecoes.setItems(nomes);
    }

    @FXML
    private void criarColecao() {
        String nome = campoNomeColecao.getText();
        gerenciador.criarColecao(nome);
        campoNomeColecao.clear();
        atualizarLista();
    }

    @FXML
    private void removerColecao() {
        String selecionado = listaColecoes.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            gerenciador.removerColecao(selecionado);
            atualizarLista();
        }
    }

    @FXML
    private void voltarMenu() {
        Navegador.cenaMenu();
    }
}
