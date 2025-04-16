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

        List<Airport> airports = createAirports();
        airportRepository.saveAll(airports);

        MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/airports/all"))
                .andReturn().getResponse();

        List<AirportDto> airportDtos = objectMapper.readValue(response.getContentAsString(), new com.fasterxml.jackson.core.type.TypeReference<List<AirportDto>>(){});

        assertThat(response.getStatus(), is(200));
        assertThat(airportDtos.size(), is(airports.size()));
    }

    @Test
    public void getAll_paginated_returnsUnsortedAirportList() throws Exception{
        List<Airport> airports = createAirports();
        airportRepository.saveAll(airports);

        MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/airports")
                        .param("pageSize", "3")
                        .param("pageNumber", "0"))
                        .andReturn().getResponse();

        List<AirportDto> airportDtos = objectMapper.readValue(response.getContentAsString(), new com.fasterxml.jackson.core.type.TypeReference<List<AirportDto>>(){});

        assertThat(response.getStatus(), is(200));
        assertThat(airportDtos.size(),is(3));
    }

    @Test
    public void getAll_paginated_sortedAirportList() throws Exception{
        List<Airport> airports = createAirports();
        airportRepository.saveAll(airports);

        MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/airports")
                        .param("pageSize", "4")
                        .param("pageNumber", "0")
                        .param("sortField", "elevation")
                        .param("sortOrder", "asc"))
                        .andReturn().getResponse();

        List<AirportDto> airportDtos = objectMapper.readValue(response.getContentAsString(),new com.fasterxml.jackson.core.type.TypeReference<List<AirportDto>>(){});

        assertThat(response.getStatus(), is(200));
        assertThat(airportDtos.size(),is(4));

        for (int i = 0; i < airportDtos.size() -1 ; i++) {
            assert(airportDtos.get(i).getElevation() < airportDtos.get(i+1).getElevation());
        }

    }

    private List<Airport> createAirports() {
        return Arrays.asList(
                new Airport("KATL", "KATL","", "Hartsfield-Jackson Atlanta International", "Atlanta", "GA", "USA", 1026L, 33.6367, -84.4281, "America/New_York"),
                new Airport("KLAX", "KLAX","", "Los Angeles International", "Los Angeles", "California", "USA", 125L, 33.9416, -118.4085, "America/Los_Angeles"),
                new Airport("KORD", "KORD","", "O'Hare International Airport", "Chicago", "Indiana", "USA", 672L, 41.9742, -87.9073, "America/Chicago"),
                new Airport("KDFW", "KDFW","", "Dallas/Fort Worth International Airport", "Dallas", "TX", "USA", 607L, 32.8968, -97.0379, "America/Chicago"),
                new Airport("KDEN", "KDEN","", "Denver International Airport", "Denver", "CO", "USA", 543L, 39.8561, -104.6737, "America/Denver"),
                new Airport("00AK","00AK","","Lowell Field","Anchor Point","Alaska","US",450L,59.94919968,-151.695999146,"America/Anchorage")
        );
    }

}
