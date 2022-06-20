package com.epam.learn.microservices.fundamentals.song.service.service.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.song.service.data.repository.SongRepository
import com.epam.learn.microservices.fundamentals.song.service.service.SongService
import com.epam.learn.microservices.fundamentals.song.service.service.dto.SongDTO
import com.epam.learn.microservices.fundamentals.song.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.song.service.service.exception.EntityNotFoundException
import com.epam.learn.microservices.fundamentals.song.service.service.mapping.SongMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@LogExecution
class SongServiceImpl(
    private val songMapper: SongMapper,
    private val songRepository: SongRepository,
) : SongService {

    @Transactional
    override fun saveSongMetadata(dto: SongDTO): Long {
        if (songRepository.existsByResourceId(dto.resourceId)) {
            throw EntityDuplicateException("resourceId = ${dto.resourceId}")
        }
        val song = songMapper.mapDtoToEntity(dto)
        val savedSong = songRepository.save(song)

        return savedSong.id ?: throw IllegalStateException("Saved song must have an ID")
    }

    override fun getSongMetadata(id: Long): SongDTO {
        return songRepository.findByIdOrNull(id)?.let(songMapper::mapEntityToDto)
            ?: throw EntityNotFoundException("id = $id")
    }

    @Transactional
    override fun deleteResources(ids: List<Long>): List<Long> {
        val existingSongIds = songRepository.findAllById(ids).mapNotNull { it.id }
        songRepository.deleteAllById(existingSongIds)
        return existingSongIds
    }
}
