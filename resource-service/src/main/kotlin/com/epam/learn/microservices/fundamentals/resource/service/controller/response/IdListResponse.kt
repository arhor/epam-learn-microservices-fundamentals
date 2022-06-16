package com.epam.learn.microservices.fundamentals.resource.service.controller.response

import java.io.Serializable

data class IdListResponse<ID : Serializable>(val ids: Iterable<ID>)
