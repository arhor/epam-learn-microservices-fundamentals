package com.epam.learn.microservices.fundamentals.song.service.service.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.song.service.data.model.Song
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

    override fun getSongMetadataByResourceId(resourceId: Long): SongDTO {
        return songRepository.findByResourceId(resourceId)?.let(songMapper::mapEntityToDto)
            ?: throw EntityNotFoundException("resourceId = $resourceId")
    }

    @Transactional
    override fun deleteSongMetadata(ids: List<Long>): List<Long> {
        return deleteInternal { songRepository.findAllById(ids) }
    }

    @Transactional
    override fun deleteSongsMetadataByResourceIds(resources: List<Long>): List<Long> {
        return deleteInternal { songRepository.findAllByResourceIdIn(resources) }
    }

    private inline fun deleteInternal(source: () -> Iterable<Song>): List<Long> {
        val existingSongIds = source.invoke().mapNotNull { it.id }

        if (existingSongIds.isNotEmpty()) {
            songRepository.deleteAllById(existingSongIds)
        }
        return existingSongIds
    }
}
