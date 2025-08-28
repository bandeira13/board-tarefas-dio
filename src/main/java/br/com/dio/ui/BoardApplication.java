package br.com.dio.ui;

import br.com.dio.model.Board;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class BoardApplication extends Application {

    private final BoardQueryService boardQueryService = new BoardQueryService();
    private final BoardService boardService = new BoardService();

    private ListView<String> boardListView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Meu Board de Tarefas");

        boardListView = new ListView<>();

        Label newBoardLabel = new Label("Nome do Novo Quadro:");
        TextField newBoardTextField = new TextField();
        Button createBoardButton = new Button("Criar");
        createBoardButton.setOnAction(event -> createNewBoard(newBoardTextField));

        HBox createBoardBox = new HBox(10);
        createBoardBox.getChildren().addAll(newBoardLabel, newBoardTextField, createBoardButton);

        Button deleteBoardButton = new Button("Deletar Selecionado");
        deleteBoardButton.setOnAction(event -> deleteSelectedBoard());

        refreshBoardList();

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(createBoardBox, new Label("Quadros Existentes:"), boardListView, deleteBoardButton);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createNewBoard(TextField textField) {
        String boardName = textField.getText();
        if (boardName != null && !boardName.trim().isEmpty()) {
            boardService.createNewBoard(boardName);
            textField.clear();
            refreshBoardList();
        }
    }

    private void deleteSelectedBoard() {
        String selectedItem = boardListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione um quadro para deletar.");
            alert.showAndWait();
            return;
        }

        try {
            int boardId = Integer.parseInt(selectedItem.split(" - ")[0].replace("ID: ", ""));

            boolean deleted = boardService.deleteBoard(boardId);

            if (deleted) {
                refreshBoardList();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Não foi possível deletar o quadro.");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ocorreu um erro ao identificar o ID do quadro selecionado.");
            alert.showAndWait();
        }
    }

    private void refreshBoardList() {
        List<Board> boards = boardQueryService.findAllBoards();
        List<String> boardNames = boards.stream()
                .map(board -> String.format("ID: %d - %s", board.getId(), board.getName()))
                .collect(Collectors.toList());
        boardListView.setItems(FXCollections.observableArrayList(boardNames));
    }

    public static void main(String[] args) {
        br.com.dio.persistence.config.ConnectionConfig.initializeDatabase();
        launch(args);
    }
}