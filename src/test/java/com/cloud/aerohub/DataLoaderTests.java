package com.cloud.aerohub;

import com.cloud.aerohub.config.DataLoader;
import com.cloud.aerohub.entity.Airport;
import com.cloud.aerohub.exception.ResourceNotFound;
import com.cloud.aerohub.repository.AirportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@ExtendWith(MockitoExtension.class)
public class DataLoaderTests {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @Test
    void testRun_SuccessfullyLoadsData() throws Exception {

        InputStream mockInputStream = new ByteArrayInputStream((
                "id,icao,iata,name,city,state,country,elevation,lat,lon,tz\n" +
                        "KATL,KATL,Hartsfield-Jackson Atlanta International,Atlanta,GA,USA,1026,33.6367,-84.4281,America/New_York"
        ).getBytes());

        DataLoader spyDataLoader = Mockito.spy(dataLoader);
        Mockito.doReturn(mockInputStream).when(spyDataLoader).getResourceAsStream();

        spyDataLoader.run();
        Mockito.verify(airportRepository, Mockito.times(1)).save(Mockito.any(Airport.class));
    }

    @Test
    void testRun_FileNotFound_ThrowsException() {

        DataLoader spyDataLoader = Mockito.spy(dataLoader);
        Mockito.doReturn(null).when(spyDataLoader).getResourceAsStream();

        RuntimeException exception = Assertions.assertThrows(ResourceNotFound.class, spyDataLoader::run);
        Assertions.assertEquals("File not found: /data/airports.csv", exception.getMessage());
    }

    @Test
    void testRun_NumberFormatException() throws Exception{

        InputStream mockInputStream = new ByteArrayInputStream((
                "id,icao,iata,name,city,state,country,elevation,lat,lon,tz\n" +
                        "KATL,KATL,Hartsfield-Jackson Atlanta International,Atlanta,Georgia,USA,invalid,33.6367,-84.4281,America/New_York"
        ).getBytes());

        DataLoader spyDataLoader = Mockito.spy(dataLoader);
        Mockito.doReturn(mockInputStream).when(spyDataLoader).getResourceAsStream();

        spyDataLoader.run();
        Mockito.verify(airportRepository, Mockito.times(0)).save(Mockito.any(Airport.class));
    }
}
