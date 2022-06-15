package com.epam.learn.microservices.fundamentals.resource.service.controller

import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/resources")
class ResourceController(private val service: ResourceService) {

    @PostMapping(consumes = ["audio/mpeg"])
    fun uploadNewResource(@RequestParam file: MultipartFile): ResponseEntity<*> {
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
            .header("Content-Disposition", "attachment; filename=\"${resource.filename}\"")
            .body(stream)
    }

    @DeleteMapping
    fun deleteResources(@RequestParam ids: Iterable<Long>): ResponseEntity<*> {
        val deleteResourcesIds = service.deleteResources(ids)
        val responseBody = mapOf("ids" to deleteResourcesIds)

        return ResponseEntity.ok(responseBody)
    }
}
