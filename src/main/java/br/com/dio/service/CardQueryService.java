package br.com.dio.service;

import br.com.dio.model.Card;
import br.com.dio.persistence.dao.CardDAO;
import br.com.dio.persistence.dao.CardDAOImpl;
import java.util.Optional;
import java.util.List;
import br.com.dio.dto.CardDTO;
import org.springframework.stereotype.Service;

@Service
public class CardQueryService {
    private final CardDAO cardDAO;

    public CardQueryService() {
        this.cardDAO = new CardDAOImpl();
    }

    public List<Card> findAllCardsByColumnId(int columnId) {
        return cardDAO.findByColumnId(columnId);
    }

    public Optional<Card> findCardById(int cardId) {
        return cardDAO.findById(cardId);
    }

    public static CardDTO toCardDTO(Card card) {
        if (card == null) return null;
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        return dto;
    }
}

