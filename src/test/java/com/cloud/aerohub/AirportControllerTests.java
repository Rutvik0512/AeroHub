package com.cloud.aerohub;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.entity.Airport;
import com.cloud.aerohub.repository.AirportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AirportControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        airportRepository.deleteAll();
    }

    @Test
    public void getAll_noAirport_returnsEmptyList() throws Exception {
        this.mockMvc.perform(get("/api/v1/airports/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    public void getAll_airport_returnsAirportList() throws Exception {

        List<Airport> airports = Arrays.asList(
                createAirport("1", "KATL", "ATL", "Hartsfield-Jackson Atlanta International", "Atlanta", "GA", "USA", 1026L, 33.6367, -84.4281, "America/New_York"),
                createAirport("2", "KLAX", "LAX", "Los Angeles International", "Los Angeles", "CA", "USA", 125L, 33.9416, -118.4085, "America/Los_Angeles")
        );

        airportRepository.saveAll(airports);

        MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/airports/all"))
                .andReturn().getResponse();

        List<AirportDto> airportDtos = objectMapper.readValue(response.getContentAsString(), new com.fasterxml.jackson.core.type.TypeReference<List<AirportDto>>(){});

        assertThat(response.getStatus(), is(200));
        assertThat(airportDtos.size(), is(2));
    }

    public Airport createAirport(String id, String name, String country, String city, String state, String icao, String iata, Long elevation, Double lat, Double lon, String timezone) {
        return  new Airport(id, icao, iata, name, city, state, country, elevation, lat, lon, timezone);
    }

}
