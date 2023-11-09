package de.fhe.ai.mc.model.util

import de.fhe.ai.mc.model.GeocodingResult
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer

class GeocodingResultDeserializer : ObjectMapperDeserializer<GeocodingResult>(GeocodingResult::class.java)
class GeocodingResultSerializer : ObjectMapperSerializer<GeocodingResult>()