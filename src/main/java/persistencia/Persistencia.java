package persistencia;

import modelos.ArquivoPDF;
import modelos.Livro;
import modelos.NotaDeAula;
import modelos.Slide;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Persistencia {
    private static final String PATH_FILE = "biblioteca_path.txt"; // SALVA OS DIRETÓRIOS DAS BIBLIOTECAS
    private static final String DADOS_FILE = "biblioteca_dados.csv"; // SALVA TODOS OS REGISTROS

    public static void salvarCaminho(String caminho) throws IOException {
        if (caminho == null) {
            Files.write(Path.of(PATH_FILE), new byte[0]);
        } else {
            Set<String> caminhos = new LinkedHashSet<>(carregarTodosCaminhos());
            caminhos.remove(caminho);
            caminhos.add(caminho);
            Files.write(Path.of(PATH_FILE), String.join(System.lineSeparator(), caminhos).getBytes());
        }
    }

    public static String carregarCaminho() throws IOException {
        List<String> linhas = carregarTodosCaminhos();
        return linhas.isEmpty() ? null : linhas.get(linhas.size() - 1);
    }

    public static List<String> carregarTodosCaminhos() throws IOException {
        Path path = Path.of(PATH_FILE);
        if (Files.exists(path)) {
            return Files.readAllLines(path).stream().filter(l -> !l.isBlank()).toList();
        }
        return new ArrayList<>();
    }

    public static void removerCaminho(String caminhoRemovido) throws IOException {
        List<String> caminhos = new ArrayList<>(carregarTodosCaminhos());
        caminhos.removeIf(c -> c.equalsIgnoreCase(caminhoRemovido));
        Files.write(Path.of(PATH_FILE), String.join(System.lineSeparator(), caminhos).getBytes());
    }

    public static void salvarRegistro(ArquivoPDF arquivo) throws IOException {
        String linha = gerarLinhaCSV(arquivo);
        Files.writeString(Path.of(DADOS_FILE), linha + System.lineSeparator(), StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    public static List<ArquivoPDF> carregarRegistros() throws IOException {
        List<ArquivoPDF> registros = new ArrayList<>();
        Path path = Path.of(DADOS_FILE);

        if (!Files.exists(path)) {
            return registros;
        }

        List<String> linhas = Files.readAllLines(path);
        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                ArquivoPDF pdf = converterLinhaParaObjeto(linha);
                if (pdf != null)
                    registros.add(pdf);
            }
        }
        return registros;
    }

    public static void atualizarRegistros(List<ArquivoPDF> registros) throws IOException {
        List<String> linhas = registros.stream()
                .map(Persistencia::gerarLinhaCSV)
                .collect(Collectors.toList());

        Files.write(Path.of(DADOS_FILE), linhas);
    }

    public static void removerRegistrosPorDiretorio(String diretorio) throws IOException {
        List<ArquivoPDF> todos = carregarRegistros();
        List<ArquivoPDF> filtrados = todos.stream()
                .filter(r -> !Paths.get(r.getCaminhoArquivo()).startsWith(diretorio))
                .toList();
        atualizarRegistros(filtrados);
    }

    private static String gerarLinhaCSV(ArquivoPDF arquivo) {
        StringBuilder sb = new StringBuilder();
        sb.append(arquivo.getTipo()).append(",");

        sb.append("\"").append(String.join(", ", arquivo.getAutores())).append("\"").append(",");
        sb.append(arquivo.getTitulo()).append(",");

        if (arquivo instanceof Livro livro) {
            sb.append(livro.getSubtitulo()).append(",");
            sb.append(",");
            sb.append(livro.getAreaConhecimento()).append(",");
            sb.append(livro.getAnoPublicacao()).append(",");
        } else if (arquivo instanceof NotaDeAula nota) {
            sb.append(nota.getSubtitulo()).append(",");
            sb.append(nota.getDisciplina()).append(",");
            sb.append(",").append(",");
        } else if (arquivo instanceof Slide slide) {
            sb.append(",");
            sb.append(slide.getDisciplina()).append(",");
            sb.append(",").append(",");
        }
        sb.append(arquivo.getCaminhoArquivo());

        return sb.toString();
    }

    private static ArquivoPDF converterLinhaParaObjeto(String linha) {
        try {
            String[] partes = parseCSVLinha(linha);
            String tipo = partes[0];
            List<String> autores = Arrays.asList(partes[1].split(",\\s*"));
            String titulo = partes[2];
            String subtitulo = partes[3];
            String disciplina = partes[4];
            String areaConhecimento = partes[5];
            int ano = partes[6].isEmpty() ? 0 : Integer.parseInt(partes[6]);
            String caminhoArquivo = partes[7];

            return switch (tipo) {
                case "Livro" -> new Livro(autores, titulo, subtitulo, areaConhecimento, ano, caminhoArquivo);
                case "NotaDeAula" -> new NotaDeAula(autores, titulo, subtitulo, disciplina, caminhoArquivo);
                case "Slide" -> new Slide(autores, titulo, disciplina, caminhoArquivo);
                default -> null;
            };
        } catch (Exception e) {
            System.out.println("Erro ao converter linha do CSV: " + linha);
            return null;
        }
    }

    private static String[] parseCSVLinha(String linha) {
        List<String> partes = new ArrayList<>();
        boolean dentroAspas = false;
        StringBuilder sb = new StringBuilder();

        for (char c : linha.toCharArray()) {
            if (c == '"') {
                dentroAspas = !dentroAspas;
            } else if (c == ',' && !dentroAspas) {
                partes.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        partes.add(sb.toString());
        return partes.toArray(new String[0]);
    }
}