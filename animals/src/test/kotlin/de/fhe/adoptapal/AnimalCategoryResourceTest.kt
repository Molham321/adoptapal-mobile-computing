package de.fhe.adoptapal

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test

@QuarkusTest
class AnimalCategoryResourceTest {

    @Test
    fun testGetAllAnimalCategories() {
        RestAssured.given()
                .`when`().get("/animals/animalCategories")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
    }

    @Test
    fun testGetAnimalCategoryById_AnimalCategoryExists() {
        RestAssured.given()
                .`when`().get("/animals/animalCategories/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
    }

    @Test
    fun testGetAnimalCategoryById_AnimalCategoryExistsNot() {
        RestAssured.given()
                .`when`().get("/animals/animalCategory/999")
                .then()
                .statusCode(404)
    }
}