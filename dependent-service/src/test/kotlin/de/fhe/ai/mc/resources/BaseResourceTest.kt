package de.fhe.ai.mc.resources

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test

/**
 * Simple test of our hello endpoints using REST assured for verification
 */

@QuarkusTest
class BaseResourceTest {

    @Test
    fun testSyncHelloEndpoint() {
        given()
            .`when`()
                .get("/base/hello")
            .then()
                .statusCode(200)
                .body(containsString("- Blocked -"))
    }

    @Test
    fun testAsyncHelloEndpoint() {
        given()
            .`when`()
                .get("/base/hello-async")
            .then()
                .statusCode(200)
                .body(containsString("- Async -"))
    }

}