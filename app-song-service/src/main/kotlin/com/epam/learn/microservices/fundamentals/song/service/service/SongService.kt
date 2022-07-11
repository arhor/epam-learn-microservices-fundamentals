package com.epam.learn.microservices.fundamentals.song.service.service

import com.epam.learn.microservices.fundamentals.song.service.service.dto.SongDTO

interface SongService {

    fun saveSongMetadata(dto: SongDTO): Long

    fun getSongMetadata(id: Long): SongDTO

    fun getSongMetadataByResourceId(resourceId: Long): SongDTO

    fun deleteSongMetadata(ids: List<Long>): List<Long>

    fun deleteSongsMetadataByResourceIds(resources: List<Long>): List<Long>
}
