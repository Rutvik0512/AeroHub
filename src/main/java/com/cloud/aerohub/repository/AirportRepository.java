package com.cloud.aerohub.repository;

import com.cloud.aerohub.entity.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {

    Page<Airport> findByNameContainingIgnoreCase(String search, Pageable pageable);
    @Query("SELECT DISTINCT a.state FROM Airport a WHERE a.state <> ''")
    List<String> findDistinctStateBy();

    @Query("SELECT DISTINCT a.timezone FROM Airport a")
    List<String> findDistinctTimezoneBy();;
}
