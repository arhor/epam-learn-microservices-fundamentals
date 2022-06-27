package com.epam.learn.microservices.fundamentals.song.service.controller

import com.epam.learn.microservices.fundamentals.dto.IdDTO
import com.epam.learn.microservices.fundamentals.dto.IdListDTO
import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.song.service.service.SongService
import com.epam.learn.microservices.fundamentals.song.service.service.dto.SongDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.constraints.Size

@LogExecution
@RestController
@RequestMapping("/songs")
class SongController(private val service: SongService) {

    @PostMapping
    fun createSongMetadata(@RequestBody song: SongDTO): ResponseEntity<*> {
        val id = service.saveSongMetadata(song)

        val location =
            ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .build(id)

        val response = IdDTO(id)

        return ResponseEntity.created(location).body(response)
    }

    @GetMapping("/{id}")
    fun getSongMetadataById(@PathVariable id: Long): SongDTO {
        return service.getSongMetadata(id)
    }

    @DeleteMapping
    fun deleteSongMetadata(
        @RequestParam(required = false) @Size(max = 200) ids: List<Long>?,
        @RequestParam(required = false) @Size(max = 200) resources: List<Long>?
    ): IdListDTO<Long> {
        val deleteSongsIds = ArrayList<Long>()

        if (ids != null) {
            deleteSongsIds += service.deleteSongMetadata(ids)
        }
        if (resources != null) {
            deleteSongsIds += service.deleteSongsMetadataByResourceIds(resources)
        }

        return IdListDTO(deleteSongsIds)
    }
}
