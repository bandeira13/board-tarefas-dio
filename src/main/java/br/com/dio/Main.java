package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.BoardColumn;
import br.com.dio.model.Card;
import br.com.dio.service.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {private static final BoardService boardService = new BoardService();
    private static final CardService cardService = new CardService();
    private static final BoardQueryService boardQueryService = new BoardQueryService();
    private static final BoardColumnQueryService boardColumnQueryService = new BoardColumnQueryService();
    private static final CardQueryService cardQueryService = new CardQueryService();

    private static final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao seu Board de Tarefas!");
        mainMenuLoop();
    }
    private static void mainMenuLoop() {
        while (true) {
            printMainMenu();
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> createNewBoard();
                case 2 -> listAllBoards();
                case 3 -> deleteBoard();
                case 4 -> selectBoard();
                case 5 -> {
                    System.out.println("Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Criar novo quadro");
        System.out.println("2. Listar todos os quadros");
        System.out.println("3. Deletar um quadro");
        System.out.println("4. Selecionar um quadro");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void createNewBoard() {
        System.out.print("Digite o nome do novo quadro: ");
        String boardName = scanner.next();
        boardService.createNewBoard(boardName);
    }

    private static void listAllBoards() {
        System.out.println("\n--- LISTA DE QUADROS ---");
        var boards = boardQueryService.findAllBoards();
        if (boards.isEmpty()) {
            System.out.println("Nenhum quadro foi criado ainda.");
        } else {
            boards.forEach(board -> System.out.printf("ID: %d - Nome: %s\n", board.getId(), board.getName()));
        }
    }

    private static void deleteBoard() {
        System.out.print("\nDigite o ID do quadro que deseja deletar: ");
        int boardId = scanner.nextInt();
        boardService.deleteBoard(boardId);
    }

    private static void selectBoard() {
        System.out.print("\nDigite o ID do quadro que deseja selecionar: ");
        int boardId = scanner.nextInt();
        Optional<Board> boardOptional = boardQueryService.findBoardById(boardId);

        if (boardOptional.isEmpty()) {
            System.err.println("Quadro não encontrado!");
            return;
        }

        boardMenuLoop(boardOptional.get());
    }


    private static void boardMenuLoop(Board board) {
        while (true) {
            printBoardMenu(board.getName());
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> viewBoard(board);
                case 2 -> createNewCard(board);
                case 3 -> moveCard(board);
                case 4 -> deleteCard();
                case 5 -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void printBoardMenu(String boardName) {
        System.out.printf("\n--- Gerenciando o Quadro: %s ---\n", boardName);
        System.out.println("1. Visualizar quadro completo (colunas e cartões)");
        System.out.println("2. Criar novo cartão");
        System.out.println("3. Mover cartão");
        System.out.println("4. Deletar cartão");
        System.out.println("5. Voltar ao menu principal");
        System.out.print("Escolha uma opção: ");
    }

    private static void viewBoard(Board board) {
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

    private static void createNewCard(Board board) {
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

    private static void moveCard(Board board) {

        System.out.println("Funcionalidade de mover ainda em construção.");
        
    }

    private static void deleteCard() {
        System.out.print("\nDigite o ID do cartão que deseja deletar: ");
        int cardId = scanner.nextInt();
        cardService.deleteCard(cardId);
    }
}
