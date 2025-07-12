package gerenciador;

import modelos.*;
import persistencia.PersistenciaColecoes; // Importe a classe de persistência

import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GerenciadorColecoes {

    private static GerenciadorColecoes instancia;
    // O mapa que armazena as coleções. Não é final para que possamos carregá-lo do
    // disco.
    private Map<String, Colecao<? extends ArquivoPDF>> colecoes;

    /**
     * Construtor privado para o padrão Singleton.
     * Tenta carregar as coleções persistidas do disco.
     */
    private GerenciadorColecoes() {
        try {
            this.colecoes = PersistenciaColecoes.carregarColecoes();
        } catch (IOException e) {
            this.colecoes = new HashMap<>(); // Em caso de erro, inicia com um mapa vazio
            System.err.println("Erro ao carregar dados das coleções: " + e.getMessage());
            e.printStackTrace();
        }
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
     * Salva o estado atual de todas as coleções no arquivo CSV.
     * Este é um método privado chamado internamente sempre que há uma alteração.
     */
    private void salvarDados() {
        try {
            PersistenciaColecoes.salvarColecoes(new ArrayList<>(colecoes.values()));
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados das coleções: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cria uma nova coleção e a salva no disco.
     */
    public <T extends ArquivoPDF> boolean criarColecao(String nome, String autor, int limite, Class<T> tipo) {
        if (colecoes.containsKey(nome)) {
            System.out.println("Erro: Já existe uma coleção com o nome \"" + nome + "\".");
            return false;
        }
        Colecao<T> nova = new Colecao<>(nome, autor, limite, tipo);
        colecoes.put(nome, nova);
        salvarDados(); // Salva o estado após a criação
        return true;
    }

    /**
     * Adiciona uma entrada a uma coleção e salva a alteração.
     */
    public boolean adicionarEntrada(String nomeColecao, ArquivoPDF entrada) {
        Colecao<?> colecao = colecoes.get(nomeColecao);
        if (colecao == null) {
            System.out.println("Erro: Coleção não encontrada.");
            return false;
        }

        if (colecao.getTipo().isInstance(entrada)) {
            boolean sucesso = adicionarEntradaGenerica(colecao, entrada);
            if (sucesso) {
                salvarDados(); // Salva o estado após adicionar
            } else {
                System.out.println("Limite atingido ou autor não confere. Entrada não adicionada.");
            }
            return sucesso;
        } else {
            System.out.println("Erro: O tipo do arquivo é incompatível com a coleção.");
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends ArquivoPDF> boolean adicionarEntradaGenerica(Colecao<?> colecao, ArquivoPDF entrada) {
        Colecao<T> colecaoTipada = (Colecao<T>) colecao;
        return colecaoTipada.adicionarEntrada((T) entrada);
    }

    /**
     * Remove uma entrada de uma coleção e salva a alteração.
     */
    public boolean removerEntrada(String nomeColecao, ArquivoPDF entrada) {
        Colecao<?> colecao = colecoes.get(nomeColecao);
        if (colecao != null && colecao.getTipo().isInstance(entrada)) {
            boolean removido = removerEntradaGenerica(colecao, entrada);
            if (removido) {
                if (colecao.estaVazia()) {
                    colecoes.remove(nomeColecao);
                    System.out.println("Coleção \"" + nomeColecao + "\" ficou vazia e foi removida.");
                }
                salvarDados(); // Salva o estado após a remoção
            }
            return removido;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T extends ArquivoPDF> boolean removerEntradaGenerica(Colecao<?> c, ArquivoPDF entrada) {
        Colecao<T> typed = (Colecao<T>) c;
        return typed.removerEntrada((T) entrada);
    }

    /**
     * Remove uma coleção inteira e salva a alteração.
     */
    public void removerColecao(String nome) {
        if (colecoes.remove(nome) != null) {
            salvarDados(); // Salva o estado após remover a coleção
            System.out.println("Coleção \"" + nome + "\" removida com sucesso.");
        } else {
            System.out.println("Erro: Coleção não encontrada.");
        }
    }

    public List<Colecao<? extends ArquivoPDF>> listarPorAutor(String autor) {
        return new ArrayList<>(colecoes.values()).stream()
                .filter(c -> c.getAutor().equalsIgnoreCase(autor))
                .toList();
    }

    public List<Colecao<? extends ArquivoPDF>> listarPorTipo(Class<?> tipo) {
        return new ArrayList<>(colecoes.values()).stream()
                .filter(c -> c.getTipo().equals(tipo))
                .toList();
    }

    public void exportarBibTex(String nomeColecao, Path destino) throws IOException {
        Optional<Colecao<?>> colOpt = Optional.ofNullable(colecoes.get(nomeColecao));

        if (colOpt.isPresent() && colOpt.get().getTipo().equals(Livro.class)) {
            Colecao<?> col = colOpt.get();
            try (BufferedWriter bw = Files.newBufferedWriter(destino)) {
                for (ArquivoPDF a : col.getEntradas()) {
                    Livro l = (Livro) a;
                    bw.write("@book{" + l.getTitulo().replace(" ", "_") + ",\n");
                    bw.write("  author = {" + String.join(" and ", l.getAutores()) + "},\n");
                    bw.write("  title = {" + l.getTitulo() + "},\n");
                    bw.write("  year = {" + l.getAnoPublicacao() + "},\n");
                    bw.write("  publisher = {" + l.getAreaConhecimento() + "}\n");
                    bw.write("}\n\n");
                }
            }
        } else {
            throw new IOException("Coleção não encontrada ou não é do tipo Livro.");
        }
    }

    public void empacotarColecao(String nomeColecao, Path zipDestino) throws IOException {
        Optional<Colecao<?>> colOpt = Optional.ofNullable(colecoes.get(nomeColecao));
        if (colOpt.isPresent()) {
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipDestino))) {
                for (ArquivoPDF pdf : colOpt.get().getEntradas()) {
                    Path path = Path.of(pdf.getCaminhoArquivo());
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

    public List<String> getNomesColecoes() {
        return new ArrayList<>(colecoes.keySet());
    }

    public List<Colecao<? extends ArquivoPDF>> getTodasColecoes() {
        return new ArrayList<>(colecoes.values());
    }
}