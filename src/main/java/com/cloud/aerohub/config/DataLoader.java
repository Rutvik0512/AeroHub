package com.cloud.aerohub.config;

import com.cloud.aerohub.entity.Airport;
import com.cloud.aerohub.exception.ResourceNotFound;
import com.cloud.aerohub.repository.AirportRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


@Component
public class DataLoader implements CommandLineRunner {

    private static final String csvPath = "/data/airports.csv";
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final AirportRepository airportRepository;

    @Override
    public void run(String... args) throws Exception {
        InputStream inputStream = getResourceAsStream();
        if (inputStream == null) {
            logger.error("Unable to load airports.csv file");
            throw new ResourceNotFound("File not found: " + csvPath);
        }

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(reader);

            logger.info("Loading airports data set");

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
                    logger.debug("Saving airport: {}", airport);
                    airportRepository.save(airport);
                }catch (NumberFormatException e){
                    logger.warn("Invalid elevation value: {}. Skipping record.", record.get(7));
                }

            }

            logger.info("Total airports loaded: {}", airportRepository.count());
        }

    }

    public InputStream getResourceAsStream() {
        return getClass().getResourceAsStream(csvPath);
    }

    DataLoader(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }
}
