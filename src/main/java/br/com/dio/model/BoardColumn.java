package br.com.dio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class BoardColumn {
    private int id;
    private String name;
    private int boardId;

    public BoardColumn(String name, int boardId) {
        this.name = name;
        this.boardId = boardId;
    }
}
