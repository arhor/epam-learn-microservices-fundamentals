package com.epam.learn.microservices.fundamentals.dto

import java.io.Serializable

data class IdDTO<ID : Serializable>(
    val id: ID,
)
