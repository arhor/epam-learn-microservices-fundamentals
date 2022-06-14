package com.epam.learn.microservices.fundamentals.resource.service.controller

import com.epam.learn.microservices.fundamentals.resource.service.service.ResourceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/resources")
class ResourceController(private val service: ResourceService) {

    @PostMapping(consumes = ["audio/mpeg"])
    fun uploadNewResource(@RequestParam file: MultipartFile): ResponseEntity<*> {
        TODO("not implemented")
    }

    @GetMapping("/{id}")
    fun getResourceAudioBinaryData(@PathVariable id: Long): ResponseEntity<*> {
        TODO("not implemented")
    }

    @DeleteMapping
    fun deleteResources(@RequestParam id: Array<Long>): ResponseEntity<*> {
        TODO("not implemented")
    }
}
