package com.orange.john.frontservice.controllers;

import com.orange.john.frontservice.model.Quote;
import com.orange.john.frontservice.model.RedisCounter;
import com.orange.john.frontservice.service.RedisCounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
public class QuoteRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteRestController.class);
    private WebClient webClient;
    private RestTemplate restTemplate;
    private RedisCounterService redisCounterService;

    @Value("${quoteservice.url}")
    private String quoteServiceUrl;

    @Value("${quotes.max.per.interval}")
    int quotesMaxPerInterval;

    @Value("${quotes.interval.seconds}")
    int quotesIntervalSeconds;

    @Autowired
    public QuoteRestController(RedisCounterService redisCounterService) {
         this.redisCounterService =redisCounterService;
    }

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        restTemplate = new RestTemplate();
    }

    @GetMapping("/admin")
    public String adminInfo() {
        return "Only admins can access this.";
    }

    @GetMapping("/quote")
    public ResponseEntity getQuote(HttpSession session) {
        /*
        For each client there is a limit of quotesMaxPerInterval quotes per quotesIntervalSeconds seconds for accessing the service.
        Needed information is kept in Redis in the form of RedisCounter objects.
         */
        boolean exhaustedQuotes = false;
        String sessionId = session.getId();
        String info;
        int remaining = quotesMaxPerInterval - 1;
        //client sessionId not present in Redis cache = new client
        if (!redisCounterService.findById(sessionId).isPresent()) {
            //save new RedisCounter object in Redis cache
            redisCounterService.save(new RedisCounter(session.getId(), 1, LocalDateTime.now()));
            info = "This is your first time accessing the service. You are limited to " + quotesMaxPerInterval + " quotes per " + quotesIntervalSeconds + " seconds.";
        } else {
            //get RedisCounter
            RedisCounter redisCounter = redisCounterService.findById(sessionId).get();
            LocalDateTime now = LocalDateTime.now();
            int timeElapsed = (int)Duration.between(redisCounter.getLastAccessedTime(), now).get(ChronoUnit.SECONDS);
            //if still remaining time from the interval
            if(timeElapsed < quotesIntervalSeconds) {
                //if still available quotes
                if(redisCounter.getCounter() < quotesMaxPerInterval) {
                    //increment counter
                    redisCounterService.incrementCounter(sessionId);
                    remaining -= redisCounter.getCounter();
                    info = "You are limited to "+ quotesMaxPerInterval + " quotes per " + quotesIntervalSeconds + " seconds.";
                } else {
                    //exhausted quotes for the interval
                    exhaustedQuotes = true;
                    info = "You have exhausted your "+ quotesMaxPerInterval + " quotes for " + quotesIntervalSeconds + " seconds. Try again in " + (quotesIntervalSeconds - timeElapsed) + " seconds.";
                }
            } else { // more than interval seconds elapsed
                //reset counter and lastAccessedTime
                redisCounterService.setCounter(sessionId, 1);
                redisCounterService.setLastAccessedTime(sessionId, now);
                info = "You are limited to "+ quotesMaxPerInterval + " quotes per " + quotesIntervalSeconds + " seconds.";
            }
        }

        if(exhaustedQuotes) {
            return ResponseEntity.ok().body(new Response(info));
        } else {
            return ResponseEntity.ok().body(new QuoteResponse(getQuoteTextFromQuoteService(), remaining, info));
        }
    }

    private String getQuoteTextFromQuoteService() {
        //blocking sync call to quoteService using RestTemplate
        ResponseEntity<Quote> httpResponse = restTemplate.exchange(
                quoteServiceUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<Quote>() {});
        Quote quote = httpResponse.getBody();
        if (quote != null) {
            return quote.getQuoteText();
        } else {
            return "";
        }
    }

    @GetMapping(value = "/unlimitedQuoteB")
    public ResponseEntity<QuoteResponse> getUnlimitedQuoteBlocking() {
        /*
        Blocking call using RestTemplate
         */
        String message;
        ResponseEntity<Quote> httpResponse = restTemplate.exchange(
                quoteServiceUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<Quote>() {
                });
        Quote quote = httpResponse.getBody();
        if (quote != null) {
            message = quote.getQuoteText();
        } else {
            message = "";
        }
        QuoteResponse response = new QuoteResponse(message, -1, "Unlimited quotes");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/unlimitedQuote")
    public Mono<ResponseEntity<QuoteResponse>> getUnlimitedQuote() {
        /*
        Non-blocking reactive call using WebClient and Mono from Spring webflux library
         */
        return webClient
                .get()
                .uri(quoteServiceUrl)
                .retrieve()
                .bodyToMono(Quote.class)
                .flatMap(quote -> Mono.just(ResponseEntity.ok().body(new QuoteResponse(quote.getQuoteText(), -1, "Unlimited quotes"))))
                // .log()
                .timeout(Duration.ofSeconds(30L))
                ;
    }
}

class QuoteResponse {
    QuoteResponse(String quote, int remaining, String info) {
        this.quote = quote;
        this.remaining = remaining;
        this.info = info;
    }

    private String quote;
    private int remaining;
    private String info;

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

class Response {
    private String info;

    Response(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
