package de.fhe.ai.mc.resources

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test

/**
 * Test of our endpoint that accesses the unreliable service.
 * Our REST client Mock should be involved here to provide responses.
 */

@QuarkusTest
class DependentResourceTest {

    @Test
    fun testSingleValueAsyncEndpoint() {
        RestAssured.given()
            .`when`()
                .get("/api/unreliable-single-value-async")
            .then()
                .statusCode(200)
                .body(containsString("value"))
                .body(containsString("ipAddress"))
    }

    @Test
    fun testMultipleValuesAsyncEndpoint() {
        RestAssured.given()
            .`when`()
                .get("/api/unreliable-multiple-values-async")
            .then()
                .statusCode(200)
    }
}