package gerenciador;

import modelos.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class GerenciadorColecoes {
    private List<Colecao<? extends ArquivoPDF>> colecoes;

    private static GerenciadorColecoes instancia;

    private Map<String, Colecao> colecoes = new HashMap<>();

    private GerenciadorColecoes() {
        // Pode carregar dados persistidos aqui se desejar
    }

    public static GerenciadorColecoes getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorColecoes();
        }
        return instancia;
    }

    public GerenciadorColecoes() {
        this.colecoes = new ArrayList<>();
    }

    public <T extends ArquivoPDF> boolean criarColecao(String nome, String autor, int limite, Class<T> tipo,
            List<T> entradas) {
        Colecao<T> nova = new Colecao<>(nome, autor, limite, tipo);
        for (T e : entradas) {
            if (!nova.adicionarEntrada(e))
                return false;
        }
        return colecoes.add(nova);
    }

    public boolean adicionarEntrada(String nomeColecao, ArquivoPDF entrada) {
        for (Colecao<?> c : colecoes) {
            if (c.getNome().equals(nomeColecao) && c.getTipo().isInstance(entrada)) {
                boolean ok = adicionarEntradaGenerica(c, entrada);
                if (!ok) {
                    System.out.println("Limite atingido ou autor não confere. Entrada não adicionada.");
                }
                return ok;
            }
        }
        System.out.println("Coleção não encontrada.");
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T extends ArquivoPDF> boolean adicionarEntradaGenerica(Colecao<?> c, ArquivoPDF entrada) {
        Colecao<T> typed = (Colecao<T>) c;
        return typed.adicionarEntrada((T) entrada);
    }

    public boolean removerEntrada(String nomeColecao, ArquivoPDF entrada) {
        Iterator<Colecao<? extends ArquivoPDF>> it = colecoes.iterator();
        while (it.hasNext()) {
            Colecao<?> c = it.next();
            if (c.getNome().equals(nomeColecao) && c.getTipo().isInstance(entrada)) {
                return removerEntradaGenerica(c, entrada, it);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T extends ArquivoPDF> boolean removerEntradaGenerica(Colecao<?> c, ArquivoPDF entrada, Iterator<?> it) {
        Colecao<T> typed = (Colecao<T>) c;
        boolean ok = typed.removerEntrada((T) entrada);
        if (ok && typed.estaVazia()) {
            it.remove();
        }
        return ok;
    }

    public List<Colecao<? extends ArquivoPDF>> listarPorAutor(String autor) {
        return colecoes.stream().filter(c -> c.getAutor().equalsIgnoreCase(autor)).toList();
    }

    public List<Colecao<? extends ArquivoPDF>> listarPorTipo(Class<?> tipo) {
        return colecoes.stream().filter(c -> c.getTipo().equals(tipo)).toList();
    }

    public void exportarBibTex(String nomeColecao, Path destino) throws IOException {
        Optional<Colecao<? extends ArquivoPDF>> col = colecoes.stream()
                .filter(c -> c.getNome().equals(nomeColecao))
                .findFirst();

        if (col.isPresent() && col.get().getTipo().equals(Livro.class)) {
            try (BufferedWriter bw = Files.newBufferedWriter(destino)) {
                for (ArquivoPDF a : col.get().getEntradas()) {
                    Livro l = (Livro) a;
                    bw.write("@book{" + l.getTitulo().replace(" ", "_") + ",\n");
                    bw.write("  author = {" + String.join(" and ", l.getAutores()) + "},\n");
                    bw.write("  title = {" + l.getTitulo() + "},\n");
                    bw.write("  year = {" + l.getAnoPublicacao() + "},\n");
                    bw.write("  publisher = {" + l.getAreaConhecimento() + "}\n");
                    bw.write("}\n\n");
                }
            }
        }
    }

    public void empacotarColecao(String nomeColecao, Path zipDestino) throws IOException {
        Optional<Colecao<? extends ArquivoPDF>> col = colecoes.stream().filter(c -> c.getNome().equals(nomeColecao))
                .findFirst();
        if (col.isPresent()) {
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipDestino))) {
                for (ArquivoPDF pdf : col.get().getEntradas()) {
                    Path path = Path.of(pdf.getCaminhoArquivo());
                    ZipEntry entry = new ZipEntry(path.getFileName().toString());
                    zos.putNextEntry(entry);
                    Files.copy(path, zos);
                    zos.closeEntry();
                }
            }
        }
    }

    public List<Colecao<? extends ArquivoPDF>> getTodasColecoes() {
        return colecoes;
    }
}
