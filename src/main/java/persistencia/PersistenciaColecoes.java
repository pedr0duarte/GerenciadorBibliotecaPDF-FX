package persistencia;

import modelos.ArquivoPDF;
import modelos.Colecao;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PersistenciaColecoes {
    private static final String ARQUIVO_COLECOES = "colecoes.csv";

    public static void salvarColecoes(List<Colecao<? extends ArquivoPDF>> colecoes) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(ARQUIVO_COLECOES))) {
            for (Colecao<? extends ArquivoPDF> c : colecoes) {
                for (ArquivoPDF pdf : c.getEntradas()) {
                    writer.write(String.join(";", c.getNome(), c.getAutor(), c.getTipo().getSimpleName(),
                            String.valueOf(c.getLimite()), pdf.getCaminhoArquivo()));
                    writer.newLine();
                }
            }
        }
    }

    public static List<String[]> carregarRegistrosBrutos() throws IOException {
        List<String[]> dados = new ArrayList<>();
        Path path = Path.of(ARQUIVO_COLECOES);
        if (!Files.exists(path))
            return dados;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                dados.add(linha.split(";"));
            }
        }
        return dados;
    }

    // Classe PersistenciaColecoes atualmente não reconstrói os objetos "Colecao"
    // com todos os dados possíveis.
    // Ficou faltando a criação integral de uma coleção a partir de um CSV
    // preenchido previamente.
}