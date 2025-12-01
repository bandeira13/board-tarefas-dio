package br.com.dio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCardRequest {

    @NotBlank(message = "O título é obrigatório.")
    private String title;

    private String description;
}