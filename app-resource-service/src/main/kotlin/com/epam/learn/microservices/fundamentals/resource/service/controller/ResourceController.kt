package com.epam.learn.microservices.fundamentals.resource.service.controller

import com.epam.learn.microservices.fundamentals.dto.IdDTO
import com.epam.learn.microservices.fundamentals.dto.IdListDTO
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import com.epam.learn.microservices.fundamentals.resource.service.service.dto.ResourceDTO
import com.epam.learn.microservices.fundamentals.resource.service.validation.HasMediaType
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.constraints.Size

@Validated
@LogExecution
@RestController
@RequestMapping("/resources")
class ResourceController(private val service: ResourceService) {

    @PostMapping(consumes = ["multipart/form-data"], produces = ["application/json"])
    fun uploadNewResource(@RequestParam @HasMediaType("audio/mpeg") file: MultipartFile): ResponseEntity<*> {
        val id = service.saveResource(
            ResourceDTO(
                filename = file.originalFilename ?: file.name,
                data = file.inputStream,
                size = file.size,
            )
        )

        val location =
            ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .build(id)

        val dto = IdDTO(id)

        return ResponseEntity.created(location).body(dto)
    }

    @GetMapping("/{id}", produces = ["application/octet-stream"])
    fun getResourceAudioBinaryData(@PathVariable id: Long): ResponseEntity<*> {
        val resource = service.getResource(id)
        val stream = InputStreamResource(resource.data)

        return ResponseEntity.ok()
            .header(CONTENT_DISPOSITION, "attachment; filename=\"${resource.filename}\"")
            .body(stream)
    }

    @DeleteMapping(consumes = ["application/json"], produces = ["application/json"])
    fun deleteResources(@RequestParam @Size(max = 200) ids: List<Long>): ResponseEntity<*> {
        val deleteResourcesIds = service.deleteResources(ids)
        val dto = IdListDTO(deleteResourcesIds)

        return ResponseEntity.ok(dto)
    }
}
