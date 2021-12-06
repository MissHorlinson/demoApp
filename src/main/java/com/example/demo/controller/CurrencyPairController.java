package com.example.demo.controller;

import com.cat.common.entity.CurrencyPair;
import com.example.demo.model.BrokerTable;
import com.example.demo.model.CurrencyPairTable;
import com.example.demo.repo.CurrencyPairRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/pair")
public class CurrencyPairController {

    private final CurrencyPairRepository currencyPairRepository;
    private Map<CurrencyPair, CurrencyPair> pairMap = new HashMap<>();

    @Autowired
    public CurrencyPairController(CurrencyPairRepository currencyPairRepository) {
        this.currencyPairRepository = currencyPairRepository;

        createCurrencyPairMap();
    }

    @PutMapping("/addPairs")
    public void addToDB() {
        pairMap.forEach((pairKey, pair) -> {
            CurrencyPairTable currencyPairTable = new CurrencyPairTable(pair.name(),
                                                                        pair.getStringFormat(),
                                                                        pair.getBitfinexValue(),
                                                                        pair.getOkexValue(),
                                                                        pair.getBitfinexCurrencyPair(),
                                                                        pair.getOkexV3Value(),
                                                                        pair.getOkexSwapValue(),
                                                                        pair.getOkexSpotValue(),
                                                                        pair.getBitmexValue(),
                                                                        pair.getBinanceValue(),
                                                                        pair.getHuobiValue());

            log.warn(currencyPairTable.toString());

            currencyPairRepository.save(currencyPairTable);
        });
    }


    public void createCurrencyPairMap() {
        for (CurrencyPair p : CurrencyPair.values()) {
            pairMap.put(p, p);
        }
    }
}
