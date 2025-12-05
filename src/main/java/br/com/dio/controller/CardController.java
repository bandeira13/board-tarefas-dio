package br.com.dio.controller;

import br.com.dio.dto.CardDTO;
import br.com.dio.dto.CreateCardRequest;
import br.com.dio.dto.UpdateCardRequest;
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
@CrossOrigin(origins = "*")
public class CardController {

    private final CardService cardService;
    private final CardQueryService cardQueryService;

    public CardController(CardService cardService, CardQueryService cardQueryService) {
        this.cardService = cardService;
        this.cardQueryService = cardQueryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createCard(@Validated @RequestBody CreateCardRequest request) {
        cardService.createNewCard(request.getTitle(), request.getDescription(), request.getColumnId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> updateCard(@PathVariable int id, @Validated @RequestBody UpdateCardRequest request) {

        Card card = cardQueryService.findCardById(id)
                .orElseThrow(() -> new NotFoundException("Cartão com ID " + id + " não encontrado."));

        cardService.updateCard(card, request.getTitle(), request.getDescription());

        CardDTO cardDTO = new CardDTO(card);
        return ResponseEntity.ok(cardDTO);
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<Void> moveCard(@PathVariable int id, @RequestParam int newColumnId) {

        Card card = cardQueryService.findCardById(id)
                .orElseThrow(() -> new NotFoundException("Cartão com ID " + id + " não encontrado."));


        cardService.moveCard(card, newColumnId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable int id) {


        cardQueryService.findCardById(id)
                .orElseThrow(() -> new NotFoundException("Cartão com ID " + id + " não encontrado."));

        cardService.deleteCard(id);

        return ResponseEntity.noContent().build();
    }
}
