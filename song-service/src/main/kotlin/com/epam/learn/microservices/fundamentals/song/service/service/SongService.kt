package com.epam.learn.microservices.fundamentals.song.service.service

import com.epam.learn.microservices.fundamentals.song.service.service.dto.SongDTO

interface SongService {
    fun saveSongMetadata(dto: SongDTO): Long

    fun getSongMetadata(id: Long): SongDTO

    fun getSongsMetadataByResourceIds(resources: List<Long>): List<SongDTO>

    fun deleteSongMetadata(ids: List<Long>): List<Long>
}
