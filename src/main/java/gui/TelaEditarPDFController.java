package gui;

import gerenciador.GerenciadorBiblioteca;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelos.*;

import java.util.Arrays;
import java.util.List;

public class TelaEditarPDFController {

    // Campos Comuns
    @FXML
    private TextField campoTitulo;
    @FXML
    private TextField campoAutores;

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

    private ArquivoPDF pdfOriginal;
    private final GerenciadorBiblioteca gerenciador = GerenciadorBiblioteca.getInstancia();
    private boolean salvo = false;

    /**
     * Preenche a tela com os dados do PDF a ser editado.
     */
    public void setPdfParaEditar(ArquivoPDF pdf) {
        this.pdfOriginal = pdf;

        campoTitulo.setText(pdf.getTitulo());
        campoAutores.setText(String.join(", ", pdf.getAutores()));

        // Esconde todos os campos específicos inicialmente
        labelSubtitulo.setVisible(false);
        campoSubtitulo.setVisible(false);
        labelDisciplina.setVisible(false);
        campoDisciplina.setVisible(false);
        labelArea.setVisible(false);
        campoArea.setVisible(false);
        labelAno.setVisible(false);
        campoAno.setVisible(false);

        // Mostra os campos relevantes para o tipo de PDF
        if (pdf instanceof Livro livro) {
            labelSubtitulo.setVisible(true);
            campoSubtitulo.setVisible(true);
            campoSubtitulo.setText(livro.getSubtitulo());
            labelArea.setVisible(true);
            campoArea.setVisible(true);
            campoArea.setText(livro.getAreaConhecimento());
            labelAno.setVisible(true);
            campoAno.setVisible(true);
            campoAno.setText(String.valueOf(livro.getAnoPublicacao()));
        } else if (pdf instanceof NotaDeAula nota) {
            labelSubtitulo.setVisible(true);
            campoSubtitulo.setVisible(true);
            campoSubtitulo.setText(nota.getSubtitulo());
            labelDisciplina.setVisible(true);
            campoDisciplina.setVisible(true);
            campoDisciplina.setText(nota.getDisciplina());
        } else if (pdf instanceof Slide slide) {
            labelDisciplina.setVisible(true);
            campoDisciplina.setVisible(true);
            campoDisciplina.setText(slide.getDisciplina());
        }
    }

    @FXML
    private void salvar() {
        List<String> autores = Arrays.asList(campoAutores.getText().split(",\\s*"));
        ArquivoPDF pdfAtualizado = null;

        try {
            // Cria um novo objeto PDF com os dados atualizados
            if (pdfOriginal instanceof Livro) {
                pdfAtualizado = new Livro(autores, campoTitulo.getText(), campoSubtitulo.getText(), campoArea.getText(),
                        Integer.parseInt(campoAno.getText()), pdfOriginal.getCaminhoArquivo());
            } else if (pdfOriginal instanceof NotaDeAula) {
                pdfAtualizado = new NotaDeAula(autores, campoTitulo.getText(), campoSubtitulo.getText(),
                        campoDisciplina.getText(), pdfOriginal.getCaminhoArquivo());
            } else if (pdfOriginal instanceof Slide) {
                pdfAtualizado = new Slide(autores, campoTitulo.getText(), campoDisciplina.getText(),
                        pdfOriginal.getCaminhoArquivo());
            }

            if (pdfAtualizado != null) {
                gerenciador.editarRegistro(pdfOriginal, pdfAtualizado);
                this.salvo = true;
                fechar();
            }
        } catch (Exception e) {
            mostrarAlerta("Erro de Validação",
                    "Verifique se todos os campos estão preenchidos corretamente. O ano deve ser um número.");
        }
    }

    public boolean foiSalvo() {
        return this.salvo;
    }

    @FXML
    private void cancelar() {
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) campoTitulo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}