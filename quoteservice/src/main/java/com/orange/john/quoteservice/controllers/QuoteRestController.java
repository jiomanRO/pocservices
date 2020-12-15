package com.orange.john.quoteservice.controllers;

import com.orange.john.quoteservice.model.Quote;
import com.orange.john.quoteservice.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
public class QuoteRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteRestController.class);

    private final QuoteService quoteService;

    @Autowired
    public QuoteRestController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/quote")
    public Optional<Quote> getRandomQuote() {
    /*   try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    */
        return quoteService.findRandomQuote();
    }

    @PostConstruct
    public void checkQuotesInDB() {
        int nQuotes = quoteService.findAllQuotes().size();
        if(nQuotes > 0 ) {
            LOGGER.info("QuoteRestController initialized, "
                    + nQuotes + " quotes available in database.");
        } else {
            LOGGER.warn("QuoteRestController initialized, "
                    + nQuotes + " quotes available in database.");
        }
    }
}
