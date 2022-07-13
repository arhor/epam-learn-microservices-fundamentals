package com.epam.learn.microservices.fundamentals.error

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

class ErrorCodeSerializer : StdSerializer<ErrorCode>(ErrorCode::class.java) {

    override fun serialize(value: ErrorCode, generator: JsonGenerator, provider: SerializerProvider) {
        val type = value.type.name
        val code = convertCodeToPaddedString(value)

        generator.writeString("$type-$code")
    }

    private fun convertCodeToPaddedString(value: ErrorCode): String {
        val numberAsString = value.value.toString()

        if (numberAsString.length > NUM_CODE_MAX_LENGTH) {
            log.debug("ErrorCode {} numeric value is too large", value)
        }
        return numberAsString.padStart(NUM_CODE_MAX_LENGTH, NUM_CODE_PAD_SYMBOL)
    }

    companion object {
        private const val NUM_CODE_MAX_LENGTH = 5
        private const val NUM_CODE_PAD_SYMBOL = '0'

        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
