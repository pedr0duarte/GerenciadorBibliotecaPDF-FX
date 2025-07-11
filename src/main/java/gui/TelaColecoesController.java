package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.FileChooser;

import modelos.Colecao;
import modelos.ArquivoPDF;
import gerenciador.GerenciadorColecoes;

import java.io.File;
import java.io.IOException;

public class TelaColecoesController {

    // Componentes da TableView
    @FXML
    private TableView<Colecao<? extends ArquivoPDF>> tabelaColecoes;
    @FXML
    private TableColumn<Colecao<? extends ArquivoPDF>, String> colunaNome;
    @FXML
    private TableColumn<Colecao<? extends ArquivoPDF>, String> colunaAutor;
    @FXML
    private TableColumn<Colecao<? extends ArquivoPDF>, String> colunaTipo;
    @FXML
    private TableColumn<Colecao<? extends ArquivoPDF>, String> colunaEntradas;
    @FXML
    private TableColumn<Colecao<? extends ArquivoPDF>, String> colunaLimite;

    // Campos para criar nova coleção
    @FXML
    private TextField campoNomeColecao;
    @FXML
    private TextField campoAutorColecao;
    @FXML
    private TextField campoLimiteColecao;
    @FXML
    private ComboBox<String> comboTipoColecao;

    // Botões de ação para a coleção selecionada
    @FXML
    private Button btnAdicionarEntrada;
    @FXML
    private Button btnVisualizar;
    @FXML
    private Button btnExportar;
    @FXML
    private Button btnCompactar;

    private final GerenciadorColecoes gerenciador = GerenciadorColecoes.getInstancia();

    @FXML
    public void initialize() {
        // Popula o ComboBox com os tipos de PDF
        comboTipoColecao.getItems().addAll("Livro", "Slide", "NotaDeAula");

        // Configura como cada coluna irá obter os dados do objeto Colecao
        colunaNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colunaAutor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAutor()));
        colunaTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo().getSimpleName()));
        colunaLimite.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getLimite())));
        colunaEntradas.setCellValueFactory(
                data -> new SimpleStringProperty(String.valueOf(data.getValue().getEntradas().size())));

        // Listener para habilitar/desabilitar botões de ação
        tabelaColecoes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean selecionado = newSelection != null;
            btnAdicionarEntrada.setDisable(!selecionado);
            btnVisualizar.setDisable(!selecionado);
            btnCompactar.setDisable(!selecionado);
            // O botão de exportar só habilita se a coleção for de Livros
            btnExportar.setDisable(!(selecionado && newSelection.getTipo().equals(modelos.Livro.class)));
        });

        // Inicia os botões como desabilitados
        btnAdicionarEntrada.setDisable(true);
        btnVisualizar.setDisable(true);
        btnExportar.setDisable(true);
        btnCompactar.setDisable(true);

        atualizarTabela();
    }

    private void atualizarTabela() {
        ObservableList<Colecao<? extends ArquivoPDF>> colecoes = FXCollections
                .observableArrayList(gerenciador.getTodasColecoes());
        tabelaColecoes.setItems(colecoes);
    }

    @FXML
    private void criarColecao() {
        String nome = campoNomeColecao.getText();
        String autor = campoAutorColecao.getText();
        String limiteStr = campoLimiteColecao.getText();
        String tipoStr = comboTipoColecao.getValue();

        if (nome.isBlank() || autor.isBlank() || limiteStr.isBlank() || tipoStr == null) {
            mostrarAlerta("Erro", "Preencha todos os campos para criar a coleção.");
            return;
        }

        try {
            int limite = Integer.parseInt(limiteStr);
            Class<? extends ArquivoPDF> tipoClasse;
            switch (tipoStr) {
                case "Livro" -> tipoClasse = modelos.Livro.class;
                case "Slide" -> tipoClasse = modelos.Slide.class;
                case "NotaDeAula" -> tipoClasse = modelos.NotaDeAula.class;
                default -> {
                    mostrarAlerta("Erro", "Tipo de coleção inválido.");
                    return;
                }
            }

            boolean sucesso = gerenciador.criarColecao(nome, autor, limite, tipoClasse);
            if (sucesso) {
                campoNomeColecao.clear();
                campoAutorColecao.clear();
                campoLimiteColecao.clear();
                comboTipoColecao.getSelectionModel().clearSelection();
                atualizarTabela();
            } else {
                mostrarAlerta("Erro", "Não foi possível criar a coleção. Verifique se o nome já existe.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "O limite deve ser um número válido.");
        }
    }

    @FXML
    private void removerColecao() {
        Colecao<? extends ArquivoPDF> selecionada = tabelaColecoes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            gerenciador.removerColecao(selecionada.getNome());
            atualizarTabela();
        } else {
            mostrarAlerta("Aviso", "Selecione uma coleção para remover.");
        }
    }

    @FXML
    private void adicionarEntrada() {
        Colecao<? extends ArquivoPDF> selecionada = tabelaColecoes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            // Chama o novo método do navegador para abrir a janela de adição
            Navegador.novaJanelaAdicionar("TelaAdicionarEntrada.fxml", "Adicionar Entrada", selecionada);
            // Atualiza a tabela principal, pois o número de entradas pode ter mudado
            atualizarTabela();
        } else {
            mostrarAlerta("Aviso", "Por favor, selecione uma coleção para adicionar uma entrada.");
        }
    }

    @FXML
    private void visualizarColecao() {
        Colecao<? extends ArquivoPDF> selecionada = tabelaColecoes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            Navegador.novaJanela("TelaVisualizarColecao.fxml", "Visualizar Coleção", selecionada);
            // Atualiza a tabela principal depois que a janela de visualização é fechada
            atualizarTabela();
        } else {
            mostrarAlerta("Aviso", "Por favor, selecione uma coleção para visualizar.");
        }
    }

    @FXML
    private void exportarBibTex() {
        Colecao<? extends ArquivoPDF> selecionada = tabelaColecoes.getSelectionModel().getSelectedItem();
        if (selecionada == null)
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Arquivo BibTeX");
        fileChooser.setInitialFileName(selecionada.getNome() + ".bib");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo BibTeX (*.bib)", "*.bib"));
        File file = fileChooser.showSaveDialog(tabelaColecoes.getScene().getWindow());

        if (file != null) {
            try {
                gerenciador.exportarBibTex(selecionada.getNome(), file.toPath());
                mostrarAlerta("Sucesso", "Coleção exportada com sucesso!");
            } catch (IOException e) {
                mostrarAlerta("Erro", "Erro ao exportar coleção: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void compactarColecao() {
        Colecao<? extends ArquivoPDF> selecionada = tabelaColecoes.getSelectionModel().getSelectedItem();
        if (selecionada == null)
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Arquivo ZIP");
        fileChooser.setInitialFileName(selecionada.getNome() + ".zip");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo Compactado (*.zip)", "*.zip"));
        File file = fileChooser.showSaveDialog(tabelaColecoes.getScene().getWindow());

        if (file != null) {
            try {
                gerenciador.empacotarColecao(selecionada.getNome(), file.toPath());
                mostrarAlerta("Sucesso", "Coleção compactada com sucesso!");
            } catch (IOException e) {
                mostrarAlerta("Erro", "Erro ao compactar coleção: " + e.getMessage());
                e.printStackTrace();
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