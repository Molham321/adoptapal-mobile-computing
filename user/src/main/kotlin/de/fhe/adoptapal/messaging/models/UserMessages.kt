package de.fhe.adoptapal.messaging.models

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer

class CreateUserMessage(val id: Long, val email: String, val password: String)
class CreateUserMessageSerializer : ObjectMapperSerializer<CreateUserMessage>()

class UpdateUserMessage(val id: Long, val newEmail: String?, val newPassword : String?)
class UpdateUserMessageSerializer : ObjectMapperSerializer<UpdateUserMessage>()

class DeleteUserMessage(val id: Long)
class DeleteUserMessageSerializer : ObjectMapperSerializer<DeleteUserMessage>()
