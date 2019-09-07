package com.juheshi.video.dao;

import com.juheshi.video.entity.CardBag;

import java.util.List;

public interface CardBagDao {

    CardBag findOneCardBag(CardBag cardBag);

    List<CardBag> listCardBag(CardBag cardBag);

    int numCardBag(CardBag cardBag);

    void insertCardBag(CardBag cardBag);

    void updateCardBag(CardBag cardBag);

    void deleteCardBag(Integer id);

}
