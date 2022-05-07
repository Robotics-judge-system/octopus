package ru.anarcom.octopus.entity.protocol

data class ProtocolFieldDescription(
    val type:ProtocolFieldType,
    val min:Long? = null,
    val max:Long? = null,
    val name:String,
)
