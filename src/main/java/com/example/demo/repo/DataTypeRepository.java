package com.example.demo.repo;

import com.example.demo.model.DataTypeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTypeRepository extends JpaRepository<DataTypeTable, Long> {
}
