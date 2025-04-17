package com.cloud.aerohub.controller;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.service.AirportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
@Tag(name = "Airport Api", description = "Airport CRUD operations with pagination and sorting")
public class AirportController {

    private final AirportService airportService;

    @GetMapping("/all")
    public ResponseEntity<List<AirportDto>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping()
    public ResponseEntity<Page<AirportDto>> getAirportsByPagination(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(airportService.getAirportsByPaginationSort(pageSize, pageNumber, sortField, sortDirection, search));
    }

    @PostMapping("/add")
    public ResponseEntity<AirportDto> addAirport(@RequestBody AirportDto airportDto) {
        AirportDto newlyCreatedDto = airportService.addAirport(airportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newlyCreatedDto);
    }

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }
}
