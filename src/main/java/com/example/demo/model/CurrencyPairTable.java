package com.example.demo.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pair")
public class CurrencyPairTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currency_pair")
    private String currencyPair;

    @Column(name = "stringFormat")
    private String stringFormat;

    @Column(name = "bitfinexValue")
    private String bitfinexValue;

    @Column(name = "okexValue")
    private String okexValue;

    @Column(name = "bitfinexCurrencyPair")
    private String bitfinexCurrencyPair;

    @Column(name = "okexV3Value")
    private String okexV3Value;

    @Column(name = "okexSwapValue")
    private String okexSwapValue;

    @Column(name = "okexSpotValue")
    private String okexSpotValue;

    @Column(name = "bitmexValue")
    private String bitmexValue;

    @Column(name = "binanceValue")
    private String binanceValue;

    @Column(name = "huobiValue")
    private String huobiValue;

    public CurrencyPairTable(String currencyPair, String stringFormat, String bitfinexValue, String okexValue, String bitfinexCurrencyPair, String okexV3Value, String okexSwapValue, String okexSpotValue, String bitmexValue, String binanceValue, String huobiValue) {
        this.currencyPair = currencyPair;
        this.stringFormat = stringFormat;
        this.bitfinexValue = bitfinexValue;
        this.okexValue = okexValue;
        this.bitfinexCurrencyPair = bitfinexCurrencyPair;
        this.okexV3Value = okexV3Value;
        this.okexSwapValue = okexSwapValue;
        this.okexSpotValue = okexSpotValue;
        this.bitmexValue = bitmexValue;
        this.binanceValue = binanceValue;
        this.huobiValue = huobiValue;
    }

    @Override
    public String toString() {
        return "currencyPair: " + currencyPair +
        ", stringFormat: "  + stringFormat +
        ", bitfinexValue: "  + bitfinexValue +
        ", okexValue: " + okexValue +
        ", bitfinexCurrencyPair: " + bitfinexCurrencyPair +
        ", okexV3Value: " + okexV3Value +
        ", okexSwapValue: " + okexSwapValue +
        ", okexSpotValue: " + okexSpotValue +
        ", bitmexValue: " + bitmexValue +
        ", binanceValue: " + binanceValue +
        ", huobiValue: " + huobiValue;
    }
}
