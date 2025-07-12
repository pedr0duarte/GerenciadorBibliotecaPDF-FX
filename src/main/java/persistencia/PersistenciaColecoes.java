package persistencia;

import gerenciador.GerenciadorBiblioteca;
import modelos.ArquivoPDF;
import modelos.Colecao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistenciaColecoes {
    private static final String ARQUIVO_COLECOES = "colecoes.csv";
    private static final String DELIMITADOR = ";";

    /**
     * Salva a lista de coleções em um arquivo CSV.
     * Formato: nome;autor;limite;tipo;caminho_pdf_1;caminho_pdf_2;...
     */
    public static void salvarColecoes(List<Colecao<? extends ArquivoPDF>> colecoes) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(ARQUIVO_COLECOES))) {
            for (Colecao<? extends ArquivoPDF> c : colecoes) {
                List<String> caminhosPdf = new ArrayList<>();
                for (ArquivoPDF pdf : c.getEntradas()) {
                    caminhosPdf.add(pdf.getCaminhoArquivo());
                }

                String linha = String.join(DELIMITADOR,
                        c.getNome(),
                        c.getAutor(),
                        String.valueOf(c.getLimite()),
                        c.getTipo().getName(), // Salva o nome completo da classe (ex: modelos.Livro)
                        String.join(DELIMITADOR, caminhosPdf));
                writer.write(linha);
                writer.newLine();
            }
        }
    }

    /**
     * Carrega as coleções a partir do arquivo CSV.
     */
    public static Map<String, Colecao<? extends ArquivoPDF>> carregarColecoes() throws IOException {
        Map<String, Colecao<? extends ArquivoPDF>> colecoes = new HashMap<>();
        Path path = Path.of(ARQUIVO_COLECOES);

        if (!Files.exists(path)) {
            return colecoes;
        }

        // Mapeia todos os PDFs da biblioteca por seu caminho para fácil acesso
        List<ArquivoPDF> todosPdfs = GerenciadorBiblioteca.getInstancia().listarRegistros();
        Map<String, ArquivoPDF> mapaPdfs = new HashMap<>();
        for (ArquivoPDF pdf : todosPdfs) {
            mapaPdfs.put(pdf.getCaminhoArquivo(), pdf);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Usando trim().isEmpty() para compatibilidade com versões antigas de Java
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(DELIMITADOR);
                if (partes.length < 4) {
                    continue;
                }

                String nome = partes[0];
                String autor = partes[1];
                int limite = Integer.parseInt(partes[2]);
                String nomeClasse = partes[3];

                try {
                    Class<?> classeCarregada = Class.forName(nomeClasse);
                    // Adicionamos a anotação aqui para suprimir os avisos de forma controlada
                    @SuppressWarnings("unchecked")
                    Class<? extends ArquivoPDF> tipoClasse = (Class<? extends ArquivoPDF>) classeCarregada;

                    // Criamos a coleção já com o tipo genérico correto
                    Colecao<? extends ArquivoPDF> colecao = new Colecao<>(nome, autor, limite, tipoClasse);

                    // Adiciona os PDFs que pertencem a esta coleção
                    if (partes.length > 4) {
                        for (int i = 4; i < partes.length; i++) {
                            String caminhoPdf = partes[i];
                            ArquivoPDF pdf = mapaPdfs.get(caminhoPdf);
                            if (pdf != null && tipoClasse.isInstance(pdf)) {
                                // Chamada ao método genérico auxiliar para adicionar com segurança
                                adicionarEntradaGenerica(colecao, pdf);
                            }
                        }
                    }
                    colecoes.put(nome, colecao);

                } catch (ClassNotFoundException e) {
                    System.err.println("Erro: Classe não encontrada ao carregar coleção -> " + nomeClasse);
                }
            }
        }
        return colecoes;
    }

    /**
     * Método auxiliar genérico para adicionar uma entrada a uma coleção de forma
     * segura,
     * lidando com os tipos genéricos de forma correta.
     */
    @SuppressWarnings("unchecked")
    private static <T extends ArquivoPDF> void adicionarEntradaGenerica(Colecao<T> colecao, ArquivoPDF entrada) {
        colecao.adicionarEntrada((T) entrada);
    }
}