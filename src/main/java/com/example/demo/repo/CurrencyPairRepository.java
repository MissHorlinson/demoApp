package com.example.demo.repo;

import com.example.demo.model.CurrencyPairTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyPairRepository extends JpaRepository<CurrencyPairTable, Long> {
}
