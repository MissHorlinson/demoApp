package com.example.demo.entity;

import com.cat.common.entity.Broker;
import com.cat.common.entity.CurrencyPair;
import com.cat.common.entity.orderbook.DataTrackingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyPairBrokerKey {
    private CurrencyPair currencyPair;
    private Broker broker;


    @Override
    public String toString(){
        return currencyPair + " " + broker;
    }
}
