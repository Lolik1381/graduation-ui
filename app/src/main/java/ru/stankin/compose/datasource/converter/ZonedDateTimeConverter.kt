package ru.stankin.compose.datasource.converter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeConverter : JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

    companion object {
        private val FORMATTER = DateTimeFormatter.ISO_DATE_TIME
    }

    override fun serialize(
        src: ZonedDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(FORMATTER.format(src))
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ZonedDateTime {
        return FORMATTER.parse(json.asString) { temporal -> ZonedDateTime.from(temporal) }
    }
}