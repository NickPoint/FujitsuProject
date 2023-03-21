package com.springboot.fujitsu;// Import relevant packages
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WeatherControllerTest {

    private final WeatherRepository weatherRepository = mock(WeatherRepository.class);

    private final WeatherController weatherController = new WeatherController(weatherRepository);

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();

    @Test
    public void calculateBaseFeeTest() throws Exception {
        String city = "tallinn";
        String vehicle = "car";
        String expectedResponse = "Final fee is: 4.0";

        List<WeatherStation> weatherStations = new ArrayList<>();
        weatherStations.add(new WeatherStation(city, "101010", 10.0, 5.0, "Clear"));
        when(weatherRepository.findByCityAndVehicleType(city)).thenReturn(weatherStations);

        mockMvc.perform(get("/fee")
                        .param("city", city)
                        .param("vehicle", vehicle))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void calculateExtraFeeTest() throws Exception {
        String city = "tartu";
        String vehicle = "bike";
        String expectedResponse = "Final fee is: 4.0";

        List<WeatherStation> weatherStations = new ArrayList<>();
        weatherStations.add(new WeatherStation(city, "101010", -2.1, 4.7, "Light snow shower"));
        when(weatherRepository.findByCityAndVehicleType(city)).thenReturn(weatherStations);

        mockMvc.perform(get("/fee")
                        .param("city", city)
                        .param("vehicle", vehicle))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

}