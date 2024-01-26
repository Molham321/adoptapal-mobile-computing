package de.fhe.adoptapal

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType


@QuarkusTest
class AnimalResourceTest {

    @Test
    fun testGetAllAnimals() {
        RestAssured.given()
                .`when`().get("/animals")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
    }

    @Test
    fun testGetAnimalById_AnimalExists() {
        RestAssured.given()
                .`when`().get("/animals/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
    }

    @Test
    fun testGetAnimalById_AnimalExistsNot() {
        RestAssured.given()
                .`when`().get("/animals/999")
                .then()
                .statusCode(404)
    }

    @Test
    fun testGetAnimalsByOwner_OwnerExists() {
        RestAssured.given()
                .`when`().get("/animals/owner/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
    }

    @Test
    fun testGetAnimalsByOwner_OwnerExistsNot() {
        RestAssured.given()
                .`when`().get("/animals/owner/999")
                .then()
                .statusCode(204)
    }

    // TODO: TEST CREATE ANIMAL NOT YET WORKING
    @Test
    fun testCreateAnimal() {
        val requestBody = """
            {
                "name": "Chom-Chom",
                "description": "merkwürdiger Wurm",
                "color": 11,
                "male": true,
                "animalCategory": 7,
                "birthday": "2023-12-31",
                "weight": 2.6,
                "owner": 1,
                "imagePath": ""
        """.trimIndent()

        RestAssured.given()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .`when`()
                .post("/animals")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)

    }

    @Test
    fun testDeleteAnimalById_AnimalExists() {
        RestAssured.given()
                .`when`().delete("/animals/2")
                .then()
                .statusCode(200)
    }

    @Test
    fun testDeleteAnimalById_AnimalExistsNot() {
        RestAssured.given()
                .`when`().delete("/animals/999")
                .then()
                .statusCode(404)
    }
}