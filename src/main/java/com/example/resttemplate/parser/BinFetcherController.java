/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.resttemplate.parser;

import com.example.resttemplate.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
