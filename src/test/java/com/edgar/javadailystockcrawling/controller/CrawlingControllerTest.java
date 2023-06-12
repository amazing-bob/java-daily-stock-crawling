package com.edgar.javadailystockcrawling.controller;

import com.edgar.javadailystockcrawling.dto.UpperLimitStocks;
import com.edgar.javadailystockcrawling.service.CrawlingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(CrawlingController.class)
class CrawlingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrawlingService crawlingService;

    @MockBean
    private UpperLimitStocks upperLimitStocks;

    @MockBean
    private List list;


    @Test
    @DisplayName("전체 크롤링 TEST")
    void crawlingAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/crawling")
                        .header("x-crawling-user", "buru"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}