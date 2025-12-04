package br.com.dio.service;

import br.com.dio.model.Board;
import br.com.dio.model.BoardColumn;
import br.com.dio.model.Card;

import br.com.dio.persistence.dao.BoardColumnDAO;
import br.com.dio.persistence.dao.BoardDAO;

import java.util.stream.Collectors;

import br.com.dio.dto.BoardDTO;
import br.com.dio.dto.BoardColumnDTO;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardQueryService {
    private final BoardColumnDAO boardColumnDAO;
    private final CardQueryService cardQueryService;
    private final BoardDAO boardDAO;

    public BoardQueryService(BoardDAO boardDAO, BoardColumnDAO boardColumnDAO , CardQueryService cardQueryService ) {
        this.boardDAO = boardDAO;
        this.cardQueryService = cardQueryService;
        this.boardColumnDAO = boardColumnDAO;

    }


    public Optional<Board> findBoardById(int id) {

        return boardDAO.findById(id);
    }

    public List<Board> findAllBoards() {

        return boardDAO.findAll();
    }

    public List<BoardColumn> findAllColumnsByBoardId(int boardId) {
        return boardColumnDAO.findAllByBoardId(boardId);
    }

    public Optional<BoardDTO> findBoardByIdWithColumnsAndCards(int boardId) {
        Optional<Board> boardOptional = boardDAO.findById(boardId);

        if (boardOptional.isEmpty()) {
            return Optional.empty();
        }

        Board board = boardOptional.get();
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setName(board.getName());


        List<BoardColumn> columns = boardColumnDAO.findAllByBoardId(boardId);


        List<BoardColumnDTO> columnDTOs = columns.stream()
                .map(column -> {
                    BoardColumnDTO columnDTO = new BoardColumnDTO();


                    columnDTO.setId(column.getId());
                    columnDTO.setName(column.getName());


                    List<Card> cards = cardQueryService.findAllCardsByColumnId(column.getId());
                    columnDTO.setCards(
                            cards.stream()
                                    .map(CardQueryService::toCardDTO)
                                    .collect(Collectors.toList())
                    );
                    return columnDTO;
                })
                .collect(Collectors.toList());

        boardDTO.setColumns(columnDTOs);


        return Optional.of(boardDTO);
    }
}