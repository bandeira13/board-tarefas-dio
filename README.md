📋 Board de Tarefas - API de Estudo

Este projeto nasceu inicialmente com o objetivo de ser um Board de Tarefas (estilo Kanban) e evoluiu para se tornar uma API REST completa para fins de estudo.
O foco principal do desenvolvimento foi praticar a construção de uma aplicação Java com Spring Boot, explorando conceitos fundamentais de Backend, como arquitetura em camadas, persistência de dados e integração com um Frontend.

🚀 O Que Usei no Projeto?

Para tirar essa ideia do papel, utilizei as tecnologias que o mercado pede, focando em boas práticas:

    Java 17: A base sólida de tudo.
    Spring Boot 3: Para agilizar o desenvolvimento e cuidar de toda a configuração mágica (Web, JDBC, Validações).
    Gradle: Para gerir as dependências do projeto.
    H2 Database: Um banco de dados SQL que roda num arquivo local (perfeito para testes rápidos sem precisar instalar nada pesado).
    Liquibase: Para controlar as versões do banco de dados (porque criar tabelas na mão é coisa do passado!).
    HTML/CSS/JS: Um Frontend simples que fiz para não ficar a testar apenas com telas pretas de terminal.

⚙️ Funcionalidades

    Quadros (Boards): Criação, listagem, exclusão e visualização detalhada.
    Colunas Automáticas: Ao criar um quadro, o sistema gera automaticamente as colunas: "A Fazer", "Em Progresso" e "Concluído".
    Cartões (Cards): Criação, edição e exclusão de tarefas dentro das colunas.
    Movimentação: Funcionalidade de mover cartões entre colunas (ex: de "A Fazer" para "Em Progresso").
    Persistência Local: Os dados são salvos em um arquivo local (board_db.mv.db), garantindo que as informações não se percam ao reiniciar a aplicação.

🛠️ Como Executar
Pré-requisitos

    Java JDK 17 ou superior.

Passos

    Clone o repositório:
    
    Bash
    git clone https://github.com/seu-usuario/board-tarefas-dio.git
    cd board-tarefas-dio

Execute a aplicação:

    Windows:
    ./gradlew.bat bootRun

Linux/Mac:
            
    Bash
        ./gradlew bootRun
    Acesse:
        Frontend: http://localhost:8080
        Banco de Dados (H2 Console): http://localhost:8080/h2-console

🗄️ Configuração do Banco de Dados

O projeto utiliza o H2 Database em modo arquivo.

    URL JDBC: jdbc:h2:file:./board_db;DB_CLOSE_DELAY=-1;MODE=MySQL
    Usuário: sa
    Senha: (vazio)

🔌 Endpoints Principais

    Método	    Recurso	                  Descrição
     GET 	  /api/boards	         Lista todos os quadros
     POST	  /api/boards	         Cria um novo quadro
     GET	  /api/boards/{id}       Detalhes de um quadro (com colunas e cards)
     POST	  /api/cards	         Cria um novo cartão
     PUT	  /api/cards/{id}/move   Move um cartão de coluna

🧪 Testes

O projeto conta com Testes de Integração para validar o fluxo de criação de boards. Para rodar os testes:
    
    Bash
    ./gradlew test

O teste principal verifica se um board é salvo corretamente no banco e se pode ser recuperado.

📝 Autor

    Desenvolvido por bandeira13.
