package com.edgar.javadailystockcrawling.mapper;

import com.edgar.javadailystockcrawling.dto.MarketIndex;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;



@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MarketIndexMapperTest {

    @Autowired
    MarketIndexMapper marketIndexMapper;

    @Test
    void selectAllMarketIndexList() {
        List<MarketIndex> list =  marketIndexMapper.selectAllMarketIndexList();
        list.forEach(System.out::println);
    }
}