package br.com.dio.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDTO {
    private int id;
    private String name;
    private List<BoardColumnDTO> columns; // Lista de Colunas (com seus Cartões)
}