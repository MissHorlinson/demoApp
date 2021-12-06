package com.example.demo.controller;

import com.cat.common.entity.orderbook.DataTrackingType;
import com.example.demo.model.DataTypeTable;
import com.example.demo.repo.DataTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/datatype")
public class DataTypeController {

    private final DataTypeRepository dataTypeRepository;
    private Map<DataTrackingType, DataTrackingType> typeMap = new HashMap<>();

    public DataTypeController(DataTypeRepository dataTypeRepository) {
        this.dataTypeRepository = dataTypeRepository;

        createDataTypeMap();
    }

    @PutMapping("/addTypes")
    public void addToDB() {
        typeMap.forEach((typeKey, type) -> {
            DataTypeTable dataTypeTable = new DataTypeTable(type.name());

            log.warn(dataTypeTable.toString());

            dataTypeRepository.save(dataTypeTable);
        });
    }

    public void createDataTypeMap() {
        for (DataTrackingType type : DataTrackingType.values() ) {
            typeMap.put(type, type);
        }
    }
}
