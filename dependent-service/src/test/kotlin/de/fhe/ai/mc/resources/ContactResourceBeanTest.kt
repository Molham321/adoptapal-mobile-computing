package de.fhe.ai.mc.resources

import de.fhe.ai.mc.model.Contact
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

/**
 * Direct test of our contacts resource class. No HTTP communication involved,
 * so simple JUnit assertions for verification can be used.
 */

@QuarkusTest
class ContactResourceBeanTest {

    @Inject
    lateinit var resource: ContactResource

    @Test
    fun testInsertContact() {
        // Given
        val c =  TestData.newContact()
        val currentNumberOfContacts = resource.getAll().await().indefinitely().size

        // When
        val insertResponse = resource.insert(c).await().indefinitely()
        // Then
        Assertions.assertEquals(201, insertResponse.status)

        // When
        val getAllResponse = resource.getAll().await().indefinitely()
        // Then
        Assertions.assertEquals(currentNumberOfContacts+1, getAllResponse.size)
        Assertions.assertEquals(c, getAllResponse[getAllResponse.size-1])

        // When
        val contactUri = insertResponse.location.path
        val contactId = contactUri.substring(contactUri.lastIndexOf('/') + 1)
        val contactByIdResponse = resource.getById(contactId.toLong()).await().indefinitely()
        // Then
        Assertions.assertEquals(200, contactByIdResponse.status)
        Assertions.assertEquals(c, contactByIdResponse.entity as Contact)

        // When
        val deleteResponse = resource.deleteById(contactId.toLong()).await().indefinitely()
        // Then
        Assertions.assertEquals(200, deleteResponse.status)
    }
}
