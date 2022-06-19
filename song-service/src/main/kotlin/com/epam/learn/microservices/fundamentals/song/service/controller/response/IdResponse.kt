package com.epam.learn.microservices.fundamentals.song.service.controller.response

import java.io.Serializable

data class IdResponse<ID : Serializable>(val id: ID)
