package br.com.dio.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MoveCardRequest {

    @Min(value = 1, message = "O ID da nova coluna deve ser válido.")
    private int newColumnId;
}