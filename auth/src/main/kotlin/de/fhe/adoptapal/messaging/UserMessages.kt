package de.fhe.adoptapal.messaging

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer

class CreateUserMessage(val id: Long, val email: String, val password: String)
class CreateUserMessageDeserializer: ObjectMapperDeserializer<CreateUserMessage>(CreateUserMessage::class.java)

class UpdateUserMessage(val id: Long, val newEmail: String?, val newPassword : String?)
class UpdateUserMessageDeserializer: ObjectMapperDeserializer<UpdateUserMessage>(UpdateUserMessage::class.java)

class DeleteUserMessage(val id: Long)
class DeleteUserMessageDeserializer: ObjectMapperDeserializer<DeleteUserMessage>(DeleteUserMessage::class.java)
