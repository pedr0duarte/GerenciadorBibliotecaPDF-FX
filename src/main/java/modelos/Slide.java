package modelos;

import java.util.List;

public class Slide extends ArquivoPDF {
    private String disciplina;

    public Slide(List<String> autores, String titulo, String disciplina, String caminhoArquivo) {
        super(autores, titulo, caminhoArquivo);
        this.disciplina = disciplina;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public String getTipo() {
        return "Slide";
    }
}