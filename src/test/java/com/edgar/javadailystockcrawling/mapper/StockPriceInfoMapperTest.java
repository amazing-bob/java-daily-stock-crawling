package com.edgar.javadailystockcrawling.mapper;

import com.edgar.javadailystockcrawling.dto.StockPriceInfo;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.util.List;


@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StockPriceInfoMapperTest {

    @Autowired
    StockPriceInfoMapper stockPriceInfoMapper;

    @Test
    void selectAllStockPriceInfoList() {
        List<StockPriceInfo> list = stockPriceInfoMapper.selectAllStockPriceInfoList();
        list.forEach(System.out::println);
    }
}