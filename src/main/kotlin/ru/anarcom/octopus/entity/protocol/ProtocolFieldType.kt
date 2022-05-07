package ru.anarcom.octopus.entity.protocol

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

enum class ProtocolFieldType(
    val jsonName: String
) {
    CHECKBOX("checkbox"),
    VALUE("value"),
    TIME("time"),
    SEPARATOR("separator"),
    ;

    @JsonValue
    fun toValue() = jsonName.lowercase(Locale.getDefault())

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromString(value: String): ProtocolFieldType = ProtocolFieldType.values()
            .find {
                it.name == value.uppercase(Locale.getDefault())
            }
            ?: throw java.lang.RuntimeException("Problems, many problems")
    }
}
