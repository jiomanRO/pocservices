package com.orange.john.quoteservice.service;

import com.orange.john.quoteservice.model.Quote;
import com.orange.john.quoteservice.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;

    @Autowired
    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public List<Quote> findAllQuotes() {
        List<Quote> quoteList = new ArrayList<>();
        for (Quote quote : quoteRepository.findAll()) {
            quoteList.add(quote);
        }
        return quoteList;
    }

    @Override
    public Optional<Quote> findRandomQuote() {
        return quoteRepository.findById((long)getRandomNumberUsingNextInt(1, (int) quoteRepository.count()));
    }

    private int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
