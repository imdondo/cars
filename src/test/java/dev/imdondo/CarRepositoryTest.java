package dev.imdondo;

import dev.imdondo.entities.Car;
import dev.imdondo.repository.CarRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;
import java.util.List;


@QuarkusTest
class CarRepositoryTest {

    @Inject
    CarRepository carRepository;

    @Test
    void findByCountryOK() {
        List<Car> cars = carRepository.findByMake("Toyota");
        assertNotNull(cars);
        assertFalse(cars.isEmpty());
        assertEquals(2, cars.size());
        assertEquals(2L, cars.get(0).getId());
        assertEquals("SecondCar", cars.get(0).getMake());
        assertEquals("MySecondCar", cars.get(0).getDescription());
        assertEquals("Me", cars.get(0).getModel());
    }

    @Test
    void findByCountryKO() {
        List<Car> cars = carRepository.findByMake("Merc");
        assertNotNull(cars);
        assertTrue(cars.isEmpty());
    }
}
