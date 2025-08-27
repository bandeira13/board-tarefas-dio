package br.com.dio.model;

public class Board {
    private int id;
    private String name;

    public Board(){}

    public Board (String name){
        this.name = name;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

