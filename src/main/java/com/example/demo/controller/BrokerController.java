package com.example.demo.controller;


import com.cat.common.entity.Broker;
import com.example.demo.model.BrokerTable;
import com.example.demo.repo.BrokerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/broker")
public class BrokerController {

    private final BrokerRepository brokerRepository;
    private Map<Broker, Broker> brokerMap = new HashMap<>();

    @Autowired
    public BrokerController(BrokerRepository brokerRepository) {
        this.brokerRepository = brokerRepository;

        createBrokerTable();
    }


    @PutMapping("/addBrokers")
    public void addToDB() {
        brokerMap.forEach((brokerKey, broker) -> {
            BrokerTable brokerTable = new BrokerTable(broker.name(), broker.getStringType());

            log.warn(brokerTable.toString());

            brokerRepository.save(brokerTable);
        });
    }


    public void createBrokerTable() {
        for (Broker b: Broker.values()) {
            brokerMap.put(b, b);
        }
    }
}
