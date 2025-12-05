package br.com.dio.controller;

import br.com.dio.dto.BoardDTO;
import br.com.dio.dto.CreateBoardRequest;
import br.com.dio.exception.NotFoundException;

import br.com.dio.model.Board;
import br.com.dio.model.BoardColumn;

import br.com.dio.service.BoardColumnQueryService;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = "*")
public class BoardController {

    private final BoardService boardService;
    private final BoardQueryService boardQueryService;
    private final BoardColumnQueryService boardColumnQueryService;

    public BoardController(BoardService boardService,
                          BoardQueryService boardQueryService,
                          BoardColumnQueryService boardColumnQueryService) {
        this.boardService = boardService;
        this.boardQueryService = boardQueryService;
        this.boardColumnQueryService = boardColumnQueryService;
    }

    @GetMapping
    public ResponseEntity<List<Board>> findAllBoards() {
        List<Board> boards = boardQueryService.findAllBoards();
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> findBoardById(@PathVariable int id) {

        Optional<BoardDTO> board = boardQueryService.findBoardByIdWithColumnsAndCards(id);


        return board.map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Quadro com ID " + id + " não encontrado."));
    }

    @GetMapping("/{id}/columns")
    public ResponseEntity<List<BoardColumn>> getBoardColumns(@PathVariable int id) {
        return ResponseEntity.ok(boardColumnQueryService.findAllByBoardId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBoard(@Validated @RequestBody CreateBoardRequest request) {

        boardService.createNewBoard(request.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable int id) {
        boolean deleted = boardService.deleteBoard(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {

            throw new NotFoundException("Quadro com ID " + id + " não encontrado.");
        }
    }

    public static class CreateBoardRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}