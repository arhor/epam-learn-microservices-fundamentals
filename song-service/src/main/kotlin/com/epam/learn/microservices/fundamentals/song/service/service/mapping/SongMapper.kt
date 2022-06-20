package com.epam.learn.microservices.fundamentals.song.service.service.mapping

import com.epam.learn.microservices.fundamentals.song.service.data.model.Song
import com.epam.learn.microservices.fundamentals.song.service.service.dto.SongDTO
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface SongMapper {

    @Mapping(target = "id", ignore = true)
    fun mapDtoToEntity(item: SongDTO): Song

    @InheritInverseConfiguration
    fun mapEntityToDto(item: Song): SongDTO
}
