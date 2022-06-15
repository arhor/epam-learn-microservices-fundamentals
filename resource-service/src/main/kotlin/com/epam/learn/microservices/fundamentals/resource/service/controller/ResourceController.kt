package com.epam.learn.microservices.fundamentals.resource.service.controller

import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import com.epam.learn.microservices.fundamentals.resource.service.validation.HasMediaType
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Size

@Validated
@RestController
@RequestMapping("/resources")
class ResourceController(private val service: ResourceService) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun uploadNewResource(@RequestParam @HasMediaType("audio/mpeg") file: MultipartFile): ResponseEntity<*> {
        val resource = service.saveResource(filename = file.name, data = file.bytes)
        val responseBody = mapOf("id" to resource.id)

        return ResponseEntity.ok(responseBody)
    }

    @GetMapping("/{id}", produces = ["audio/mpeg"])
    fun getResourceAudioBinaryData(@PathVariable id: Long): ResponseEntity<*> {
        val (resource, data) = service.findResource(id)
        val stream = InputStreamResource(data)

        return ResponseEntity.ok()
            .contentLength(resource.length)
            .header(CONTENT_DISPOSITION, "attachment; filename=\"${resource.filename}\"")
            .body(stream)
    }

    @DeleteMapping
    fun deleteResources(@RequestParam @Size(max = 200) ids: Iterable<Long>): ResponseEntity<*> {
        val deleteResourcesIds = service.deleteResources(ids)
        val responseBody = mapOf("ids" to deleteResourcesIds)

        return ResponseEntity.ok(responseBody)
    }
}
