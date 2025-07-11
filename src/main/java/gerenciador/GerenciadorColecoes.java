package gerenciador;

import modelos.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.*;

public class GerenciadorColecoes {

    private static GerenciadorColecoes instancia;
    private final Map<String, Colecao<? extends ArquivoPDF>> colecoes;

    // Construtor privado para garantir o padrão Singleton
    private GerenciadorColecoes() {
        this.colecoes = new HashMap<>();
        // No futuro, dados persistidos de coleções podem ser carregados aqui.
    }

    /**
     * Retorna a instância única da classe (Singleton).
     */
    public static GerenciadorColecoes getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorColecoes();
        }
        return instancia;
    }

    /**
     * Cria uma nova coleção.
     * Este método genérico permite a criação de coleções com tipos específicos de
     * ArquivoPDF.
     *
     * @param nome   Nome da coleção.
     * @param autor  Autor principal da coleção.
     * @param limite Número máximo de itens na coleção.
     * @param tipo   A classe do tipo de arquivo PDF que a coleção irá aceitar
     *               (Livro.class, Slide.class, etc.).
     * @param <T>    O tipo de ArquivoPDF.
     * @return Verdadeiro se a coleção foi criada com sucesso, falso caso contrário.
     */
    public <T extends ArquivoPDF> boolean criarColecao(String nome, String autor, int limite, Class<T> tipo) {
        if (colecoes.containsKey(nome)) {
            System.out.println("Erro: Já existe uma coleção com o nome \"" + nome + "\".");
            return false;
        }
        Colecao<T> nova = new Colecao<>(nome, autor, limite, tipo);
        colecoes.put(nome, nova);
        return true;
    }

    /**
     * Adiciona uma entrada a uma coleção existente.
     *
     * @param nomeColecao Nome da coleção.
     * @param entrada     O ArquivoPDF a ser adicionado.
     * @return Verdadeiro se a entrada foi adicionada, falso caso contrário.
     */
    public boolean adicionarEntrada(String nomeColecao, ArquivoPDF entrada) {
        Colecao<?> colecao = colecoes.get(nomeColecao);
        if (colecao == null) {
            System.out.println("Erro: Coleção não encontrada.");
            return false;
        }

        // Verifica se o tipo da entrada é compatível com o tipo da coleção
        if (colecao.getTipo().isInstance(entrada)) {
            boolean sucesso = adicionarEntradaGenerica(colecao, entrada);
            if (!sucesso) {
                System.out.println("Limite atingido ou autor não confere. Entrada não adicionada.");
            }
            return sucesso;
        } else {
            System.out.println("Erro: O tipo do arquivo é incompatível com a coleção.");
            return false;
        }
    }

    // Método auxiliar genérico para adicionar a entrada
    @SuppressWarnings("unchecked")
    private <T extends ArquivoPDF> boolean adicionarEntradaGenerica(Colecao<?> colecao, ArquivoPDF entrada) {
        Colecao<T> colecaoTipada = (Colecao<T>) colecao;
        return colecaoTipada.adicionarEntrada((T) entrada);
    }

    /**
     * Remove uma entrada de uma coleção. Se a coleção ficar vazia, ela é removida.
     *
     * @param nomeColecao Nome da coleção.
     * @param entrada     O ArquivoPDF a ser removido.
     * @return Verdadeiro se a entrada foi removida, falso caso contrário.
     */
    public boolean removerEntrada(String nomeColecao, ArquivoPDF entrada) {
        Colecao<?> colecao = colecoes.get(nomeColecao);
        if (colecao != null && colecao.getTipo().isInstance(entrada)) {
            boolean removido = removerEntradaGenerica(colecao, entrada);
            if (removido && colecao.estaVazia()) {
                colecoes.remove(nomeColecao);
                System.out.println("Coleção \"" + nomeColecao + "\" ficou vazia e foi removida.");
            }
            return removido;
        }
        return false;
    }

    // Método auxiliar genérico para remover a entrada
    @SuppressWarnings("unchecked")
    private <T extends ArquivoPDF> boolean removerEntradaGenerica(Colecao<?> c, ArquivoPDF entrada) {
        Colecao<T> typed = (Colecao<T>) c;
        return typed.removerEntrada((T) entrada);
    }

    /**
     * Remove uma coleção inteira pelo nome.
     *
     * @param nome O nome da coleção a ser removida.
     */
    public void removerColecao(String nome) {
        if (colecoes.remove(nome) != null) {
            System.out.println("Coleção \"" + nome + "\" removida com sucesso.");
        } else {
            System.out.println("Erro: Coleção não encontrada.");
        }
    }

    /**
     * Lista coleções filtrando pelo nome do autor.
     */
    public List<Colecao<? extends ArquivoPDF>> listarPorAutor(String autor) {
        return colecoes.values().stream()
                .filter(c -> c.getAutor().equalsIgnoreCase(autor))
                .collect(Collectors.toList());
    }

    /**
     * Lista coleções filtrando pelo tipo de arquivo.
     */
    public List<Colecao<? extends ArquivoPDF>> listarPorTipo(Class<?> tipo) {
        return colecoes.values().stream()
                .filter(c -> c.getTipo().equals(tipo))
                .collect(Collectors.toList());
    }

    /**
     * Exporta uma coleção do tipo Livro para o formato BibTeX.
     */
    public void exportarBibTex(String nomeColecao, Path destino) throws IOException {
        Colecao<?> colecao = colecoes.get(nomeColecao);

        if (colecao != null && colecao.getTipo().equals(Livro.class)) {
            try (BufferedWriter bw = Files.newBufferedWriter(destino)) {
                for (ArquivoPDF a : colecao.getEntradas()) {
                    Livro l = (Livro) a;
                    bw.write("@book{" + l.getTitulo().replace(" ", "_") + ",\n");
                    bw.write("  author = {" + String.join(" and ", l.getAutores()) + "},\n");
                    bw.write("  title = {" + l.getTitulo() + "},\n");
                    bw.write("  year = {" + l.getAnoPublicacao() + "},\n");
                    // O campo "publisher" no BibTeX é para a editora. Usando "areaConhecimento"
                    // aqui.
                    bw.write("  publisher = {" + l.getAreaConhecimento() + "}\n");
                    bw.write("}\n\n");
                }
            }
        } else {
            throw new IOException("Coleção não encontrada ou não é uma coleção de livros.");
        }
    }

    /**
     * Compacta todos os arquivos PDF de uma coleção em um arquivo .zip.
     */
    public void empacotarColecao(String nomeColecao, Path zipDestino) throws IOException {
        Colecao<?> colecao = colecoes.get(nomeColecao);
        if (colecao != null) {
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipDestino))) {
                for (ArquivoPDF pdf : colecao.getEntradas()) {
                    Path path = Paths.get(pdf.getCaminhoArquivo());
                    if (Files.exists(path)) {
                        ZipEntry entry = new ZipEntry(path.getFileName().toString());
                        zos.putNextEntry(entry);
                        Files.copy(path, zos);
                        zos.closeEntry();
                    }
                }
            }
        } else {
            throw new IOException("Coleção não encontrada.");
        }
    }

    /**
     * Retorna uma lista com os nomes de todas as coleções.
     * Usado pela interface gráfica para popular a lista de coleções.
     */
    public List<String> getNomesColecoes() {
        return new ArrayList<>(colecoes.keySet());
    }

    /**
     * Retorna uma lista com todas as coleções.
     */
    public List<Colecao<? extends ArquivoPDF>> getTodasColecoes() {
        return new ArrayList<>(colecoes.values());
    }
}