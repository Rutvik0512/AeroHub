package com.cloud.aerohub.controller;

import com.cloud.aerohub.dto.AirportDto;
import com.cloud.aerohub.service.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
public class AirportController {

    private final AirportService airportService;

    @GetMapping("/all")
    public ResponseEntity<List<AirportDto>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping()
    public ResponseEntity<List<AirportDto>> getAirportsByPagination(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection) {
        return ResponseEntity.ok(airportService.getAirportsByPaginationSort(pageSize, pageNumber, sortField, sortDirection ));
    }

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }
}
