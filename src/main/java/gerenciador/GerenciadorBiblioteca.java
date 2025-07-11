package gerenciador;

import excecoes.Excecoes;
import modelos.ArquivoPDF;
import modelos.Livro;
import modelos.NotaDeAula;
import persistencia.Persistencia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciadorBiblioteca {

    private static GerenciadorBiblioteca instancia;

    private Path caminhoBiblioteca;
    private final List<ArquivoPDF> registros;

    // Construtor privado para o padrão Singleton.
    // Carrega o último caminho da biblioteca e todos os registros persistidos.
    private GerenciadorBiblioteca() {
        this.registros = new ArrayList<>();
        try {
            // Carrega todos os registros do arquivo de dados CSV
            this.registros.addAll(Persistencia.carregarRegistros());
            // Carrega o caminho da última biblioteca usada
            String caminhoSalvo = Persistencia.carregarCaminho();
            if (caminhoSalvo != null && !caminhoSalvo.isBlank()) {
                this.caminhoBiblioteca = Paths.get(caminhoSalvo);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados da biblioteca: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retorna a instância única da classe (Singleton).
     */
    public static GerenciadorBiblioteca getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorBiblioteca();
        }
        return instancia;
    }

    /**
     * Adiciona um novo arquivo à biblioteca, movendo o arquivo físico e salvando o
     * registro.
     */
    public void adicionarArquivo(ArquivoPDF arquivo) throws IOException, Excecoes {
        if (caminhoBiblioteca == null) {
            throw new Excecoes("Nenhuma biblioteca selecionada. Crie ou alterne para uma biblioteca primeiro.");
        }
        moverArquivoParaAutor(arquivo);
        registros.add(arquivo);
        Persistencia.salvarRegistro(arquivo);
    }

    /**
     * Retorna a lista de todos os registros de todas as bibliotecas.
     */
    public List<ArquivoPDF> listarRegistros() {
        return new ArrayList<>(registros); // Retorna uma cópia para proteger a lista original
    }

    /**
     * Retorna os registros que pertencem à biblioteca atualmente ativa.
     */
    public List<ArquivoPDF> listarRegistrosBibliotecaAtual() {
        if (caminhoBiblioteca == null) {
            return new ArrayList<>();
        }
        return registros.stream()
                .filter(r -> Paths.get(r.getCaminhoArquivo()).startsWith(caminhoBiblioteca))
                .collect(Collectors.toList());
    }

    /**
     * Busca um único registro pelo título exato (ignorando maiúsculas/minúsculas).
     * Necessário para a TelaBuscarPDFController.
     */
    public ArquivoPDF buscarPorTitulo(String titulo) {
        return listarRegistrosBibliotecaAtual().stream()
                .filter(pdf -> pdf.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca registros que contêm o termo no título, autor ou subtítulo.
     */
    public List<ArquivoPDF> buscarRegistros(String termo) {
        String termoLowerCase = termo.toLowerCase();
        // Usa um Set para evitar duplicatas, caso um registro corresponda a múltiplos
        // critérios
        return listarRegistrosBibliotecaAtual().stream()
                .filter(registro -> {
                    boolean tituloMatch = registro.getTitulo().toLowerCase().contains(termoLowerCase);
                    boolean autorMatch = registro.getAutores().stream()
                            .anyMatch(a -> a.toLowerCase().contains(termoLowerCase));
                    boolean subtituloMatch = false;
                    if (registro instanceof Livro livro) {
                        subtituloMatch = livro.getSubtitulo() != null
                                && livro.getSubtitulo().toLowerCase().contains(termoLowerCase);
                    } else if (registro instanceof NotaDeAula nota) {
                        subtituloMatch = nota.getSubtitulo() != null
                                && nota.getSubtitulo().toLowerCase().contains(termoLowerCase);
                    }
                    return tituloMatch || autorMatch || subtituloMatch;
                })
                .collect(Collectors.toList());
    }

    /**
     * Remove um arquivo pelo título (ignorando maiúsculas/minúsculas).
     * Necessário para a TelaRemoverPDFController.
     */
    public boolean removerPorTitulo(String titulo) {
        Optional<ArquivoPDF> paraRemoverOpt = listarRegistrosBibliotecaAtual().stream()
                .filter(r -> r.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();

        if (paraRemoverOpt.isPresent()) {
            ArquivoPDF paraRemover = paraRemoverOpt.get();
            try {
                removerArquivoFisico(paraRemover);
                registros.remove(paraRemover);
                Persistencia.atualizarRegistros(registros);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Cria um novo diretório de biblioteca e o define como ativo.
     */
    public void criarNovaBiblioteca(String novoCaminho) throws IOException {
        caminhoBiblioteca = Paths.get(novoCaminho);
        if (!Files.exists(caminhoBiblioteca)) {
            Files.createDirectories(caminhoBiblioteca);
        }
        Persistencia.salvarCaminho(novoCaminho);
    }

    /**
     * Altera a biblioteca ativa para um outro diretório existente.
     */
    public void alternarBiblioteca(String novoCaminho) throws IOException {
        caminhoBiblioteca = Paths.get(novoCaminho);
        Persistencia.salvarCaminho(novoCaminho);
    }

    /**
     * Deleta o diretório da biblioteca atual e todos os seus registros.
     */
    public boolean deletarBiblioteca() throws IOException {
        if (caminhoBiblioteca != null && Files.exists(caminhoBiblioteca)) {
            // Deleta arquivos e diretórios recursivamente
            try (var stream = Files.walk(caminhoBiblioteca)) {
                stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        System.err.println("Falha ao deletar " + path + ": " + e.getMessage());
                    }
                });
            }
            // Remove os registros associados da lista em memória e da persistência
            Persistencia.removerRegistrosPorDiretorio(caminhoBiblioteca.toString());
            registros.removeIf(r -> Paths.get(r.getCaminhoArquivo()).startsWith(caminhoBiblioteca));

            // Limpa o caminho da biblioteca atual
            caminhoBiblioteca = null;
            Persistencia.salvarCaminho(null);
            return true;
        }
        return false;
    }

    public Path getCaminhoBiblioteca() {
        return caminhoBiblioteca;
    }

    // Métodos privados auxiliares

    private void moverArquivoParaAutor(ArquivoPDF arquivo) throws IOException, Excecoes {
        if (arquivo.getAutores() == null || arquivo.getAutores().isEmpty()) {
            throw new Excecoes("O arquivo precisa ter pelo menos um autor.");
        }
        String autorPrincipal = arquivo.getAutores().get(0).trim();
        Path diretorioAutor = caminhoBiblioteca.resolve(autorPrincipal);

        if (!Files.exists(diretorioAutor)) {
            Files.createDirectories(diretorioAutor);
        }

        Path origem = Paths.get(arquivo.getCaminhoArquivo());
        if (!Files.exists(origem)) {
            throw new Excecoes("Arquivo PDF de origem não encontrado em: " + origem);
        }

        Path destino = diretorioAutor.resolve(origem.getFileName());
        Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
        arquivo.setCaminhoArquivo(destino.toString()); // Atualiza o caminho no objeto
    }

    private void removerArquivoFisico(ArquivoPDF registro) throws IOException {
        Path caminho = Paths.get(registro.getCaminhoArquivo());
        Files.deleteIfExists(caminho);
    }
}