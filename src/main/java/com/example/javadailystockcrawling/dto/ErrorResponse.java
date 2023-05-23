package com.example.javadailystockcrawling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String stutusCode;
    private String requestUrl;
    private String code;
    private String message;
    private String resultCode;
    private List<Error> errorList;

}
