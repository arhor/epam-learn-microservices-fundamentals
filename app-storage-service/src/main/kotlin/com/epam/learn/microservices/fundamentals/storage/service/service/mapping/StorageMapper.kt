package com.epam.learn.microservices.fundamentals.storage.service.service.mapping

import com.epam.learn.microservices.fundamentals.storage.service.data.model.Storage
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageRequestDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageResponseDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface StorageMapper {

    @Mapping(target = "id", ignore = true)
    fun mapToEntity(dto: StorageRequestDTO): Storage

    fun mapToDTO(entity: Storage): StorageResponseDTO
}
