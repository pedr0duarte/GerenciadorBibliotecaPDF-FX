package modelos;

import java.util.List;

public class Livro extends ArquivoPDF {
    private String subtitulo;
    private String areaConhecimento;
    private int anoPublicacao;

    public Livro(List<String> autores, String titulo, String subtitulo, String areaConhecimento, int anoPublicacao,
            String caminhoArquivo) {
        super(autores, titulo, caminhoArquivo);
        this.subtitulo = subtitulo;
        this.areaConhecimento = areaConhecimento;
        this.anoPublicacao = anoPublicacao;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public String getAreaConhecimento() {
        return areaConhecimento;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public void setAreaConhecimento(String areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    @Override
    public String getTipo() {
        return "Livro";
    }
}