package br.com.dio.dto;

import br.com.dio.model.Card;
import lombok.Data;

@Data
public class CardDTO {
    private int id;
    private String title;
    private String description;
    private int columnId;

    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.columnId = card.getColumnId();
    }
}