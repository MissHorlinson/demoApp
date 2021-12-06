package com.example.demo.model;

import com.cat.common.entity.orderbook.DataTrackingType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "datatype")
public class DataTypeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String dataType;

    public DataTypeTable(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "DataType: " + dataType;
    }
}
