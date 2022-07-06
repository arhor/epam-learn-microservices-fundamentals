package com.epam.learn.microservices.fundamentals.song.service.controller

import com.epam.learn.microservices.fundamentals.song.service.config.LocalizationConfig
import com.epam.learn.microservices.fundamentals.song.service.service.SongService
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
    fun shouldReturnDefaultMessage() {
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
        private const val UTC_DATE_TIME_PATTERN = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$"
    }
}
