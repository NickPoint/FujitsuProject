package com.springboot.fujitsu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Cronjob for getting information from the endpoint and adding it to the database
 */
@Component
public class CronJob {
    private final RestTemplate restTemplate;
    private final WeatherRepository weatherRepository;
    private final Set<String> neededStations = new HashSet<>() {{
        add("Tallinn-Harku");
        add("Tartu-Tõravere");
        add("Pärnu");
    }};
    private static final Logger logger = LoggerFactory.getLogger(CronJob.class);


    public CronJob(RestTemplate restTemplate, WeatherRepository weatherRepository) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
    }

    /**
     * Makes a request to the weather portal and get a response, runs every hour at 15 minutes past the hour
     */
    @Scheduled(cron = "0 15 * * * *")
    public void runJob() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Observation.class); // Create a JAXB context and unmarshaller to parse XML response
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // Send a request to the weather portal and get a response
            String response = restTemplate.getForObject("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php", String.class);
            if (response != null) {
                // If the response is not null, parse the XML response
                StringReader reader = new StringReader(response);
                Observation observation = (Observation) unmarshaller.unmarshal(reader);
                long timestamp = observation.getTimestamp();
                List<WeatherStation> stations = observation.getStations();
                // Save the weather data to the database
                for (WeatherStation station : stations) {
                    if (neededStations.contains(station.getName())) {
                        station.setTimestamp(timestamp);
                        String name = station.getName().replaceAll("-.*", "");
                        station.setName(name.toLowerCase());
                        weatherRepository.save(station);
                        logger.info("Weather station '" + station.getName() + "' is saved to the database");
                    }
                }
            }
            else logger.warn("Cannot set connection to the weather portal of the Estonian Environment Agency");

        } catch (JAXBException e) {
            logger.error("Error while parsing weather data: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
