package com.cloud.aerohub.service;

import com.cloud.aerohub.dto.AirportDto;

import java.util.List;

public interface AirportService {

    List<AirportDto> getAllAirports();
    List<AirportDto> getAirportsByPaginationSort(int pageSize, int pageNumber, String sortField, String sortDirection, String search);
    AirportDto addAirport(AirportDto airportDto);
}
