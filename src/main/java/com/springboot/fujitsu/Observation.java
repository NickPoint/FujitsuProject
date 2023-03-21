package com.springboot.fujitsu;


import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "observations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Observation {
    @XmlAttribute(name = "timestamp")
    private long timestamp;

    @XmlElement(name = "station")
    private List<WeatherStation> stations;

    public long getTimestamp() {
        return timestamp;
    }

    public List<WeatherStation> getStations() {
        return stations;
    }
}
