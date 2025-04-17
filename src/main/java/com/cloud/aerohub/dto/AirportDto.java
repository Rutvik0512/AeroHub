package com.cloud.aerohub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirportDto {
    private String icao;
    private String iata;
    private String name;
    private String city;
    private String state;
    private String country;
    private Long elevation;
    private Double lat;
    private Double lon;
    private String timezone;
}
