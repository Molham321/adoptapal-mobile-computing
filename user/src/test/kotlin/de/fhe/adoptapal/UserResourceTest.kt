package de.fhe.adoptapal

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test

@QuarkusTest
class UserResourceTest {
    @Test
    fun testGetAllEndpoint() {
        RestAssured.given()
            .`when`().get("/users")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
    }

    @Test
    fun testGetByIdEndpoint_UserExists() {
        RestAssured.given()
            .`when`().get("/users/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
    }

    @Test
    fun testGetByIdEndpoint_UserNotExists() {
        RestAssured.given()
            .`when`().get("/users/999")
            .then()
            .statusCode(404)
    }

    @Test
    fun testGetByEmailEndpoint_UserExists() {
        RestAssured.given()
            .`when`().get("/users/email/user@example.com")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
    }

    @Test
    fun testGetByEmailEndpoint_UserNotExists() {
        RestAssured.given()
            .`when`().get("/users/email/nonexistent@example.com")
            .then()
            .statusCode(404)
    }

    @Test
    fun testCreateUserEndpoint() {
        val requestBody = """
            {
                "username": "testUser",
                "email": "newuser@example.com",
                "phoneNumber": "1234567890"
            }
        """.trimIndent()

        RestAssured.given()
            .body(requestBody)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/users")
            .then()
            .statusCode(201)

        RestAssured.given()
            .body(requestBody)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/users")
            .then()
            .statusCode(400)
    }
    @Test
    fun testUpdateByIdEndpoint_UserExists() {
        val requestBody = """
            {
                "username": "testUser",
                "email": "newuser321@example.com",
                "phoneNumber": "1234567890"
            }
        """.trimIndent()

        RestAssured.given()
            .body(requestBody)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/users/1")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
    }

    @Test
    fun testUpdateByIdEndpoint_UserNotExists() {
        val requestBody = """
            {
                "username": "testUser",
                "email": "newuser@example.com",
                "phoneNumber": "1234567890"
            }
        """.trimIndent()

        RestAssured.given()
            .body(requestBody)
            .contentType(ContentType.JSON)
            .`when`()
            .put("/users/999")
            .then()
            .statusCode(404)
    }

    @Test
    fun testDeleteByIdEndpoint_UserExists() {
        RestAssured.given()
            .`when`().delete("/users/2")
            .then()
            .statusCode(200)
    }

    @Test
    fun testDeleteByIdEndpoint_UserNotExists() {
        RestAssured.given()
            .`when`().delete("/users/999")
            .then()
            .statusCode(404)
    }
}