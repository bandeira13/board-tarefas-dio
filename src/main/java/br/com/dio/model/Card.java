package br.com.dio.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Card {
    private int id;
    private String title;
    private String description;
    private int columnId;

    public Card(String title, String description, int columnId) {
        this.title = title;
        this.description = description;
        this.columnId = columnId;
    }


}
