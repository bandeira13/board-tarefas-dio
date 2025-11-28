package br.com.dio.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardColumnDTO {
    private int id;
    private String name;
    private List<CardDTO> cards; // Lista de Cartões dentro da Coluna
}