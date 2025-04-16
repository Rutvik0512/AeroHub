package com.cloud.aerohub.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportDto {
    @JsonProperty(value = "_key")
    @JsonAlias("id")
    private String id;
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
