package dev.imdondo.repository;

import dev.imdondo.entities.Car;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {

    public List<Car> findByMake(String make) {
        return list("SELECT m FROM Car m WHERE m.make = ?1 ORDER BY id DESC", make);
    }
}
