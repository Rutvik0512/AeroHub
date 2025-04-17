package com.cloud.aerohub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(indexes ={
        @Index(name = "idx_name", columnList = "name"),
})
public class Airport {

    @Id
    private String icao;
    private String iata;

    @NotNull
    private String name;

    private String city;
    private String state;

    @NotNull
    private String country;

    @NotNull
    private Long elevation;

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @NotNull
    private String timezone;


    public String toString() {
        return "Airport {" +
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
