package de.fhe.adoptapal

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test

@QuarkusTest
class ColorResourceTest {

    @Test
    fun testGetAllColors() {
        RestAssured.given()
                .`when`().get("/animals/colors")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
    }

    @Test
    fun testGetColorById_ColorExists() {
        RestAssured.given()
                .`when`().get("/animals/colors/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
    }

    @Test
    fun testGetColorById_ColorExistsNot() {
        RestAssured.given()
                .`when`().get("/animals/colors/999")
                .then()
                .statusCode(404)
    }
}