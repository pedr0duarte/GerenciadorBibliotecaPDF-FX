package modelos;

import java.util.List;

public abstract class ArquivoPDF {
    protected List<String> autores;
    protected String titulo;
    protected String caminhoArquivo;

    public ArquivoPDF(List<String> autores, String titulo, String caminhoArquivo) {
        this.autores = autores;
        this.titulo = titulo;
        this.caminhoArquivo = caminhoArquivo;
    }

    public List<String> getAutores() {
        return autores;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setAutores(List<String> autores) {
        this.autores = autores;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public abstract String getTipo();

    @Override
    public String toString() {
        return getTipo() + " - Titulo: " + titulo + ", Autores: " + autores;
    }
}