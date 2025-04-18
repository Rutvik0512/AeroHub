package com.cloud.aerohub.config;

import com.cloud.aerohub.entity.Airport;
import com.cloud.aerohub.exception.ResourceNotFound;
import com.cloud.aerohub.repository.AirportRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private static final String csvPath = "/data/airports.csv";

    private final AirportRepository airportRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        if (isInitialized()){
            log.info("Airports data set already initialized");
            return;
        }

        log.info("Initializing airports data set");

        InputStream inputStream = getResourceAsStream();
        if (inputStream == null) {
            log.error("Unable to load airports.csv file");
            throw new ResourceNotFound("File not found: " + csvPath);
        }

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(reader);

            log.info("Loading airports data set");

            for (CSVRecord record : records) {
                try{
                    Airport airport = Airport.builder()
                            .icao(record.get(1))
                            .iata(record.get(2))
                            .name(record.get(3))
                            .city(record.get(4))
                            .state(record.get(5))
                            .country(record.get(6))
                            .elevation(Long.parseLong(record.get(7)))
                            .lat(Double.parseDouble(record.get(8)))
                            .lon(Double.parseDouble(record.get(9)))
                            .timezone(record.get(10))
                            .build();
                    log.trace("Saving airport: {}", airport);
                    airportRepository.save(airport);
                }catch (NumberFormatException e){
                    log.warn("Invalid elevation value: {}. Skipping record.", record.get(7));
                }

            }
            log.info("Total airports loaded: {}", airportRepository.count());
        }

    }

    public boolean isInitialized() {
        return airportRepository.count() > 0;
    }

    public InputStream getResourceAsStream() {
        return getClass().getResourceAsStream(csvPath);
    }

    DataLoader(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }
}
