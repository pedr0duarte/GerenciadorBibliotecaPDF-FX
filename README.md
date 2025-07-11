# 📚 Gerenciador de Biblioteca PDF

## 📌 Descrição

Este projeto representa um sistema de gerenciamento de bibliotecas de arquivos PDF operado via linha de comando.
O sistema organiza documentos em bibliotecas independentes, permitindo-se adicionar, listar, buscar, editar e excluir registros de arquivos.
A persistência dos metadados é feita em arquivos CSV.
Todas as funcionalidades seguem princípios da Programação Orientada a Objetos (POO), incluindo encapsulamento, herança e tratamento de exceções.

Agora com suporte a coleções, exportações BibTeX (.bib) e compactação de coleções (.zip).

---

## ⚙️ Funcionalidades

### Parte 1 do Menu – Biblioteca de Arquivos PDF

- 📁 **Criar nova biblioteca** em diretório personalizado.
- 🔄 **Alternar entre bibliotecas** e deletar biblioteca atual.
- ➕ **Adicionar documentos**:
  - Tipos: `Livro`, `Nota de Aula`, `Slide`.
  - PDF copiado para subdiretório com nome do autor.
- 📄 **Listar registros** da biblioteca ativa.
- 🔍 **Buscar registros** por termo no título, subtítulo ou autor.
- ✏️ **Editar registros**, incluindo mudança de autor (move o PDF).
- ❌ **Deletar registros**, removendo também o arquivo.
- 💾 **Persistência** dos registros em `biblioteca_dados.csv`.
- 📌 **Persistência da biblioteca ativa** em `biblioteca_path.txt`.

---

### Parte 2 do Menu – Gerenciamento de Coleções

- 📚 **Criar coleção** por tipo (Livro, Nota, Slide), autor e limite máximo.
  - Exibe os títulos das entradas incluídas na coleção.
- ➕ **Adicionar entrada à coleção** com verificação de tipo e autor.
  - Exibe aviso se o limite for atingido ou entrada for incompatível.
- ➖ **Remover entrada da coleção**.
  - Coleções vazias são deletadas automaticamente.
- 📤 **Exportar coleção de livros para arquivo `.bib`** (BibTeX).
- 🗜️ **Compactar coleção em `.zip`** com os PDFs.
- 📄 **Listar todas as coleções** com autor, tipo, limite e entradas.
- 💾 **Persistência das coleções** (opcional) via `colecoes.csv`.

---

## 📂 Estrutura de Diretórios

```
GerenciadorBibliotecaColecoesPDF/
├── src/
│   ├── interfaceusuario/
│   │   └── InterfaceUsuario.java
│   ├── gerenciador/
│   │   ├── GerenciadorBiblioteca.java
│   │   └── GerenciadorColecoes.java
│   ├── modelos/
│   │   ├── ArquivoPDF.java
│   │   ├── Colecao.java
│   │   ├── Livro.java
│   │   ├── NotaDeAula.java
│   │   └── Slide.java
│   ├── persistencia/
│   │   ├── Persistencia.java
│   │   └── PersistenciaColecoes.java
│   └── excecoes/
│       └── Excecoes.java
├── bin/  (aqui estão seus arquivos .class compilados, seguindo a mesma estrutura de pacotes)
│   ├── interfaceusuario/
│   │   └── InterfaceUsuario.class
│   ├── gerenciador/
│   │   ├── GerenciadorBiblioteca.class
│   │   └── GerenciadorColecoes.class
│   ├── modelos/
│   │   ├── ArquivoPDF.class
│   │   ├── Colecao.class
│   │   ├── Livro.class
│   │   ├── NotaDeAula.class
│   │   └── Slide.class
│   ├── persistencia/
│   │   ├── Persistencia.class
│   │   └── PersistenciaColecoes.class
│   └── excecoes/
│       └── Excecoes.class
├── biblioteca_dados.csv
├── biblioteca_path.txt
├── colecoes.csv (opcional)
└── README.md
```

## 🧠 Estrutura Interna

### 📦 Pacotes e Principais Classes

| Pacote           | Classe                          | Finalidade |
|------------------|----------------------------------|------------|
| `interfaceusuario` | `InterfaceUsuario`               | Menu principal e interação com o usuário. |
| `gerenciador`     | `GerenciadorBiblioteca`          | Adiciona, edita, lista e busca registros. |
|                  | `GerenciadorColecoes`            | Controla coleções, exportações e limites. |
| `modelos`         | `ArquivoPDF` (abstract)          | Base para os tipos de documentos. |
|                  | `Livro`, `NotaDeAula`, `Slide`    | Subtipos com campos específicos. |
|                  | `Colecao<T>`                     | Representa uma coleção genérica. |
| `persistencia`    | `Persistencia`                   | Lê/escreve CSV de registros e caminhos. |
|                  | `PersistenciaColecoes` (opcional) | Lê/escreve CSV de coleções. |
| `excecoes`        | `Excecoes`                       | Exceção personalizada para erros de operação. |

---


## 🧪 Plano de Testes

| Ação                            | Esperado                                 |
|---------------------------------|-------------------------------------------|
| Criar nova biblioteca           | Diretório criado e ativo.                 |
| Adicionar livro válido          | PDF movido, registro criado.              |
| Editar registro                 | PDF movido se autor for alterado.         |
| Buscar por autor/título         | Retorna registros corretos.               |
| Criar coleção válida            | Exibe entradas incluídas.                 |
| Adicionar entrada duplicada     | Exibe mensagem de erro.                   |
| Exportar coleção de livros      | Gera arquivo `.bib` com campos BibTeX.    |
| Compactar coleção               | Gera `.zip` com PDFs da coleção.          |
| Deletar registro                | Remove CSV e PDF.                         |
| Deletar biblioteca              | Remove diretório e registros.             |

---

## 🚀 EXECUÇÃO

1. Compile e Execute o programa principal:
   	InterfaceUsuario.java

ou

2. Execute o .jar:
	java -jar GerenciadorBibliotecaColecoesPDF.jar

3. Siga o menu principal:

```
==== MENU PRINCIPAL ====
1. Gerenciar Biblioteca
2. Gerenciar Coleções
3. Sair
```

---

## 📘 Documentação de Métodos

### InterfaceUsuario.java

- `main()`: Inicia a aplicação e exibe o menu.
- `adicionarLivro(...)`: Adiciona livro com dados do usuário.
- `adicionarNotaDeAula(...)`: Adiciona nota de aula.
- `adicionarSlide(...)`: Adiciona slide.
- `listarRegistros(...)`: Lista os registros da biblioteca ativa.
- `buscarRegistros(...)`: Busca registros por termo.
- `editarRegistro(...)`: Edita dados e atualiza local do arquivo.
- `deletarRegistro(...)`: Remove registro e arquivo PDF.
- `criarColecao(...)`: Cria nova coleção e mostra entradas incluídas.
- `adicionarEntradaColecao(...)`: Adiciona entrada com validação.
- `removerEntradaColecao(...)`: Remove entrada e deleta coleção se vazia.
- `exportarBibTex(...)`: Exporta coleção de livros como `.bib`.
- `compactarColecao(...)`: Gera `.zip` com PDFs da coleção.
- `listarColecoes(...)`: Mostra todas as coleções existentes.

---

## ✅ Requisitos

- Java 17 ou superior
- Terminal com suporte a entrada de texto
- Permissões de leitura e escrita no sistema de arquivos

---

## 👨‍💻 Autor

Aluno: PEDRO HENRIQUE DUARTE DE OLIVEIRA - 20190003968
Docente: VALDIGLEIS DA SILVA COSTA
Disciplina: DIM0116 - LINGUAGEM DE PROGRAMAÇÃO II - T02 (2025.1)

Trabalho prático de Programação Orientada a Objetos, com foco em:
- Encapsulamento, herança e polimorfismo
- Uso de generics e coleções
- Organização modular e persistência em arquivos