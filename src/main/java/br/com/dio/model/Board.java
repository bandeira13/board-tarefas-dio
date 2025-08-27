package br.com.dio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Board {
    private int id;
    private String name;

    public Board(){}

    public Board (String name){
        this.name = name;
    }

}

