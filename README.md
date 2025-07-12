# 📚 Gerenciador de Biblioteca PDF com JavaFX

## 📌 Descrição

Este projeto é um sistema de gerenciamento de bibliotecas de arquivos PDF, agora com uma interface gráfica de usuário (GUI) completa desenvolvida com **JavaFX**.  
O sistema permite organizar documentos PDF em diferentes bibliotecas, além de criar e gerenciar coleções temáticas de documentos.

A aplicação suporta a **adição, listagem, busca, edição e remoção de PDFs**, tratando diferentes tipos de documentos como **Livros, Slides e Notas de Aula**.  
A **persistência dos metadados** é realizada em arquivos CSV, garantindo que os dados do usuário sejam mantidos entre as sessões.

---

## ✨ Funcionalidades da Interface Gráfica

### 📁 Gerenciamento de Bibliotecas
- Criar novas bibliotecas em qualquer diretório
- Alternar entre bibliotecas existentes
- Deletar bibliotecas inteiras, incluindo os arquivos físicos

### 📄 Gerenciamento de PDFs
- Adicionar novos documentos (Livros, Slides, Notas de Aula) com seletor de arquivos
- Listar todos os PDFs da biblioteca ativa em uma tabela detalhada
- Editar os metadados de um PDF existente
- Remover registros e seus arquivos correspondentes

### 📦 Gerenciamento de Coleções
- Criar coleções personalizadas por tipo, autor e com limite de itens
- Adicionar e remover PDFs em coleções através de janelas interativas
- Exportar coleções de livros para o formato `.bib` (BibTeX)
- Compactar os arquivos de uma coleção em um `.zip`

---

## 🛠️ Tecnologias Utilizadas

- Java 17  
- JavaFX 21  
- Maven (build e dependências)

---

## 🚀 Como Executar

### ✅ Pré-requisitos

- Java Development Kit (JDK) 17 ou superior
- Apache Maven instalado

### ▶️ Passos

1. Clone o repositório:

```bash
git clone https://github.com/pedr0duarte/GerenciadorBibliotecaPDF-FX.git
```

2. Execute o programa:

```bash
mvn clean javafx:run
```

---

## 💾 Persistência de Dados

A aplicação salva automaticamente os dados entre execuções:

| Tipo de Dado        | Arquivo CSV                  |
|---------------------|------------------------------|
| Metadados dos PDFs  | `biblioteca_dados.csv`       |
| Coleções criadas    | `colecoes.csv`               |
| Caminho da biblioteca ativa | `biblioteca_path.txt` |

---

## 📂 Estrutura do Projeto

```
GerenciadorBibliotecaPDF-FX/
├── src/
│   └── main/
│       ├── java/
│       │   ├── gui/                # Controladores e classes da interface
│       │   ├── gerenciador/        # Lógica de negócio
│       │   ├── modelos/            # Representação dos dados (POJO)
│       │   ├── persistencia/       # Salvamento e carregamento de dados
│       │   └── excecoes/           # Exceções customizadas
│       └── resources/
│           └── gui/                # Arquivos .fxml das telas
├── biblioteca_dados.csv            # Metadados dos PDFs
├── biblioteca_path.txt             # Última biblioteca usada
├── colecoes.csv                    # Dados das coleções
├── pom.xml                         # Configuração do Maven
└── README.md
```

---

## 👨‍💻 Autor

- **Aluno:** PEDRO HENRIQUE DUARTE DE OLIVEIRA – 20190003968  
- **Docente:** VALDIGLEIS DA SILVA COSTA  
- **Disciplina:** DIM0116 - LINGUAGEM DE PROGRAMAÇÃO II - T02 (2025.1)
