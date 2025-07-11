package modelos;

import java.util.*;

public class Colecao<T extends ArquivoPDF> {
    private String nome;
    private String autor;
    private int limite;
    private Class<T> tipo;
    private List<T> entradas;

    public Colecao(String nome, String autor, int limite, Class<T> tipo) {
        this.nome = nome;
        this.autor = autor;
        this.limite = limite;
        this.tipo = tipo;
        this.entradas = new ArrayList<>();
    }

    public boolean adicionarEntrada(T entrada) {
        if (!entrada.getAutores().contains(autor))
            return false;
        if (!tipo.isInstance(entrada))
            return false;
        if (entradas.size() >= limite)
            return false;
        entradas.add(entrada);
        return true;
    }

    public boolean removerEntrada(T entrada) {
        return entradas.remove(entrada);
    }

    public boolean estaVazia() {
        return entradas.isEmpty();
    }

    public String getNome() {
        return nome;
    }

    public String getAutor() {
        return autor;
    }

    public int getLimite() {
        return limite;
    }

    public Class<T> getTipo() {
        return tipo;
    }

    public List<T> getEntradas() {
        return entradas;
    }

    @Override
    public String toString() {
        return "Coleção \"" + nome + "\" do autor \"" + autor + "\" (" + entradas.size() + "/" + limite + ")";
    }
}
