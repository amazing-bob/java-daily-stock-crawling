package com.example.javadailystockcrawling.repository;

import com.example.javadailystockcrawling.dto.MarketIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketIndexRepository extends JpaRepository<MarketIndex, String> {

}
