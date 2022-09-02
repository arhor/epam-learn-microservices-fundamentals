package com.epam.learn.microservices.fundamentals.storage.service.controller

import com.epam.learn.microservices.fundamentals.dto.IdDTO
import com.epam.learn.microservices.fundamentals.dto.IdListDTO
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.storage.service.data.StorageType
import com.epam.learn.microservices.fundamentals.storage.service.service.StorageService
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageRequestDTO
import com.epam.learn.microservices.fundamentals.storage.service.service.dto.StorageResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.constraints.Size

@LogExecution
@RestController
@RequestMapping("/storages")
class StorageController(private val service: StorageService) {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    fun createStorage(@RequestBody request: StorageRequestDTO): ResponseEntity<IdDTO<Long>> {
        val id = service.createStorage(request)

        val location =
            ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .build(id)

        val dto = IdDTO(id)

        return ResponseEntity.created(location).body(dto)
    }

    @GetMapping(produces = ["application/json"])
    fun getStorages(
        @RequestParam(required = false) type: StorageType?,
        @RequestParam(required = false) single: Boolean?,
    ): List<StorageResponseDTO> {
        return service.getStorages(type, single)
    }

    @GetMapping(params = ["ids"], produces = ["application/json"])
    fun getStoragesByIds(
        @RequestParam(required = true) ids: List<Long>,
    ): List<StorageResponseDTO> {
        return service.getStoragesByIds(ids)
    }

    @GetMapping(path = ["{id}"], produces = ["application/json"])
    fun getStorageById(@PathVariable id: Long): StorageResponseDTO {
        return service.getStorageById(id)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(produces = ["application/json"])
    fun deleteStorages(@RequestParam @Size(max = 200) ids: List<Long>): IdListDTO<Long> {
        return IdListDTO(
            ids = service.deleteStorages(
                storagesIds = ids
            )
        )
    }
}
