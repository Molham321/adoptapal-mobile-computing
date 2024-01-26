package de.fhe.adoptapal

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test

@QuarkusTest
class AddressResourceTest {

    @Test
    fun testGetAllEndpoint() {
        RestAssured.given()
            .`when`().get("/addresses")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
    }

    @Test
    fun testGetByIdEndpoint_AddressExists() {
        RestAssured.given()
            .`when`().get("/addresses/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
    }

    @Test
    fun testGetByIdEndpoint_AddressNotExists() {
        RestAssured.given()
            .`when`().get("/addresses/999")
            .then()
            .statusCode(404)
    }

    @Test
    fun testCreateAddressEndpoint() {
        val requestBody = """
            {
                "street": "Test Street",
                "city": "Test City",
                "postalCode": "12345"
            }
        """.trimIndent()

        RestAssured.given()
            .body(requestBody)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/addresses")
            .then()
            .statusCode(201)
    }

    @Test
    fun testUpdateByIdEndpoint_AddressExists() {
        val requestBody = """
            {
                "street": "Updated Street",
                "city": "Updated City",
                "postalCode": "54321"
            }
        """.trimIndent()

        RestAssured.given()
            .body(requestBody)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/addresses/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
    }

    @Test
    fun testUpdateByIdEndpoint_AddressNotExists() {
        val requestBody = """
            {
                "street": "Updated Street",
                "city": "Updated City",
                "postalCode": "54321"
            }
        """.trimIndent()

        RestAssured.given()
            .body(requestBody)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/addresses/999")
            .then()
            .statusCode(404)
    }

    @Test
    fun testDeleteByIdEndpoint_AddressExists() {
        RestAssured.given()
            .`when`().delete("/addresses/2")
            .then()
            .statusCode(200)
    }

    @Test
    fun testDeleteByIdEndpoint_AddressNotExists() {
        RestAssured.given()
            .`when`().delete("/addresses/999")
            .then()
            .statusCode(404)
    }
}