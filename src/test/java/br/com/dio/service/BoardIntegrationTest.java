package br.com.dio.service;


import br.com.dio.model.Board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BoardIntegrationTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardQueryService boardQueryService;

    @Test
    public void testarCenarioCompletoDeCriacaoDeBoard() {

        String nomeDoQuadro = "Quadro de Teste Automatizado";

        int totalAntes = boardQueryService.findAllBoards().size();

        System.out.println("Criando board via teste...");
        boardService.createNewBoard(nomeDoQuadro);

        List<Board> todosQuadros = boardQueryService.findAllBoards();


        Assertions.assertEquals(totalAntes + 1, todosQuadros.size());


        Board quadroCriado = todosQuadros.stream()
                .filter(b -> nomeDoQuadro.equals(b.getName()))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(quadroCriado, "Quadro criado não encontrado na lista de quadros!");
        Assertions.assertEquals(nomeDoQuadro, quadroCriado.getName());

        System.out.println("Teste finalizado com sucesso! ID do novo board: " + quadroCriado.getId());
    }
}