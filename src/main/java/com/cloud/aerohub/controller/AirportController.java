package com.cloud.aerohub.controller;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.dto.AirportPage;
import com.cloud.aerohub.service.AirportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/airports")
@Tag(name = "Airport Api", description = "Airport CRUD operations with pagination and sorting")
public class AirportController {
    
    private final AirportService airportService;

    @GetMapping("/all")
    public ResponseEntity<List<AirportDto>> getAllAirports() {
        log.info("Request received to get all airports");
        List<AirportDto> airports = airportService.getAllAirports();
        log.info("Returning {} airports", airports.size());
        return ResponseEntity.ok(airports);
    }

    @GetMapping()
    public ResponseEntity<AirportPage> getAirportsByPagination(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String search) {
        log.info("Pagination request received: page={}, size={}, sort={}, direction={}, search={}",
                pageNumber, pageSize, sortField, sortDirection, search);
        AirportPage result = airportService.getAirportsByPaginationSort(
                pageSize, pageNumber, sortField, sortDirection, search);
        log.info("Returning paginated result with {} airports", result.getContent().size());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    public ResponseEntity<AirportDto> addAirport(@RequestBody AirportDto airportDto) {
        log.info("Request to add new airport: {}", airportDto.getName());
        AirportDto newlyCreatedDto = airportService.addAirport(airportDto);
        log.info("Successfully created airport with ID: {}", newlyCreatedDto.getIcao());
        return ResponseEntity.status(HttpStatus.CREATED).body(newlyCreatedDto);
    }

    @GetMapping("/states")
    public ResponseEntity<List<String>> getStates() {
        log.info("Request to get all states");
        List<String> states = airportService.getStates();
        log.info("Returning {} states", states.size());
        return ResponseEntity.ok(states);
    }

    @GetMapping("/timezones")
    public ResponseEntity<List<String>> getTimezones() {
        log.info("Request to get all timezones");
        List<String> timezones = airportService.getTimezones();
        log.info("Returning {} timezones", timezones.size());
        return ResponseEntity.ok(timezones);
    }

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }
}