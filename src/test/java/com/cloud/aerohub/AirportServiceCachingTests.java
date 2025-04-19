package com.cloud.aerohub;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.entity.Airport;
import com.cloud.aerohub.service.AirportService;
import com.cloud.aerohub.repository.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

@EnableCaching
@SpringBootTest
class AirportServiceCachingTests {

    @Autowired
    private AirportService airportService;

    @MockitoBean
    private AirportRepository airportRepository;

    @BeforeEach
    void setUp() {
        reset(airportRepository);
    }

    @Test
    void testGetStates_IsCached() {
        when(airportRepository.findDistinctStateBy()).thenReturn(List.of("CA", "TX"));

        List<String> first = airportService.getStates();
        assertThat(first).containsExactly("CA", "TX");
        verify(airportRepository, times(1)).findDistinctStateBy();

        List<String> second = airportService.getStates();
        assertThat(second).containsExactly("CA", "TX");
        verifyNoMoreInteractions(airportRepository);
    }

    @Test
    void testGetTimezones_IsCached() {
        when(airportRepository.findDistinctTimezoneBy()).thenReturn(List.of("PST", "EST"));

        List<String> first = airportService.getTimezones();
        assertThat(first).containsExactly("PST", "EST");
        verify(airportRepository, times(1)).findDistinctTimezoneBy();

        List<String> second = airportService.getTimezones();
        assertThat(second).containsExactly("PST", "EST");
        verifyNoMoreInteractions(airportRepository);
    }

    @Test
    void testCacheEvicted_OnAddAirport_forStates() {
        when(airportRepository.findDistinctStateBy()).thenReturn(List.of("CA", "TX"));

        airportService.getStates();
        verify(airportRepository, times(1)).findDistinctStateBy();

        when(airportRepository.save(any(Airport.class))).thenReturn(new Airport());
        airportService.addAirport(createAirportDto());

        airportService.getStates();
        verify(airportRepository, times(2)).findDistinctStateBy();
    }

    @Test
    void testCacheEvicted_OnAddAirport_forTimezones() {
        when(airportRepository.findDistinctTimezoneBy()).thenReturn(List.of("PST", "EST"));

        airportService.getTimezones();
        verify(airportRepository, times(1)).findDistinctTimezoneBy();

        when(airportRepository.save(any(Airport.class))).thenReturn(new Airport());
        airportService.addAirport(createAirportDto());

        airportService.getTimezones();
        verify(airportRepository, times(2)).findDistinctTimezoneBy();
    }

    private AirportDto createAirportDto() {
        return new AirportDto( "KATL","", "Hartsfield-Jackson Atlanta International", "Atlanta", "GA", "USA", 1026L, 33.6367, -84.4281, "America/New_York");
    }
}