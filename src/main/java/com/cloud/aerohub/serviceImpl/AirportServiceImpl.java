package com.cloud.aerohub.serviceImpl;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.entity.Airport;
import com.cloud.aerohub.repository.AirportRepository;
import com.cloud.aerohub.service.AirportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<AirportDto> getAllAirports() {
        List<Airport> airports = airportRepository.findAll();
        return airports.stream()
                .map(this::getEntityToDto)
                .toList();
    }

    @Cacheable(
            cacheNames = "airportsFirstPage",
            keyGenerator = "airportKeyGen",
            condition = "#pageNumber == 0"
    )
    @Override
    public Page<AirportDto> getAirportsByPaginationSort(int pageSize, int pageNumber, String sortField, String sortDirection, String search) {
        Pageable pageable = (sortField != null && !sortField.isBlank())
                ? PageRequest.of(pageNumber, pageSize,
                Sort.by("desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC, sortField))
                : PageRequest.of(pageNumber, pageSize);

        Page<Airport> airports = (search != null && !search.isBlank())
                                ? airportRepository.findByNameContainingIgnoreCase(search.trim(), pageable)
                                : airportRepository.findAll(pageable);

        return airports.map(this::getEntityToDto);
    }

    @Override
    public AirportDto addAirport(AirportDto airportDto) {
        Airport airport = getDtoToEntity(airportDto);
        airportRepository.save(airport);
        return getEntityToDto(airport);
    }

    public AirportDto getEntityToDto(Airport airport) {
        return objectMapper.convertValue(airport, AirportDto.class);
    }

    public Airport getDtoToEntity(AirportDto airportDto) {
        return objectMapper.convertValue(airportDto, Airport.class);
    }

    public AirportServiceImpl(AirportRepository airportRepository, ObjectMapper objectMapper) {
        this.airportRepository = airportRepository;
        this.objectMapper = objectMapper;
    }

}
