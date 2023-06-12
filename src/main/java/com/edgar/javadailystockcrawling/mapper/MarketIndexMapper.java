package com.edgar.javadailystockcrawling.mapper;

import com.edgar.javadailystockcrawling.dto.MarketIndex;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MarketIndexMapper {
    List<MarketIndex> selectAllMarketIndexList();
}
