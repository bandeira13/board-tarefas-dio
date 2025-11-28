package br.com.dio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBoardRequest {

    @NotBlank(message = "O nome do quadro é obrigatório.")
    private String name;
}