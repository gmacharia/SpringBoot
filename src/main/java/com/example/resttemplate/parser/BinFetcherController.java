/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.resttemplate.parser;

import com.example.resttemplate.utils.Constants;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author kobe
 */
@RestController
public class BinFetcherController {

    @Value("${spring.application.name}")
    String appName;

    String result;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/cardnumber/{cardnumber}")
    public String validateBinList(@PathVariable String cardnumber) {

        HttpHeaders headers;
        RestTemplate restTemplate;
        HttpEntity<String> entity;
        ResponseEntity<String> response;
        try {

            // HttpHeaders
            headers = new HttpHeaders();

            headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
            // Request to return JSON format
            headers.setContentType(MediaType.APPLICATION_JSON);

            // HttpEntity<String>: To get result as String.
            entity = new HttpEntity<>(headers);

            // RestTemplate
            restTemplate = new RestTemplate();
            logger.debug(appName + " Sending 'POST' request to URL : " + Constants.URL);

            // Send request with GET method, and Headers.
            response = restTemplate.exchange(Constants.URL + cardnumber,
                    HttpMethod.GET, entity, String.class);

            result = response.getBody();

            logger.debug(appName + " Response from Bin Validator : " + result);
        } catch (Exception ex) {
            logger.error(appName + " Error Message : " + ex.getMessage());
        }
        return result;
    }
    
    //option without headers
    @GetMapping("/bin/cardnumber/{cardnumber}")
    public String validateCardData(@PathVariable String cardnumber) {

        RestTemplate restTemplate;
        try {
            restTemplate = new RestTemplate();

            logger.debug(appName + " Sending 'POST' request to URL : " + Constants.URL);

            result = restTemplate.getForObject(Constants.URL + cardnumber, String.class);

            logger.debug(appName + " Response from Bin Validator : " + result);
        } catch (Exception ex) {
            logger.error(appName + " Error Message : " + ex.getMessage());
        }
        return result;
    }
}
