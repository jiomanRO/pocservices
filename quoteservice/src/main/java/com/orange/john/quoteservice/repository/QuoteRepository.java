package com.orange.john.quoteservice.repository;

import com.orange.john.quoteservice.model.Quote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends CrudRepository<Quote,Long> {
}
