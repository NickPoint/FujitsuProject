package com.springboot.fujitsu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherStation, Long> {
    @Query("SELECT station FROM WeatherStation station WHERE station.name = LOWER(:city) ORDER BY station.timestamp desc")
    List<WeatherStation> findByCityAndVehicleType(@Param("city") String city);
}
