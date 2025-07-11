📚 Gerenciador de Biblioteca PDF com JavaFX
📌 Descrição
Este projeto é um sistema de gerenciamento de bibliotecas de arquivos PDF, agora com uma interface gráfica de usuário (GUI) completa desenvolvida com JavaFX. O sistema permite organizar documentos PDF em diferentes bibliotecas, além de criar e gerenciar coleções temáticas de documentos.

A aplicação suporta a adição, listagem, busca e remoção de PDFs, tratando diferentes tipos de documentos como Livros, Slides e Notas de Aula. A persistência dos metadados é realizada em arquivos CSV, garantindo que os dados do usuário sejam mantidos entre as sessões.

✨ Funcionalidades da Interface Gráfica
Menu Principal Intuitivo: Navegue facilmente entre as diferentes funcionalidades do sistema.

Gerenciamento de PDFs:

Adicionar: Uma tela dedicada para adicionar novos PDFs (Livro, Slide, Nota de Aula), com seletor de arquivos.

Listar: Visualize todos os PDFs da sua biblioteca em uma tabela organizada.

Buscar: Encontre PDFs específicos por título.

Remover: Remova registros de PDF da sua biblioteca.

Gerenciamento de Coleções:

Criar Coleção: Defina um nome, autor, limite e tipo para criar novas coleções.

Visualizar Coleções: Veja todas as suas coleções em uma tabela detalhada.

Adicionar e Remover Entradas: Adicione PDFs da sua biblioteca a uma coleção ou remova-os através de janelas de diálogo interativas.


Exportar para BibTeX: Exporte coleções de livros para um arquivo .bib com um clique. 


Compactar Coleção: Empacote todos os PDFs de uma coleção em um arquivo .zip para fácil compartilhamento. 

📸 Telas da Aplicação
(Sugestão: Adicione aqui capturas de tela da sua aplicação em funcionamento)

(Tela de Coleções)

(Tela de Visualização de Entradas)

🛠️ Tecnologias Utilizadas
Java 17


JavaFX 21 para a Interface Gráfica de Usuário 

Maven para gerenciamento de dependências e build do projeto

🚀 Como Executar
Para executar o projeto, você precisa ter o Java 17 (ou superior) e o Maven instalados em seu sistema.

Clone o repositório para a sua máquina local.

Abra um terminal ou prompt de comando na pasta raiz do projeto (onde o arquivo pom.xml está localizado).

Execute o seguinte comando:

Bash

mvn clean javafx:run
O Maven irá baixar as dependências e iniciar a aplicação JavaFX.

📂 Estrutura do Projeto
A estrutura de diretórios foi organizada para separar a lógica de negócio, a interface gráfica e os dados de persistência.

GerenciadorBibliotecaFX/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── gui/                # Controladores e classes da interface
│   │   │   ├── gerenciador/        # Classes de lógica de negócio
│   │   │   ├── modelos/            # Classes de modelo (POJO)
│   │   │   ├── persistencia/       # Classes para salvar e carregar dados
│   │   │   └── excecoes/           # Exceções customizadas
│   │   └── resources/
│   │       └── gui/                # Arquivos FXML que definem as telas
│   ├── ...
├── biblioteca_dados.csv            # Armazena os metadados dos PDFs
├── biblioteca_path.txt             # Salva o caminho da última biblioteca usada
├── colecoes.csv                    # Armazena os dados das coleções
├── pom.xml                         # Arquivo de configuração do Maven
└── README.md
👨‍💻 Autor
Aluno: PEDRO HENRIQUE DUARTE DE OLIVEIRA - 20190003968

Docente: VALDIGLEIS DA SILVA COSTA

Disciplina: DIM0116 - LINGUAGEM DE PROGRAMAÇÃO II - T02 (2025.1)