package com.cloud.aerohub.service;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.dto.AirportPage;

import java.util.List;

public interface AirportService {

    List<AirportDto> getAllAirports();
    AirportPage getAirportsByPaginationSort(int pageSize, int pageNumber, String sortField, String sortDirection, String search);
    AirportDto addAirport(AirportDto airportDto);
}
