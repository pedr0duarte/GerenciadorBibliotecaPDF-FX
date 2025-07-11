package modelos;

import java.util.List;

public class NotaDeAula extends ArquivoPDF {
    private String subtitulo;
    private String disciplina;

    public NotaDeAula(List<String> autores, String titulo, String subtitulo, String disciplina, String caminhoArquivo) {
        super(autores, titulo, caminhoArquivo);
        this.subtitulo = subtitulo;
        this.disciplina = disciplina;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public String getTipo() {
        return "NotaDeAula";
    }
}