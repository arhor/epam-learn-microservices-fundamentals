package com.epam.learn.microservices.fundamentals.song.service.service.impl

import com.epam.learn.microservices.fundamentals.song.service.data.model.Song
import com.epam.learn.microservices.fundamentals.song.service.data.repository.SongRepository
import com.epam.learn.microservices.fundamentals.song.service.service.SongService
import com.epam.learn.microservices.fundamentals.song.service.service.dto.SongDTO
import com.epam.learn.microservices.fundamentals.song.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.song.service.service.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.invoke.MethodHandles

@Service
class SongServiceImpl(
    private val repository: SongRepository,
) : SongService {

    @Transactional
    override fun saveSongMetadata(dto: SongDTO): Long {
        if (repository.existsByResourceId(dto.resourceId)) {
            throw EntityDuplicateException("resourceId = ${dto.resourceId}")
        }
        val song = repository.save(
            Song(
                year = dto.year,
                name = dto.name,
                album = dto.album,
                artist = dto.artist,
                length = dto.length,
                resourceId = dto.resourceId,
            )
        )
        return song.id ?: throw IllegalStateException("Saved song must have an ID")
    }

    override fun getSongMetadata(id: Long): SongDTO {
        return repository.findByIdOrNull(id)?.let {
            SongDTO(
                year = it.year,
                name = it.name,
                album = it.album,
                artist = it.artist,
                length = it.length,
                resourceId = it.resourceId,
            )
        } ?: throw EntityNotFoundException("id = $id")
    }

    @Transactional
    override fun deleteResources(ids: List<Long>): List<Long> {
        val existingSongIds = repository.findAllById(ids).mapNotNull { it.id }
        repository.deleteAllById(existingSongIds)
        return existingSongIds
    }

    companion object {
        private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
    }
}
