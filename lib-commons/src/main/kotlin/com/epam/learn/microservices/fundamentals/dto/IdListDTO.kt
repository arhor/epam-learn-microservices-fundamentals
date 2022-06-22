package com.epam.learn.microservices.fundamentals.dto

import java.io.Serializable

data class IdListDTO<ID : Serializable>(
    val ids: List<ID>,
) {
    constructor(ids: Iterable<ID>) : this(ids.toList())
}
