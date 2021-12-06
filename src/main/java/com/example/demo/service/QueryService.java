package com.example.demo.service;

import com.cat.client.RoundingServiceClient;
import com.cat.common.entity.Broker;
import com.cat.common.entity.CurrencyPair;
import com.cat.common.entity.config.DataType;
import com.cat.common.entity.orderbook.DataTrackingType;
import com.cat.common.entity.rounding.RoundingParametersConfiguration;

import com.example.demo.entity.BrokerTypeKey;
import com.example.demo.entity.CurrencyPairBrokerKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class QueryService {

    @Value("${signal.data}")
    private String signal;
    private final RoundingServiceClient roundingServiceClient;
    private Map<String, CurrencyPair> currencyPairMap = new HashMap<String, CurrencyPair>();
    private Map<CurrencyPair, Optional<RoundingParametersConfiguration>> valueMap = new HashMap<>();

    @Value("#{${brokerTypeMapping}}")
    private Map<Broker, List<String>> keysMap = new HashMap<>();  // промежуточная, хранит по брокеру тип и брокера для запроса
    private Map<Broker, BrokerTypeKey> paramsMap = new HashMap<>();

    private Map<CurrencyPairBrokerKey, String> testMap = new HashMap<>(); // хранит параметры собраные через generateParameters
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Value("${generateParamBrokerKey}")
    private Broker brokerKey;


    public QueryService(RoundingServiceClient roundingServiceClient) {
        this.roundingServiceClient = roundingServiceClient;
        createCurrencyPairMap();
    }


    @PostConstruct
    public void init() {
        keysMap.forEach((b, strings) -> {
            String broker = strings.get(0);
            String dataType = strings.get(1);
            if(validateBroker(broker) != null && validateDataType(dataType) != null) {
                BrokerTypeKey brokerTypeKey = new BrokerTypeKey(Broker.valueOf(broker), DataTrackingType.valueOf(dataType));
                paramsMap.put(b, brokerTypeKey);
                log.info("Key: {} -> value {}", b, brokerTypeKey.toString());
            } else
                log.info("Params not full, can not working correct with part data");
        });

        executor.scheduleWithFixedDelay(new CheckDB(), 0, 2, TimeUnit.MINUTES);

        String[] parts = getSignalParts(signal);
        log.info("Pair from signal: {}", getPairFromSignal(parts[parts.length - 1]));
        log.info("Broker from signal: {}", getBrokerFromSignal(parts[2]));
    }

    public void createCurrencyPairMap() {
        for(CurrencyPair p : CurrencyPair.values()) {
            currencyPairMap.put(p.name(), p);
        }

//        currencyPairMap.put(CurrencyPair.BTC_USD.name(), CurrencyPair.BTC_USD);
//        currencyPairMap.put(CurrencyPair.LTC_USD.name(), CurrencyPair.LTC_USD);
//        currencyPairMap.put(CurrencyPair.NEO_USD.name(), CurrencyPair.NEO_USD);
//        currencyPairMap.put(CurrencyPair.OMG_BTC.name(), CurrencyPair.OMG_BTC);
//        currencyPairMap.put(CurrencyPair.OMG_USD.name(), CurrencyPair.OMG_USD);
//        currencyPairMap.put(CurrencyPair.EOS_USD.name(), CurrencyPair.EOS_USD);
//        currencyPairMap.put(CurrencyPair.EOS_BTC.name(), CurrencyPair.EOS_BTC);
//        currencyPairMap.put(CurrencyPair.XRP_USD.name(), CurrencyPair.XRP_USD);
//        currencyPairMap.put(CurrencyPair.XRP_BTC.name(), CurrencyPair.XRP_BTC);
//        currencyPairMap.put(CurrencyPair.IOT_USD.name(), CurrencyPair.IOT_USD);
//        currencyPairMap.put(CurrencyPair.IOT_BTC.name(), CurrencyPair.IOT_BTC);
//        currencyPairMap.put(CurrencyPair.TRX_USD.name(), CurrencyPair.TRX_USD);
//        currencyPairMap.put(CurrencyPair.TRX_BTC.name(), CurrencyPair.TRX_BTC);
//        currencyPairMap.put(CurrencyPair.ZEC_BTC.name(), CurrencyPair.ZEC_BTC);
//        currencyPairMap.put(CurrencyPair.BCH_USD.name(), CurrencyPair.BCH_USD);
//        currencyPairMap.put(CurrencyPair.BCH_BTC.name(), CurrencyPair.BCH_BTC);
//        currencyPairMap.put(CurrencyPair.XMR_USD.name(), CurrencyPair.XMR_USD);
//        currencyPairMap.put(CurrencyPair.XMR_BTC.name(), CurrencyPair.XMR_BTC);
//        currencyPairMap.put(CurrencyPair.BTG_USD.name(), CurrencyPair.BTG_USD);
//        currencyPairMap.put(CurrencyPair.BTG_BTC.name(), CurrencyPair.BTG_BTC);
//        currencyPairMap.put(CurrencyPair.ETP_USD.name(), CurrencyPair.ETP_USD);
//        currencyPairMap.put(CurrencyPair.ETP_BTC.name(), CurrencyPair.ETP_BTC);
//        currencyPairMap.put(CurrencyPair.ETC_USD.name(), CurrencyPair.ETC_USD);
//        currencyPairMap.put(CurrencyPair.ETH_USD.name(), CurrencyPair.ETH_USD);
//        currencyPairMap.put(CurrencyPair.XLM_USD.name(), CurrencyPair.XLM_USD);

        log.info("map size " + currencyPairMap.size());
    }

    public class CheckDB implements Runnable {

        public CheckDB() {}

        @Override
        public void run() {

            testMap.clear();

            currencyPairMap.forEach((s, currencyPair) -> {
                Optional<RoundingParametersConfiguration>[] roundInfo = new Optional[2];
                int i = 0;

                for (BrokerTypeKey type:paramsMap.values()) {
                    roundInfo[i] = roundingServiceClient.getRounding(type.getBroker(), currencyPair, type.getType());
                    i++;
                }

                if(roundInfo[0].isPresent() && roundInfo[1].isPresent()){
                    CurrencyPairBrokerKey key = new CurrencyPairBrokerKey(currencyPair, brokerKey);
                    String param = generateParameters(roundInfo[0].get().getMinOrderValue(),
                            roundInfo[1].get().getRoundPrice(),
                            roundInfo[1].get().getRoundAmount(),
                            roundInfo[0].get().getRoundPrice(),
                            roundInfo[0].get().getRoundAmount());
                    testMap.put(key, param);
                }
            });

//            for (CurrencyPair pair : currencyPairMap.values()) {
//
//                Optional<RoundingParametersConfiguration>[] roundInfo = new Optional[2];
//                int i = 0;
//
//                for (BrokerTypeKey type:paramsMap.values()) {
//
//                    roundInfo[i] = roundingServiceClient.getRounding(type.getBroker(), pair, type.getType());
//                    i++;
//                }
//
//                if(roundInfo[0].isPresent() && roundInfo[1].isPresent()){
//                    CurrencyPairBrokerKey key = new CurrencyPairBrokerKey(pair, Broker.BINANCE);
//                    String param = generateParameters(roundInfo[0].get().getMinOrderValue(),
//                            roundInfo[1].get().getRoundPrice(),
//                            roundInfo[1].get().getRoundAmount(),
//                            roundInfo[0].get().getRoundPrice(),
//                            roundInfo[0].get().getRoundAmount());
//                    testMap.put(key, param);
//                }
//            }

            log.info("Map with fullParam size is " + testMap.size());

            for (CurrencyPairBrokerKey key :testMap.keySet()) {
                //log.info("Key: " + key.toString() + " value: " + testMap.get(key));
                System.out.println("FALCONX_SNAP_" + key.getBroker().name() + "_ARB_INF_" + key.getCurrencyPair().name().replace("_","") + ", generateParameters(" + testMap.get(key) + ")");
            }
        }


        private String generateParameters(BigDecimal minOrderValue, int round2BtcScaleValue, int round3BtcScaleValue, int roundOrderPriceValue, int roundOrderQuantityValue) {
            return "minOrderValue: " + minOrderValue + " round2: " + round2BtcScaleValue + " round3: " + round3BtcScaleValue +
                    " orderPrice: " + roundOrderPriceValue + " orderQuantity: " + roundOrderQuantityValue;
        }

    }

    private Broker validateBroker(String broker) {
        try {
            return Broker.valueOf(broker);
        } catch (Exception ex) {
            log.warn("Broker " + broker + " not exist");
            return null;
        }
    }

    public DataTrackingType validateDataType(String type) {
        try {
            return DataTrackingType.valueOf(type);
        } catch (Exception ex) {
            log.warn("DataType " + type + " not exist");
            return null;
        }
    }

    public CurrencyPair getPairFromSignal(String currency) {
        String pair2 = currency.substring(0, currency.length() - 3) + "_" + currency.substring(currency.length()-3);

        if(currencyPairMap.containsKey(pair2)) {
            return CurrencyPair.valueOf(pair2);
        } else {
            log.warn("Pair " + pair2 + " not exist");
            return null;
        }
    }

    public Broker getBrokerFromSignal(String broker) {
        try {
            return Broker.valueOf(broker);
        } catch (Exception ex) {
            log.warn("Broker {} not exist", broker);
            return null;
        }
    }

    public DataTrackingType getTypeFromSignal(String type) {
        try {
            return DataTrackingType.valueOf(type);
        } catch (Exception ex) {
            log.warn("DataTrackingType {} not exist", type);
            return null;
        }
    }

    public String[] getSignalParts(String signal) {
        return  signal.split("_");
    }
}
