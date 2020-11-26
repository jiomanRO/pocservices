package com.orange.john.quoteservice.service;

import com.orange.john.quoteservice.model.Quote;

import java.util.List;
import java.util.Optional;

public interface QuoteService {
    List<Quote> findAllQuotes();
    Optional<Quote> findRandomQuote();
}
