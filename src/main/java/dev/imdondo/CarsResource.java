package dev.imdondo;

import dev.imdondo.entities.Car;
import dev.imdondo.repository.CarRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarsResource {

    @Inject
    CarRepository carRepository;

    @GET
    public Response getAll() {
        List<Car> cars = carRepository.listAll();
        return Response.ok(cars).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        return carRepository
                .findByIdOptional(id)
                .map(car -> Response.ok(car).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @GET
    @Path("title/{title}")
    public Response getByTitle(@PathParam("title") String title) {
        return carRepository
                .find("title", title)
                .singleResultOptional()
                .map(car -> Response.ok(car).build())
                .orElse(Response.status(NOT_FOUND).build());
    }

    @GET
    @Path("country/{country}")
    public Response getByCountry(@PathParam("country") String make) {
        List<Car> cars = carRepository.findByMake(make);
        return Response.ok(cars).build();
    }

    @POST
    @Transactional
    public Response create(Car car) {
        carRepository.persist(car);
        if (carRepository.isPersistent(car)) {
            return Response.created(URI.create("/cars/" + car.getId())).build();
        }
        return Response.status(BAD_REQUEST).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateById(@PathParam("id") Long id, Car car) {
        return carRepository
                .findByIdOptional(id)
                .map(
                        m -> {
                            m.setMake(car.getMake());
                            return Response.ok(m).build();
                        })
                .orElse(Response.status(NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = carRepository.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(NOT_FOUND).build();
    }
}
