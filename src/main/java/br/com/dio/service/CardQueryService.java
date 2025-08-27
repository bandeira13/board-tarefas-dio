package br.com.dio.service;

import br.com.dio.model.Card;
import br.com.dio.persistence.dao.CardDAO;
import br.com.dio.persistence.dao.CardDAOImpl;

import java.util.List;

public class CardQueryService {
    private final CardDAO cardDAO;

    public CardQueryService() {
        this.cardDAO = new CardDAOImpl();
    }

    public List<Card> findAllCardsByColumnId(int columnId) {
        return cardDAO.findByColumnId(columnId);
    }
}
