package com.springboot.fujitsu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BusinessLogicTests {
    @Test
    void calculateBaseFee() {
        assertEquals(4.0, new FeeCalculateService("tallinn", "car", 0 ,0, "").calculateBaseFee());
        assertEquals(4.0, new FeeCalculateService("TaLLinn", "CAr", 0 ,0, "").calculateBaseFee());
        assertEquals(3.5, new FeeCalculateService("tallinn", "scooter", 0 ,0, "").calculateBaseFee());
        assertEquals(3, new FeeCalculateService("tallinn", "bike", 0 ,0, "").calculateBaseFee());
        assertEquals(3.5, new FeeCalculateService("tartu", "car", 0 ,0, "").calculateBaseFee());
        assertEquals(3, new FeeCalculateService("tartu", "scooter", 0 ,0, "").calculateBaseFee());
        assertEquals(3, new FeeCalculateService("TarTu", "scOOter", 0 ,0, "").calculateBaseFee());
        assertEquals(2.5, new FeeCalculateService("tartu", "bike", 0 ,0, "").calculateBaseFee());
        assertEquals(3, new FeeCalculateService("pärnu", "car", 0 ,0, "").calculateBaseFee());
        assertEquals(2.5, new FeeCalculateService("pärnu", "scooter", 0 ,0, "").calculateBaseFee());
        assertEquals(2, new FeeCalculateService("pärnu", "bike", 0 ,0, "").calculateBaseFee());
        assertEquals(2, new FeeCalculateService("PÄRNU", "BIKE", 0 ,0, "").calculateBaseFee());
    }
    @Test
    void calculateExtraFee() {
        assertEquals(4.0, new FeeCalculateService("TARTU", "BIKE", -2.1, 4.7, "Light snow shower").calculateExtraFee(2.5));
        assertEquals(1, new FeeCalculateService("TARTU", "BIKE", -11, 0, "").calculateExtraFee(0));
        assertEquals(0.5, new FeeCalculateService("TARTU", "scooter", -5, 0, "").calculateExtraFee(0));
        assertEquals(0, new FeeCalculateService("Tallinn", "car", -11, 0, "").calculateExtraFee(0));
        assertEquals(1, new FeeCalculateService("Tallinn", "scooter", -11, 0, "").calculateExtraFee(0));
        assertEquals(0.5, new FeeCalculateService("pÄrnu", "bike", 5, 15, "").calculateExtraFee(0));
        assertEquals(0, new FeeCalculateService("pärnu", "car", 20, 15, "").calculateExtraFee(0));
        assertEquals(0, new FeeCalculateService("TaLLinn", "CAr", 20, 0, "Heavy snowfall").calculateExtraFee(0));
        assertEquals(1, new FeeCalculateService("TaLLinn", "BIKE", 20, 0, "Heavy snowfall").calculateExtraFee(0));
        assertEquals(1, new FeeCalculateService("TaLLinn", "scooter", 20, 0, "Light SlEEt").calculateExtraFee(0));
        assertEquals(0.5, new FeeCalculateService("TaLLinn", "bike", 20, 0, "Heavy RAIN").calculateExtraFee(0));

    }

    @Test
    void weatherExceptionCheck() {
        assertThrows(IllegalArgumentException.class, () -> new FeeCalculateService("tartu", "bIke", 20, 24, "").calculateExtraFee(0));
        assertThrows(IllegalArgumentException.class, () -> new FeeCalculateService("TaLLinn", "bIke", 20, 0, "Glaze").calculateExtraFee(0));
        assertThrows(IllegalArgumentException.class, () -> new FeeCalculateService("TaLLinn", "bIke", 20, 0, "Hail").calculateExtraFee(0));
        assertThrows(IllegalArgumentException.class, () -> new FeeCalculateService("TaLLinn", "bIke", 20, 0, "Thunderstorm").calculateExtraFee(0));
    }

    @Test
    void invalidCityOrVehicleCheck() {
        assertThrows(IllegalArgumentException.class, () -> new FeeCalculateService("Narva", "bIke", 20, 24, "").calculateBaseFee());
        assertThrows(IllegalArgumentException.class, () -> new FeeCalculateService("Tartu", "Helicopter", 20, 24, "").calculateBaseFee());
    }
}
