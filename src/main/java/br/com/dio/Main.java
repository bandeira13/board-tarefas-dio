package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.BoardColumn;
import br.com.dio.model.Card;
import br.com.dio.persistence.config.ConnectionConfig;
import br.com.dio.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Main {
    private final Logger logger = LoggerFactory.getLogger(Main.class);
    private final BoardService boardService ;
    private final CardService cardService;
    private final BoardQueryService boardQueryService ;
    private final BoardColumnQueryService boardColumnQueryService ;
    private final CardQueryService cardQueryService;
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public Main(BoardService boardService, CardService cardService, BoardQueryService boardQueryService, BoardColumnQueryService boardColumnQueryService, CardQueryService cardQueryService) {
        this.boardService = boardService;
        this.cardService = cardService;
        this.boardQueryService = boardQueryService;
        this.boardColumnQueryService = boardColumnQueryService;
        this.cardQueryService = cardQueryService;
    }

    public void startCLI() {
        ConnectionConfig.initializeDatabase();

        logger.info("Bem-vindo ao seu Board de Tarefas!");
        mainMenuLoop();
    }
    private void mainMenuLoop() {
        while (true) {
            printMainMenu();
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> createNewBoard();
                case 2 -> listAllBoards();
                case 3 -> deleteBoard();
                case 4 -> selectBoard();
                case 5 -> {
                    logger.info("Até logo!");
                    return;
                }
                default -> logger.warn("Opcao inválida. Tente novamente.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Criar novo quadro");
        System.out.println("2. Listar todos os quadros");
        System.out.println("3. Deletar um quadro");
        System.out.println("4. Selecionar um quadro");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opcao: ");
    }

    private void createNewBoard() {
        System.out.print("Digite o nome do novo quadro: ");
        String boardName = scanner.next();
        boardService.createNewBoard(boardName);
    }

    private void listAllBoards() {
        System.out.println("\n--- LISTA DE QUADROS ---");
        var boards = boardQueryService.findAllBoards();
        if (boards.isEmpty()) {
            logger.info("Nenhum quadro foi criado ainda.");
        } else {
            boards.forEach(board -> System.out.printf("ID: %d - Nome: %s\n", board.getId(), board.getName()));
        }
    }

    private void deleteBoard() {
        System.out.print("\nDigite o ID do quadro que deseja deletar: ");
        int boardId = scanner.nextInt();
        boardService.deleteBoard(boardId);
    }

    private void selectBoard() {
        System.out.print("\nDigite o ID do quadro que deseja selecionar: ");
        int boardId = scanner.nextInt();
        Optional<Board> boardOptional = boardQueryService.findBoardById(boardId);

        if (boardOptional.isEmpty()) {
            logger.error("Quadro não encontrado!");
            return;
        }

        boardMenuLoop(boardOptional.get());
    }


    private void boardMenuLoop(Board board) {
        while (true) {
            printBoardMenu(board.getName());
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> viewBoard(board);
                case 2 -> createNewCard(board);
                case 3 -> moveCard(board);
                case 4 -> deleteCard();
                case 5 -> { return; }
                default -> logger.warn("Opcao inválida.");
            }
        }
    }

    private void printBoardMenu(String boardName) {
        System.out.printf("\n--- Gerenciando o Quadro: %s ---\n", boardName);
        System.out.println("1. Visualizar quadro completo (colunas e cartões)");
        System.out.println("2. Criar novo cartão");
        System.out.println("3. Mover cartão");
        System.out.println("4. Deletar cartão");
        System.out.println("5. Voltar ao menu principal");
        System.out.print("Escolha uma opção: ");
    }

    private void viewBoard(Board board) {
        List<BoardColumn> columns = boardColumnQueryService.findAllByBoardId(board.getId());
        System.out.println("\n---------------------------------");
        System.out.printf("QUADRO: %s\n", board.getName());
        System.out.println("---------------------------------");
        for (BoardColumn column : columns) {
            System.out.printf("\nColuna: %s (ID: %d)\n", column.getName(), column.getId());
            List<Card> cards = cardQueryService.findAllCardsByColumnId(column.getId());
            if (cards.isEmpty()) {
                System.out.println("  (Vazia)");
            } else {
                cards.forEach(card ->
                        System.out.printf("  - Card ID: %d | Título: %s\n", card.getId(), card.getTitle())
                );
            }
        }
        System.out.println("---------------------------------");
    }

    private void createNewCard(Board board) {
        List<BoardColumn> columns = boardColumnQueryService.findAllByBoardId(board.getId());

        System.out.println("\n--- Criar Novo Cartão ---");
        System.out.print("Título do cartão: ");
        String title = scanner.next();
        System.out.print("Descrição do cartão: ");
        String description = scanner.next();

        System.out.println("Em qual coluna o cartão deve ser criado?");
        columns.forEach(c -> System.out.printf("ID: %d - Nome: %s\n", c.getId(), c.getName()));
        System.out.print("Digite o ID da coluna: ");
        int columnId = scanner.nextInt();

        cardService.createNewCard(title, description, columnId);
    }


    private void moveCard(Board board) {
        System.out.print("\nDigite o ID do cartão que deseja mover: ");
        int cardId = scanner.nextInt();


        Optional<Card> cardOptional = cardQueryService.findCardById(cardId);
        if (cardOptional.isEmpty()) {
            logger.error("Cartão com ID " + cardId + " não foi encontrado.");
            return;
        }

        System.out.println("\nPara qual coluna você deseja mover o cartão?");
        List<BoardColumn> columns = boardColumnQueryService.findAllByBoardId(board.getId());
        columns.forEach(c -> System.out.printf("ID: %d - Nome: %s\n", c.getId(), c.getName()));
        System.out.print("Digite o ID da nova coluna: ");
        int newColumnId = scanner.nextInt();


        boolean columnExistsInBoard = columns.stream().anyMatch(c -> c.getId() == newColumnId);
        if (!columnExistsInBoard) {
            logger.error("ID de coluna inválido ou não pertence a este quadro.");
            return;
        }

        cardService.moveCard(cardOptional.get(), newColumnId);
    }

    private void deleteCard() {
        System.out.print("\nDigite o ID do cartão que deseja deletar: ");
        int cardId = scanner.nextInt();
        cardService.deleteCard(cardId);
    }
}