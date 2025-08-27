package br.com.dio.model;

public class Card {
    private int id;
    private String title;
    private String description;
    private int columnId;

    public Card (){}

    public Card(String title, String description, int columnId) {
        this.title = title;
        this.description = description;
        this.columnId = columnId;
    }
    public Card(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }
}
