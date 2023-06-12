package com.edgar.javadailystockcrawling.mapper;

import com.edgar.javadailystockcrawling.dto.StockPriceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockPriceInfoMapper {
    @Select("SELECT * FROM stock_price_info")
    List<StockPriceInfo> selectAllStockPriceInfoList();
}