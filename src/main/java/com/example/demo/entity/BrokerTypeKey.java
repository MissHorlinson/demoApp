package com.example.demo.entity;

import com.cat.common.entity.Broker;
import com.cat.common.entity.orderbook.DataTrackingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrokerTypeKey {
    private Broker broker;
    private DataTrackingType type;

    @Override
    public String toString() {
        return "Broker: " + broker +" type: " + type;
    }
}
