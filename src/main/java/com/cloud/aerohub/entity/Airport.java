package com.cloud.aerohub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {

    @Id
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


    public String toString() {
        return "Airport {" +
                "id=" + id +
                ", icao=" + icao +
                ", iata=" + iata +
                ", name=" + name +
                ", city=" + city +
                ", state=" + state +
                ", country=" + country +
                ", elevation=" + elevation +
                ", lat=" + lat +
                ", lon=" + lon +
                ", timezone=" + timezone +
                ", }";
    }
}
