/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.resttemplate.parser;

import com.example.resttemplate.model.CardDetails;
import com.example.resttemplate.utils.Constants;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author kobe
 */
@RestController
public class BinFetcherController {

    @Value("${spring.application.name}")
    private String appName;

    private String result;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private CardDetails cardDetails;

    private HttpHeaders headers;
    private HttpEntity<String> entity;
    private ResponseEntity<String> response;

   // private static final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final Logger logger = LoggerFactory.getLogger(BinFetcherController.class); 

    @GetMapping("/cardnumber/{cardnumber}")
    public String validateBinList(@PathVariable String cardnumber) {

        try {

            // HttpHeaders
            headers = new HttpHeaders();

            headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
            // Request to return JSON format
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HttpEntity<String>: To get result as String.
            entity = new HttpEntity<>(headers);

            logger.info(appName + " Sending 'POST' request to URL : " + Constants.URL);

            // Send request with GET method, and Headers.
            response = restTemplate.exchange(Constants.URL + cardnumber,
                    HttpMethod.GET, entity, String.class);

            result = response.getBody();

            logger.info(appName + " Response from Bin Validator : " + result);

        } catch (Exception ex) {
            logger.error(appName + " Error Message : " + ex.getMessage());
            throw ex;
        }
        return result;
    }

    //option without headers
    @GetMapping("/bin/cardnumber/{cardnumber}")
    public String validateCardData(@PathVariable String cardnumber) {

        try {
            logger.info(appName + " Sending 'POST' request to URL : " + Constants.URL);

            result = restTemplate.getForObject(Constants.URL + cardnumber, String.class);

            logger.info(appName + " Response from Bin Validator : " + result);

        } catch (Exception ex) {
            logger.error(appName + " Error Message : " + ex.getMessage());
            throw ex;
        }
        return result;
    }

    //using web client builder
    @GetMapping("/bin/cardnumber/webclient/{cardnumber}")
    public CardDetails validateCard(@PathVariable String cardnumber) {
        try {
            cardDetails = webClientBuilder.build()
                    .get()
                    .uri(Constants.URL + cardnumber, CardDetails.class)
                    .retrieve()
                    .bodyToMono(CardDetails.class)
                    .block(); //converts to async

            logger.info(appName + " Sending 'POST' request to URL : " + Constants.URL);

            logger.info(appName + " Response from Bin Validator : " + cardDetails.toString());

        } catch (HttpClientErrorException ex) {
            logger.error(appName + " Error Message : " + ex.getMessage());
            throw ex;
        }
        return cardDetails;
    }
}
