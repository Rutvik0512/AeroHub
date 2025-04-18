package com.cloud.aerohub.repository;

import com.cloud.aerohub.entity.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {

    Page<Airport> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
