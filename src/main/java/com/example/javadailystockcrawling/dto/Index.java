package com.example.javadailystockcrawling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent()
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Index {
    private String name;
    private String value;
    private String updown;
    private String rate;



    @Override
    public String toString() {
        return "%s\t\t%s  |  %s  |  %s".formatted(this.name, this.value, this.updown, (this.rate == null ? "" : this.rate));
    }
}
