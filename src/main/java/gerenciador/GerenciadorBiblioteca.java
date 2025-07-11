package gerenciador;

import modelos.*;
import persistencia.Persistencia;
import excecoes.Excecoes;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class GerenciadorBiblioteca {
    private Path caminhoBiblioteca;
    private List<ArquivoPDF> registros;

    private static GerenciadorBiblioteca instancia;

    private List<ArquivoPDF> arquivos = new ArrayList<>();

    private GerenciadorBiblioteca() {
    }

    public static GerenciadorBiblioteca getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorBiblioteca();
        }
        return instancia;
    }

    public GerenciadorBiblioteca(String caminhoBiblioteca) throws IOException {
        this.caminhoBiblioteca = Path.of(caminhoBiblioteca);
        this.registros = Persistencia.carregarRegistros();
    }

    public void adicionarArquivo(ArquivoPDF arquivo) throws IOException, Excecoes {
        moverArquivoParaAutor(arquivo);
        registros.add(arquivo);
        Persistencia.salvarRegistro(arquivo);
    }

    public List<ArquivoPDF> listarRegistros() {
        return registros;
    }

    public List<ArquivoPDF> listarRegistrosBibliotecaAtual() {
        return registros.stream()
                .filter(r -> Paths.get(r.getCaminhoArquivo()).startsWith(caminhoBiblioteca))
                .collect(Collectors.toList());
    }

    public List<ArquivoPDF> buscarRegistros(String termo) {
        termo = termo.toLowerCase();
        List<ArquivoPDF> encontrados = new ArrayList<>();
        for (ArquivoPDF registro : registros) {
            if (registro.getTitulo().toLowerCase().contains(termo)
                    || registro.getAutores().toString().toLowerCase().contains(termo)) {
                encontrados.add(registro);
            }
            if (registro instanceof Livro livro && livro.getSubtitulo().toLowerCase().contains(termo)) {
                encontrados.add(livro);
            }
            if (registro instanceof NotaDeAula nota && nota.getSubtitulo().toLowerCase().contains(termo)) {
                encontrados.add(nota);
            }
        }
        return encontrados;
    }

    public List<ArquivoPDF> buscarRegistrosBibliotecaAtual(String termo) {
        return buscarRegistros(termo).stream()
                .filter(r -> Paths.get(r.getCaminhoArquivo()).startsWith(caminhoBiblioteca))
                .collect(Collectors.toList());
    }

    public boolean editarRegistro(int indice, ArquivoPDF novoArquivo) throws IOException, Excecoes {
        List<ArquivoPDF> registrosAtual = listarRegistrosBibliotecaAtual();
        if (indice >= 0 && indice < registrosAtual.size()) {
            ArquivoPDF antigo = registrosAtual.get(indice);
            moverArquivoParaAutor(novoArquivo);
            removerArquivoFisico(antigo);
            registros.remove(antigo);
            registros.add(novoArquivo);
            Persistencia.atualizarRegistros(registros);
            return true;
        }
        return false;
    }

    public boolean deletarArquivo(int indice) throws IOException {
        List<ArquivoPDF> registrosAtual = listarRegistrosBibliotecaAtual();
        if (indice >= 0 && indice < registrosAtual.size()) {
            ArquivoPDF registro = registrosAtual.get(indice);
            removerArquivoFisico(registro);
            registros.remove(registro);
            Persistencia.atualizarRegistros(registros);
            return true;
        }
        return false;
    }

    public void criarNovaBiblioteca(String novoCaminho) throws IOException {
        caminhoBiblioteca = Path.of(novoCaminho);
        if (!Files.exists(caminhoBiblioteca)) {
            Files.createDirectories(caminhoBiblioteca);
        }
    }

    public void alternarBiblioteca(String novoCaminho) {
        caminhoBiblioteca = Path.of(novoCaminho);
    }

    public boolean deletarBiblioteca() throws IOException {
        if (Files.exists(caminhoBiblioteca)) {
            try (var stream = Files.walk(caminhoBiblioteca)) {
                List<Path> paths = stream.sorted(Comparator.reverseOrder()).toList();
                for (Path p : paths) {
                    Files.deleteIfExists(p);
                }
            }
            registros.removeIf(r -> Paths.get(r.getCaminhoArquivo()).startsWith(caminhoBiblioteca));
            Persistencia.atualizarRegistros(registros);
            return true;
        }
        return false;
    }

    public Path getCaminhoBiblioteca() {
        return caminhoBiblioteca;
    }

    private void moverArquivoParaAutor(ArquivoPDF arquivo) throws IOException, Excecoes {
        String autorPrincipal = arquivo.getAutores().get(0).trim();
        Path diretorioAutor = caminhoBiblioteca.resolve(autorPrincipal);

        if (!Files.exists(diretorioAutor)) {
            Files.createDirectories(diretorioAutor);
        }

        Path origem = Paths.get(arquivo.getCaminhoArquivo());
        if (!Files.exists(origem)) {
            throw new Excecoes("Arquivo PDF não encontrado: " + origem);
        }

        Path destino = diretorioAutor.resolve(origem.getFileName());
        Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
        arquivo.setCaminhoArquivo(destino.toString());
    }

    private void removerArquivoFisico(ArquivoPDF registro) throws IOException {
        Path caminho = Paths.get(registro.getCaminhoArquivo());
        Files.deleteIfExists(caminho);
    }
}