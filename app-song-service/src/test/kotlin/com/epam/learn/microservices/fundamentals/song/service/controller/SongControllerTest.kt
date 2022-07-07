package com.epam.learn.microservices.fundamentals.song.service.controller

import com.epam.learn.microservices.fundamentals.song.service.config.LocalizationConfig
import com.epam.learn.microservices.fundamentals.song.service.service.SongService
import com.epam.learn.microservices.fundamentals.song.service.service.dto.SongDTO
import com.epam.learn.microservices.fundamentals.song.service.service.exception.EntityNotFoundException
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Import(LocalizationConfig::class)
@WebMvcTest(SongController::class)
internal class SongControllerTest {

    @Autowired
    private lateinit var http: MockMvc

    @MockBean
    private lateinit var service: SongService

    @Test
    fun `should return status 200 with expected response body for the existing song`() {
        // given
        val songId = 1L
        val song = SongDTO(
            name = "test song name",
            year = "test song year",
            album = "test song album",
            artist = "test song artist",
            length = "test song length",
            resourceId = 1L,
        )

        given(service.getSongMetadata(anyLong()))
            .willReturn(song)

        // when
        http.perform(get("/api/songs/{songId}", songId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name", equalTo(song.name)))
            .andExpect(jsonPath("$.year", equalTo(song.year)))
            .andExpect(jsonPath("$.album", equalTo(song.album)))
            .andExpect(jsonPath("$.artist", equalTo(song.artist)))
            .andExpect(jsonPath("$.length", equalTo(song.length)))
            .andExpect(jsonPath("$.resourceId", equalTo(song.resourceId), Long::class.java))

        // then
        then(service)
            .should()
            .getSongMetadata(songId)
    }

    @Test
    fun `should return status 404 with expected error response for the non-existing song`() {
        // given
        val songId = 1L
        val errorCondition = "id=$songId"

        given(service.getSongMetadata(anyLong()))
            .willThrow(EntityNotFoundException(errorCondition))

        // when
        http.perform(get("/api/songs/{songId}", songId))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code", equalTo("DAT-00002")))
            .andExpect(jsonPath("$.message", containsString(errorCondition)))
            .andExpect(jsonPath("$.timestamp", matchesPattern(UTC_DATE_TIME_PATTERN)))

        // then
        then(service)
            .should()
            .getSongMetadata(songId)
    }

    companion object {
        private const val UTC_DATE_TIME_PATTERN = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{0,3})?Z$"
    }
}
