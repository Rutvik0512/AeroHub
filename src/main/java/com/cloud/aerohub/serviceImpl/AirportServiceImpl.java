package com.cloud.aerohub.serviceImpl;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.entity.Airport;
import com.cloud.aerohub.repository.AirportRepository;
import com.cloud.aerohub.service.AirportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<AirportDto> getAirportsByPaginationSort(int pageSize, int pageNumber, String sortField, String sortDirection) {
        Pageable pageable = (sortField != null && !sortField.isBlank())
                ? PageRequest.of(pageNumber, pageSize,
                Sort.by("desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC, sortField))
                : PageRequest.of(pageNumber, pageSize);

        return airportRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::getEntityToDto)
                .toList();
    }


    public AirportDto getEntityToDto(Airport airport) {
        return objectMapper.convertValue(airport, AirportDto.class);
    }

    public AirportServiceImpl(AirportRepository airportRepository, ObjectMapper objectMapper) {
        this.airportRepository = airportRepository;
        this.objectMapper = objectMapper;
    }

}
