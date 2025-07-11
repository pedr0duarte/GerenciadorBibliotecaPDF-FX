package gui;

import gerenciador.GerenciadorBiblioteca;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import persistencia.Persistencia;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TelaGerenciarBibliotecasController {

    @FXML
    private Label labelBibliotecaAtiva;
    @FXML
    private ListView<String> listaBibliotecas;

    private final GerenciadorBiblioteca gerenciador = GerenciadorBiblioteca.getInstancia();

    @FXML
    public void initialize() {
        atualizarTela();
    }

    private void atualizarTela() {
        // Atualiza o label da biblioteca ativa
        Path caminhoAtivo = gerenciador.getCaminhoBiblioteca();
        if (caminhoAtivo != null) {
            labelBibliotecaAtiva.setText(caminhoAtivo.toString());
        } else {
            labelBibliotecaAtiva.setText("Nenhuma biblioteca ativa. Por favor, crie ou alterne para uma.");
        }

        // Atualiza a lista de bibliotecas conhecidas
        try {
            List<String> todasAsBibliotecas = Persistencia.carregarTodosCaminhos();
            ObservableList<String> items = FXCollections.observableArrayList(todasAsBibliotecas);
            listaBibliotecas.setItems(items);
        } catch (IOException e) {
            mostrarAlerta("Erro", "Não foi possível carregar a lista de bibliotecas.");
        }
    }

    @FXML
    private void criarNova() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecione a Pasta para a Nova Biblioteca");
        File diretorio = directoryChooser.showDialog(labelBibliotecaAtiva.getScene().getWindow());

        if (diretorio != null) {
            try {
                gerenciador.criarNovaBiblioteca(diretorio.getAbsolutePath());
                mostrarAlerta("Sucesso", "Nova biblioteca criada e definida como ativa!");
                atualizarTela();
            } catch (IOException e) {
                mostrarAlerta("Erro", "Falha ao criar a nova biblioteca: " + e.getMessage());
            }
        }
    }

    @FXML
    private void alternarParaSelecionada() {
        String selecionada = listaBibliotecas.getSelectionModel().getSelectedItem();
        if (selecionada != null && !selecionada.isBlank()) {
            try {
                gerenciador.alternarBiblioteca(selecionada);
                mostrarAlerta("Sucesso", "Biblioteca alterada com sucesso!");
                atualizarTela();
            } catch (IOException e) {
                mostrarAlerta("Erro", "Falha ao alternar de biblioteca: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Aviso", "Por favor, selecione uma biblioteca da lista para alternar.");
        }
    }

    @FXML
    private void deletarAtual() {
        if (gerenciador.getCaminhoBiblioteca() == null) {
            mostrarAlerta("Aviso", "Nenhuma biblioteca ativa para deletar.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Você está prestes a deletar a biblioteca ATUAL.");
        confirmacao.setContentText(
                "Esta ação é irreversível e irá apagar a pasta e todos os PDFs contidos nela. Deseja continuar?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                if (gerenciador.deletarBiblioteca()) {
                    mostrarAlerta("Sucesso", "Biblioteca deletada com sucesso.");
                    atualizarTela();
                } else {
                    mostrarAlerta("Erro", "Não foi possível deletar a biblioteca.");
                }
            } catch (IOException e) {
                mostrarAlerta("Erro", "Ocorreu um erro ao deletar a biblioteca: " + e.getMessage());
            }
        }
    }

    @FXML
    private void voltarMenu() {
        Navegador.cenaMenu();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}