package com.springboot.fujitsu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains main business logic
 */
public class FeeCalculateService {

    private final String city;
    private final String vehicleType;
    private final double airTemperature;
    private final double windSpeed;
    private final String weatherPhenomenon;
    private final double finalFee;

    private static final double ATEF_LESS_THAN_MINUS_10 = 1.0;
    private static final double ATEF_BETWEEN_MINUS_10_AND_0 = 0.5;
    private static final double WSEF_BETWEEN_10_AND_20 = 0.5;
    private static final double WPEF_SNOW_OR_SLEET = 1.0;
    private static final double WPEF_RAIN = 0.5;

    private static final Logger logger = LoggerFactory.getLogger(FeeCalculateService.class);

    public FeeCalculateService(String city, String vehicleType, double airTemperature, double windSpeed, String weatherPhenomenon) {
        this.city = city.toLowerCase();
        this.vehicleType = vehicleType.toLowerCase();
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon.toLowerCase();
        this.finalFee = calculateExtraFee(calculateBaseFee());
    }

    public double getFinalFee() {
        return finalFee;
    }

    /**
     * Calculates the base fee based on courier location and vehicle type
     * @return calculated base fee
     */
    public double calculateBaseFee() {
        logger.info("Basic fee calculation is started");
        final IllegalArgumentException e = new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
        double bf = switch (city) {
            case "tallinn" -> switch (vehicleType) {
                case "car" -> 4.0;
                case "scooter" -> 3.5;
                case "bike" -> 3.0;
                default -> {
                    logger.error("An error occurred during the request: {}", e.getMessage(), e);
                    throw e;
                }

            };
            case "tartu" -> switch (vehicleType) {
                case "car" -> 3.5;
                case "scooter" -> 3.0;
                case "bike" -> 2.5;
                default -> {
                    logger.error("An error occurred during the request: {}", e.getMessage(), e);
                    throw e;
                }
            };
            case "pärnu" -> switch (vehicleType) {
                case "car" -> 3.0;
                case "scooter" -> 2.5;
                case "bike" -> 2.0;
                default -> {
                    logger.error("An error occurred during the request: {}", e.getMessage(), e);
                    throw e;
                }
            };
            default -> throw new IllegalArgumentException("Invalid city: " + city);
        };
        logger.info("Basic fee is calculated: " + bf);
        return bf;
    }

    /**
     * Calculates extra fee for the courier based on air temperature, wind speed and on weather phenomenon
     * @param bf base fee of the courier
     * @return final courier fee
     */
    public double calculateExtraFee(double bf) {
        double extraFee = bf;
        logger.info("Extra fee calculation is started");

        if (vehicleType.equals("scooter") || vehicleType.equals("bike")) {
            if (airTemperature < -10) { // Air temperature is less than -10̊ C, then ATEF = 1 €
                extraFee += ATEF_LESS_THAN_MINUS_10;
            } else if (airTemperature >= -10 && airTemperature <= 0) { // Air temperature is between -10̊ C and 0̊ C, then ATEF = 0,5 €
                extraFee += ATEF_BETWEEN_MINUS_10_AND_0;
            }

            IllegalArgumentException e = new IllegalArgumentException("Usage of selected vehicle type is forbidden: " + vehicleType);
            if (vehicleType.equals("bike")) {
                if (windSpeed > 20) { // In case of wind speed is greater than 20 m/s
                    logger.warn("WARNING!!! {}", e.getMessage(), e);
                    throw e;
                } else if (windSpeed >= 10) { // Wind speed is between 10 m/s and 20 m/s, then WSEF = 0,5 €
                    extraFee += WSEF_BETWEEN_10_AND_20;
                }
            }

            if (weatherPhenomenon.contains("snow") || weatherPhenomenon.contains("snowfall") || weatherPhenomenon.contains("sleet")) { // Weather phenomenon is related to snow or sleet, then WPEF = 1 €
                extraFee += WPEF_SNOW_OR_SLEET;
            } else if (weatherPhenomenon.contains("rain") || (weatherPhenomenon.contains("shower") && !weatherPhenomenon.contains("snow"))) { // Weather phenomenon is related to rain, then WPEF = 0,5 €
                extraFee += WPEF_RAIN;
            } else if (weatherPhenomenon.contains("glaze") || weatherPhenomenon.contains("hail") || weatherPhenomenon.contains("thunder")) {
                //In case the weather phenomenon is glaze, hail, or thunder
                logger.warn("WARNING!!! {}", e.getMessage(), e);
                throw e;
            }
        }
        logger.info("Final fee is calculated: " + extraFee);
        return extraFee;
    }
}
