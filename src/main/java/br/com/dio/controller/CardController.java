package br.com.dio.controller;


import br.com.dio.dto.CreateCardRequest;
import br.com.dio.dto.MoveCardRequest;
import br.com.dio.exception.NotFoundException;
import br.com.dio.model.Card;
import br.com.dio.service.CardQueryService;
import br.com.dio.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;
    private final CardQueryService cardQueryService;

    public CardController(CardService cardService, CardQueryService cardQueryService) {
        this.cardService = cardService;
        this.cardQueryService = cardQueryService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna 201 Created
    public void createCard(@Validated @RequestBody CreateCardRequest request) {
        cardService.createNewCard(request.getTitle(), request.getDescription(), request.getColumnId());
    }


    @PutMapping("/{id}/move")
    public ResponseEntity<Void> moveCard(@PathVariable int id, @Validated @RequestBody MoveCardRequest request) {


        Card card = cardQueryService.findCardById(id)
                .orElseThrow(() -> new NotFoundException("Cartão com ID " + id + " não encontrado."));


        cardService.moveCard(card, request.getNewColumnId());

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable int id) {


        cardQueryService.findCardById(id)
                .orElseThrow(() -> new NotFoundException("Cartão com ID " + id + " não encontrado."));

        cardService.deleteCard(id);

        return ResponseEntity.noContent().build(); // Retorna 204 No Content (sucesso sem corpo)
    }
}



