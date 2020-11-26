package com.orange.john.quoteservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "quote")
@Table(name = "quotes")
public class Quote {
    //create table quotes (quote_id INTEGER, quote_text varchar(2000), quote_author varchar(1000));

    @Id
    @Column(name="quote_id")
    private Long quoteId;

    @Column(name = "quote_text")
    private String quoteText;

    @Column(name = "quote_author")
    private String quoteAuthor;

    public Long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }
}
