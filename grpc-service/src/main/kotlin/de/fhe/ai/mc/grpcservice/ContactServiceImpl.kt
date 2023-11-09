package de.fhe.ai.mc.grpcservice

import de.fhe.ai.mc.*
import io.quarkus.grpc.GrpcService
import io.smallrye.mutiny.Uni

@GrpcService
class ContactServiceImpl : ContactService {

    private val contactList = ArrayList<Contact>()

    override fun insertContact(
        contact: Contact?
    ): Uni<ContactServiceReply> {
        // Check if a contact was given
        if( contact == null )
            return createMessageReply(false,
                "No Contact given for Insert")

        // Check if contact is already in our list
        val foundContact = contactList.find { it.id == contact.id  }
        if(foundContact != null)
            return createMessageReply(false,
                "Contact already inserted")

        // Add Contact
        contactList.add(contact)

        return createMessageReply(true, "Contact added")
    }

    override fun getAll(
        request: Empty?
    ): Uni<ContactServiceReply> {
        return createContactsReply(contactList)
    }

    override fun getContacts(
        request: ContactServiceRequest?
    ): Uni<ContactServiceReply> {
        // Should we search for a contact id?
        if(request?.contactId != null && request.contactId != 0L) {
            return handleIdRequest(request.contactId) { createContactReply(it) }
        }

        // Should we search by search string
        else if(request?.searchString != null) {
            val foundContacts = contactList.filter {
                it.firstname == request.searchString ||
                        it.lastname == request.searchString
            }
            return if(foundContacts.isNotEmpty())
                createContactsReply(foundContacts)
            else
                createMessageReply(false,
                    "No contacts found for search string '${request.searchString}'")
        }

        // No id or search string given? Send an info reply
        return createMessageReply(false,
            "Invalid request - id or searchString must be given")
    }

    override fun deleteContact(
        request: ContactServiceRequest?
    ): Uni<ContactServiceReply> {
        // Should we search for a contact id?
        if(request?.contactId != null) {
            return handleIdRequest(request.contactId) {
                contactList.remove(it)
                createMessageReply(true, "Contact removed")
            }
        }

        // No id given? Send an info reply
        return createMessageReply(false,
            "Invalid request - id must be given to delete a contact")
    }

    override fun updateContact(
        contact: Contact?
    ): Uni<ContactServiceReply> {
        // Check if a contact was given
        if(contact == null)
            return createMessageReply(false,
                "No Contact given for Update")

        return handleIdRequest(contact.id) {
            contactList.remove(it)
            contactList.add(contact)
            createMessageReply(false, "Contact updated")
        }
    }


    /*
        Private Helper Method for searching a contact by id and doing something with it.
        The passed handler function should create a matching reply message at the end
     */
    private fun handleIdRequest(
        contactId: Long,
        handler: (Contact) -> Uni<ContactServiceReply>
    ): Uni<ContactServiceReply> {
        val foundContact = contactList.find { it.id == contactId  }
        return if(foundContact != null)
            handler.invoke(foundContact)
        else
            createMessageReply(false, "No contact found for id $contactId")
    }

    /*
        Private Helper Methods to create Reply Messages
     */
    private fun createMessageReply(
        success: Boolean,
        message: String
    ): Uni<ContactServiceReply> {
        return Uni.createFrom().item(
            ContactServiceReply.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .build())
    }

    private fun createContactReply(
        contact: Contact
    ): Uni<ContactServiceReply> {
        return Uni.createFrom().item(
            ContactServiceReply.newBuilder()
                .setSuccess(true)
                .setContacts(0, contact)
                .build())
    }

    private fun createContactsReply(
        contacts: List<Contact>
    ): Uni<ContactServiceReply> {
        return Uni.createFrom().item(
            ContactServiceReply.newBuilder()
                .setSuccess(true)
                .addAllContacts(contacts)
                .build())
    }

}