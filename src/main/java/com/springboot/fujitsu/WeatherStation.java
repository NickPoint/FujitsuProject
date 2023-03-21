package com.springboot.fujitsu;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "station")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeatherStation {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private @Id Long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "wmocode")
    private String wmoCode;
    @XmlElement(name = "airtemperature")
    private double airTemperature;
    @XmlElement(name = "windspeed")
    private double windSpeed;
    @XmlElement(name = "phenomenon")
    private String weatherPhenomenon;
    private long timestamp;

    public WeatherStation(String name, String wmoCode, double airTemperature, double windSpeed, String weatherPhenomenon) {
        this.name = name;
        this.wmoCode = wmoCode;
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon;
    }

    public WeatherStation() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getAirTemperature() {
        return airTemperature;
    }
    public double getWindSpeed() {
        return windSpeed;
    }
    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "WeatherStation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", wmoCode='" + wmoCode + '\'' +
                ", airTemperature=" + airTemperature +
                ", windSpeed=" + windSpeed +
                ", weatherPhenomenon='" + weatherPhenomenon + '\'' +
                ", timestamp=" + timestamp +
                '}' + "\n";
    }
}
