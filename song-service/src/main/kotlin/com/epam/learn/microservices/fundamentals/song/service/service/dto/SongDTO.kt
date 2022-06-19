package com.epam.learn.microservices.fundamentals.song.service.service.dto

import java.time.Duration
import java.time.Year

data class SongDTO(
    val year: Year?,
    val name: String?,
    val album: String?,
    val artist: String?,
    val length: Duration,
    val resourceId: Long,
)
