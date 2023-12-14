package de.fhe.adoptapal

import de.fhe.adoptapal.core.JwtTokenUtils
import de.fhe.adoptapal.model.UserEntity
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.http.Header
import org.junit.jupiter.api.Test

// TODO(erik): test login endpoint
// TODO(erik): clearing repository between tests doesn't work
@QuarkusTest
class AuthResourceTest {
    @Test
    fun registerEndpointNewUser() {
        given()
            .body("""
                {
                    "email": "unique",
                    "password": "password"
                }
            """.trimIndent())
            .contentType(ContentType.JSON)
            .`when`()
                .post("/register")
            .then()
                .statusCode(200)
    }

    @Test
    fun registerEndpointDuplicateUser() {
        given()
            .body("""
                {
                    "email": "new",
                    "password": "password"
                }
            """.trimIndent())
            .contentType(ContentType.JSON)
            .`when`()
                .post("/register")
            .then()
                .statusCode(200)

        given()
            .body("""
                {
                    "email": "new",
                    "password": "other-password"
                }
            """.trimIndent())
            .contentType(ContentType.JSON)
            .`when`()
                .post("/register")
            .then()
                .statusCode(400)
    }

    @Test
    fun validateEndpointAuthorized() {
        val user = UserEntity()
        user.email = "user"
        user.password = "password"
        user.role = "role"
        val token = JwtTokenUtils.generateToken(user, 3600, "test")

        given()
            .header(Header("Authorization", "Bearer $token"))
            .`when`()
                .get("/validate")
            .then()
                .statusCode(401)
    }

    @Test
    fun validateEndpointUnauthorized() {
        given()
            .`when`()
                .get("/validate")
            .then()
                .statusCode(401)
    }
}
