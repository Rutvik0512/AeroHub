package com.cloud.aerohub.serviceImpl;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.dto.AirportPage;
import com.cloud.aerohub.entity.Airport;
import com.cloud.aerohub.repository.AirportRepository;
import com.cloud.aerohub.service.AirportService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AirportDto> getAllAirports() {
        log.debug("Fetching all airports from database");
        List<Airport> airports = airportRepository.findAll();
        log.info("Retrieved {} airports from database", airports.size());
        List<AirportDto> airportDtos = airports.stream()
                .map(this::getEntityToDto)
                .toList();
        log.debug("Successfully converted {} airport entities to DTOs", airportDtos.size());
        return airportDtos;
    }

    @Cacheable(
            cacheNames = "airportsFirstPage",
            keyGenerator = "airportKeyGen",
            condition = "#pageNumber == 0"
    )
    @Override
    @Transactional(readOnly = true)
    public AirportPage getAirportsByPaginationSort(int pageSize, int pageNumber, String sortField, String sortDirection, String search) {
        log.debug("Performing paginated airport query with parameters: pageSize={}, pageNumber={}, sortField={}, sortDirection={}, search={}",
                pageSize, pageNumber, sortField, sortDirection, search);

        Pageable pageable = (sortField != null && !sortField.isBlank())
                ? PageRequest.of(pageNumber, pageSize,
                Sort.by("desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC, sortField))
                : PageRequest.of(pageNumber, pageSize);

        log.debug("Created pageable with sort: {}", pageable.getSort());

        Page<Airport> airports = (search != null && !search.isBlank())
                                ? airportRepository.findByNameContainingIgnoreCase(search.trim(), pageable)
                                : airportRepository.findAll(pageable);

        log.info("Retrieved {} airports from database with pagination", airports.getTotalElements());

        return AirportPage.builder()
                .content(airports.getContent().stream().map(this::getEntityToDto).toList())
                .pageNumber(airports.getNumber())
                .totalPages(airports.getTotalPages())
                .totalElements(airports.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public AirportDto addAirport(AirportDto airportDto) {
        log.info("Adding new airport: {}", airportDto.getName());
        Airport airport = getDtoToEntity(airportDto);
        log.debug("Successfully converted DTO to entity");
        Airport savedAirport = airportRepository.save(airport);
        log.info("Successfully saved airport with ICAO: {} to database", savedAirport.getIcao());
        return getEntityToDto(airport);
    }

    public AirportDto getEntityToDto(Airport airport) {
        log.trace("Converting Airport entity to DTO: {}", airport.getIcao());
        return objectMapper.convertValue(airport, AirportDto.class);
    }

    public Airport getDtoToEntity(AirportDto airportDto) {
        log.trace("Converting Airport DTO to entity: {}", airportDto.getIcao());
        return objectMapper.convertValue(airportDto, Airport.class);
    }

    public AirportServiceImpl(AirportRepository airportRepository, ObjectMapper objectMapper) {
        this.airportRepository = airportRepository;
        this.objectMapper = objectMapper;
    }

}
