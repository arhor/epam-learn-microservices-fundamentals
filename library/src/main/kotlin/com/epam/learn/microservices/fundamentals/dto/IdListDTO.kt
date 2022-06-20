package com.epam.learn.microservices.fundamentals.dto

import java.io.Serializable

data class IdListDTO<ID : Serializable>(
    val ids: Iterable<ID>,
)
