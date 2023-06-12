package com.edgar.javadailystockcrawling.repository;

import com.edgar.javadailystockcrawling.dto.StockPriceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockPriceInfo, String> {

}
