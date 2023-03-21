package com.springboot.fujitsu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This is a REST controller class for handling HTTP GET requests at the "/fee" endpoint.
 */
@RestController
public class WeatherController {
    private final WeatherRepository weatherRepository; // allow us to talk with the database

    public WeatherController(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    /**
     * A single endpoint mapping for the "/fee" endpoint
     * Query should be like this: <a href="http://localhost:8080/fee?city=tallinn&vehicle=car">...</a>, where instead of tallinn and car you can write your parameters.
     * NB! The localhost port may vary from the example
     * @param city given city
     * @param vehicle given vehicle type
     * @return final fee of a courier
     */
    @GetMapping("/fee")
    public String calculateDeliveryFee (@RequestParam("city") String city, @RequestParam("vehicle") String vehicle) {
        logger.info("GET request for fee calculation");
        logger.info("Given city: " + city + "; Given vehicle type: " + vehicle);
        List<WeatherStation> weatherStations = weatherRepository.findByCityAndVehicleType(city);
        if (weatherStations.isEmpty()) {
            NoSuchElementException e = new NoSuchElementException("Weather station for city of '" + city + "' doesn't been found");
            logger.error("An error occurred during the request: {}", e.getMessage(), e);
            throw e;
        }
        WeatherStation weatherStation = weatherStations.get(0);
        FeeCalculateService feeCalculateService = new FeeCalculateService(city, vehicle, weatherStation.getAirTemperature(), weatherStation.getWindSpeed(), weatherStation.getWeatherPhenomenon());
        return "Final fee is: " + feeCalculateService.getFinalFee();
    }


}
