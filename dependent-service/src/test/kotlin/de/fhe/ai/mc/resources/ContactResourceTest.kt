package de.fhe.ai.mc.resources

import io.quarkus.test.junit.QuarkusTest
import io.restassured.http.ContentType
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test

/**
 * Some tests of our contacts endpoint using REST assured for verification
 */

@QuarkusTest
class ContactResourceTest {

    @Test
    fun getAll() {
        given()
            .`when`()
                .get("/contacts")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
    }

    @Test
    fun insert() {
        val c = TestData.newContact()

        val insertResponse = given()
            .body(c)
            .contentType(ContentType.JSON)
            .`when`()
                .post("/contacts")
            .then()
                .statusCode(201)
                .header("location", containsString("/contacts/"))
            .extract()
                .response()

        // We will use contact id later to delete the contact
        val locationHeader = insertResponse.headers.get("location").value
        val contactId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1)

        given()
            .`when`()
                .get("/contacts")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", `is`(1),
                    "name[0]", `is`(c.name),
                    "firstname[0]", `is`(c.firstname),
                    "mail[0]", `is`(c.mail))

        given()
            .`when`()
                .delete("/contacts/$contactId")
            .then()
                .statusCode(200)
    }
}