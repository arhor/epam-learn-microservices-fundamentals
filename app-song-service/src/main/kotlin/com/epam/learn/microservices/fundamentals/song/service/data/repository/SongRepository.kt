package com.epam.learn.microservices.fundamentals.song.service.data.repository

import com.epam.learn.microservices.fundamentals.song.service.data.model.Song
import org.springframework.data.repository.CrudRepository

interface SongRepository : CrudRepository<Song, Long> {

    fun existsByResourceId(resourceId: Long): Boolean

    fun findByResourceId(resourceId: Long): Song?

    fun findAllByResourceIdIn(resourceIds: List<Long>): List<Song>
}
