package com.springboot.fujitsu;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class CronJobTest {

    private final WeatherRepository weatherRepository;

    public CronJobTest(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Test
    public void runCronJobTest() {
        weatherRepository.deleteAll(); // !NB all information from the database will be deleted
        Awaitility.await().atMost(1, TimeUnit.HOURS).until(() -> weatherRepository.count() > 0);

        assertFalse(weatherRepository.findByCityAndVehicleType("tallinn").isEmpty());
        assertFalse(weatherRepository.findByCityAndVehicleType("tartu").isEmpty());
        assertFalse(weatherRepository.findByCityAndVehicleType("pärnu").isEmpty());
    }
}
