package com.example.demo.model;

import com.cat.common.entity.Broker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "broker")
public class BrokerTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "broker")
    private String broker;

    @Column(name = "stringType")
    private String stringType;

    public BrokerTable(String broker, String stringType) {
        this.broker = broker;
        this.stringType = stringType;
    }

    @Override
    public String toString() {
        return "broker: " + broker +
                ", stringType: " + stringType;
    }
}
