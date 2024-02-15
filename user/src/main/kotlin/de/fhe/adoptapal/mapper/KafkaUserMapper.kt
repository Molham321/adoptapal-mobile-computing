package de.fhe.adoptapal.mapper

import de.fhe.adoptapal.messaging.models.KafkaUserRegistrationResponse
import de.fhe.adoptapal.model.UserEntity
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer

class UserResultSerializer: ObjectMapperSerializer<UserEntity>()
class UserResultDeserializer: ObjectMapperDeserializer<UserEntity>(UserEntity::class.java)

class UserDeletionResultDeserializer: ObjectMapperDeserializer<Long>(Long::class.java)
class UserCreationAckSerializer : ObjectMapperSerializer<KafkaUserRegistrationResponse>()