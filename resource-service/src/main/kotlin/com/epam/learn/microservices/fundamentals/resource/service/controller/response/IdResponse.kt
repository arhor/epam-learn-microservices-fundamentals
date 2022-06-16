package com.epam.learn.microservices.fundamentals.resource.service.controller.response

import java.io.Serializable

data class IdResponse<ID : Serializable>(val id: ID)
