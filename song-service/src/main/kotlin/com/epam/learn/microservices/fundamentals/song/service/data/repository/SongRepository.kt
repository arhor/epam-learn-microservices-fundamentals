package com.epam.learn.microservices.fundamentals.song.service.data.repository

import com.epam.learn.microservices.fundamentals.song.service.data.model.Song
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SongRepository : CrudRepository<Song, Long> {

    fun existsByResourceId(resourceId: Long): Boolean
}
