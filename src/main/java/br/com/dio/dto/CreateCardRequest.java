package br.com.dio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCardRequest {


    @NotBlank(message = "O título é obrigatório.")
    private String title;

    private String description;

    @Min(value = 1, message = "O ID da coluna deve ser válido.")
    private int columnId;
}