package com.epam.learn.microservices.fundamentals.error

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = ErrorCodeSerializer::class)
interface ErrorCode {

    val type: Type
    val value: Int
    val label: String

    enum class Type {
        GEN,
        DAT,
        VAL,
        ;
    }

    object UNCATEGORIZED : ErrorCode {
        override val type = Type.GEN
        override val value = 0
        override val label = ""
    }
}
