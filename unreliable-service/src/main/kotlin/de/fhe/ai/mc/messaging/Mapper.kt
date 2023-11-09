package de.fhe.ai.mc.messaging

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer

class GeocodingResultDeserializer : ObjectMapperDeserializer<GeocodingResult>(GeocodingResult::class.java)
class GeocodingResultSerializer : ObjectMapperSerializer<GeocodingResult>()