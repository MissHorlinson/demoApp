package com.example.demo.repo;

import com.example.demo.model.BrokerTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerRepository extends JpaRepository<BrokerTable, Long> {
}
