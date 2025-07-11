📚 Gerenciador de Biblioteca PDF com JavaFX
📌 Descrição
Este projeto é um sistema de gerenciamento de bibliotecas de arquivos PDF, agora com uma interface gráfica de usuário (GUI) completa desenvolvida com JavaFX. O sistema permite organizar documentos PDF em diferentes bibliotecas, além de criar e gerenciar coleções temáticas de documentos.

A aplicação suporta a adição, listagem, busca, edição e remoção de PDFs, tratando diferentes tipos de documentos como Livros, Slides e Notas de Aula. A persistência dos metadados é realizada em arquivos CSV, garantindo que os dados do usuário sejam mantidos entre as sessões.

✨ Funcionalidades da Interface Gráfica
Gerenciamento de Bibliotecas

Criar novas bibliotecas em qualquer diretório.

Alternar entre bibliotecas existentes.

Deletar bibliotecas inteiras, incluindo os arquivos físicos.

Gerenciamento de PDFs

Adicionar novos documentos (Livros, Slides, Notas de Aula) com um seletor de arquivos.

Listar todos os PDFs da biblioteca ativa em uma tabela detalhada.

Editar os metadados de um PDF existente.

Remover registros e seus arquivos correspondentes.

Gerenciamento de Coleções

Criar coleções personalizadas por tipo, autor e com limite de itens.

Adicionar e remover PDFs em coleções através de janelas interativas.

Exportar coleções de livros para o formato .bib (BibTeX).

Compactar os arquivos de uma coleção em um arquivo .zip.

🛠️ Tecnologias Utilizadas
Java 17

JavaFX 21 para a Interface Gráfica de Usuário

Maven para gerenciamento de dependências e build do projeto

🚀 Como Executar
Pré-requisitos
Java Development Kit (JDK) 17 ou superior.

Apache Maven.

Passos
Clone o repositório para a sua máquina local:

Bash

git clone https://github.com/pedr0duarte/Gerenciador-de-Biblioteca-PDF-com-GUI.git
Navegue até a pasta raiz do projeto pelo terminal.

Execute o seguinte comando Maven:

Bash

mvn clean javafx:run
A aplicação será iniciada.

💾 Persistência de Dados
A aplicação garante que nenhuma informação seja perdida entre as sessões:

Metadados dos PDFs: Todas as informações sobre os arquivos são salvas em biblioteca_dados.csv.

Coleções: As coleções criadas e suas entradas são armazenadas em colecoes.csv.

Biblioteca Ativa: O caminho para a última biblioteca utilizada é mantido em biblioteca_path.txt, permitindo que o programa reabra no mesmo estado.

📂 Estrutura do Projeto
GerenciadorBibliotecaFX/
├── src/
│   └── main/
│       ├── java/
│       │   ├── gui/                # Controladores e classes da interface
│       │   ├── gerenciador/        # Classes de lógica de negócio
│       │   ├── modelos/            # Classes de modelo (POJO)
│       │   ├── persistencia/       # Classes para salvar e carregar dados
│       │   └── excecoes/           # Exceções customizadas
│       └── resources/
│           └── gui/                # Arquivos FXML que definem as telas
├── biblioteca_dados.csv            # Armazena os metadados dos PDFs
├── biblioteca_path.txt             # Salva o caminho da última biblioteca usada
├── colecoes.csv                    # Armazena os dados das coleções
├── pom.xml                         # Arquivo de configuração do Maven
└── README.md
👨‍💻 Autor
Aluno: PEDRO HENRIQUE DUARTE DE OLIVEIRA - 20190003968

Docente: VALDIGLEIS DA SILVA COSTA

Disciplina: DIM0116 - LINGUAGEM DE PROGRAMAÇÃO II - T02 (2025.1)
