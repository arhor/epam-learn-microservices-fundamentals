package com.epam.learn.microservices.fundamentals.song.service.controller.response

import java.io.Serializable

data class IdListResponse<ID : Serializable>(val ids: Iterable<ID>)
