package dev.imdondo;


import io.quarkus.test.junit.QuarkusTest;

//import io.vertx.core.json.JsonObject;
import javax.json.JsonObject ;
import javax.json.Json;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarResourceTestIT {

    @Test
    @Order(1)
    void getAll() {
        given()
                .when()
                .get("/cars")
                .then()
                .body("size()", equalTo(2))
                .body("id", hasItems(1, 2))
                .body("make", hasItems("FirstCar", "SecondCar"))
                .body("model", hasItem("hatchback"))
                .body("description", hasItem("MyFirstCar"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getById() {
        given()
                .when()
                .get("/cars/1")
                .then()
                .body("id", equalTo(1))
                .body("make", equalTo("FirstCar"))
                .body("description", equalTo("MyFirstCar"))
                .body("model", equalTo("hatchback"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getByIdKO() {
        given().when().get("/cars/1000").then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(1)
    void getByTitle() {
        given()
                .when()
                .get("/cars/make/FirstCar")
                .then()
                .body("id", equalTo(1))
                .body("make", equalTo("FirstCar"))
                .body("description", equalTo("MyFirstMovie"))
                .body("model", equalTo("hatchback"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getByTitleKO() {
        given()
                .when()
                .get("/cars/make/LastMovie")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(2)
    void getByCountry() {
        given()
                .when()
                .get("/cars/make/Toyota")
                .then()
                .body("size()", equalTo(2))
                .body("id", hasItems(1, 2))
                .body("make", hasItems("FirstCar", "SecondCar"))
                .body("model", hasItem("hatchback"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(2)
    void getByCountryKO() {
        given()
                .when()
                .get("/cars/make/Toyota")
                .then()
                .body("size()", equalTo(0))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(3)
    void create() {
        JsonObject jsonObject =
                Json.createObjectBuilder()
                        .add("title", "ThirdCar")
                        .add("description", "MyThirdCar")
                        .add("model", "hatchback")
                        .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .post("/movies")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(4)
    void updateById() {
        JsonObject jsonObject = Json.createObjectBuilder().add("make", "SecondMovieUpdate").build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .put("/movies/2")
                .then()
                .body("id", equalTo(2))
                .body("make", equalTo("SecondCarUpdate"))
                .body("description", equalTo("MySecondCar"))
                .body("model", equalTo("hatchback"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(4)
    void updateByIdKO() {
        JsonObject jsonObject = Json.createObjectBuilder().add("make", "SecondCarUpdate").build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .put("/movies/2000")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(5)
    void deleteById() {
        given()
                .when()
                .delete("/movies/2")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        given().when().get("/movies/2").then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(5)
    void deleteByIdKO() {
        given()
                .when()
                .delete("/movies/2000")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
