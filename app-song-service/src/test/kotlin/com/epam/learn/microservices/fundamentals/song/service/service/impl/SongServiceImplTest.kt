package com.epam.learn.microservices.fundamentals.song.service.service.impl

import com.epam.learn.microservices.fundamentals.song.service.data.model.Song
import com.epam.learn.microservices.fundamentals.song.service.data.repository.SongRepository
import com.epam.learn.microservices.fundamentals.song.service.service.dto.SongDTO
import com.epam.learn.microservices.fundamentals.song.service.service.exception.EntityDuplicateException
import com.epam.learn.microservices.fundamentals.song.service.service.mapping.SongMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.then
import org.mockito.kotlin.willReturn

@ExtendWith(MockitoExtension::class)
internal class SongServiceImplTest {

    @Mock
    private lateinit var songMapper: SongMapper

    @Mock
    private lateinit var songRepository: SongRepository

    @InjectMocks
    private lateinit var songService: SongServiceImpl

    @Test
    fun `should successfully save new song metadata and return new record id`() {
        // given
        val dto = SongDTO(resourceId = 1L)
        val entity = Song(resourceId = dto.resourceId, id = 2L)

        given { songRepository.existsByResourceId(any()) } willReturn { false }
        given { songMapper.mapDtoToEntity(any()) } willReturn { entity }
        given { songRepository.save<Song>(any()) } willReturn { entity }

        // when
        val result = songService.saveSongMetadata(dto)

        // then
        assertThat(result)
            .isNotNull
            .isEqualTo(entity.id)

        then(songRepository)
            .should()
            .existsByResourceId(dto.resourceId)
        then(songMapper)
            .should()
            .mapDtoToEntity(dto)
        then(songRepository)
            .should()
            .save(entity)
    }

    @Test
    fun `should throw EntityDuplicateException saving song metadata with already existing resource id`() {
        // given
        val dto = SongDTO(resourceId = 1L)

        given { songRepository.existsByResourceId(any()) } willReturn { true }

        // when
        val result = catchException { songService.saveSongMetadata(dto) }

        // then
        assertThat(result)
            .isInstanceOf(EntityDuplicateException::class.java)
            .hasFieldOrPropertyWithValue("condition", "resourceId = ${dto.resourceId}")

        then(songRepository)
            .should()
            .existsByResourceId(dto.resourceId)
        then(songRepository)
            .shouldHaveNoMoreInteractions()
        then(songMapper)
            .shouldHaveNoInteractions()
    }
}
