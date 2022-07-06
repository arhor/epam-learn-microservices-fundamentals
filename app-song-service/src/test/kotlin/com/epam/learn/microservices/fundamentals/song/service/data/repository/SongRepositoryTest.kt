package com.epam.learn.microservices.fundamentals.song.service.data.repository

import com.epam.learn.microservices.fundamentals.song.service.config.DatabaseConfig
import com.epam.learn.microservices.fundamentals.song.service.data.model.Song
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Tag("integration")
@DataJdbcTest
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(classes = [DatabaseConfig::class])
internal class SongRepositoryTest {

    @Autowired
    private lateinit var repository: SongRepository

    @Test
    fun `should return true for the existing resource id`() {
        // given
        val song = repository.save(Song(resourceId = 1L))

        // when
        val result = repository.existsByResourceId(song.resourceId)

        // then
        assertThat(result)
            .isTrue
    }

    @Test
    fun `should return false for the non-existing resource id`() {
        // given
        val song = Song(resourceId = 2L)

        // when
        val result = repository.existsByResourceId(song.resourceId)

        // then
        assertThat(result)
            .isFalse
    }

    @Test
    fun `should return all resources by id list`() {
        // given
        val expectedSongs = repository.saveAll((1L..3L).map { Song(resourceId = it) })
        val unexpectedSongs = repository.saveAll((4L..6L).map { Song(resourceId = it) })

        // when
        val result = repository.findAllByResourceIdIn(expectedSongs.map { it.resourceId })

        // then
        assertThat(result)
            .isNotNull
            .containsAll(expectedSongs)
            .doesNotContainAnyElementsOf(unexpectedSongs)
    }

    @Test
    fun `should return an empty list for the empty id list`() {
        // given
        val resourceIds = emptyList<Long>()

        // when
        val result = repository.findAllByResourceIdIn(resourceIds)

        // then
        assertThat(result)
            .isNotNull
            .isEmpty()
    }

    companion object {
        @JvmStatic
        @Container
        private val db = PostgreSQLContainer("postgres:12")

        @JvmStatic
        @DynamicPropertySource
        fun registerDynamicProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", db::getJdbcUrl)
            registry.add("spring.datasource.username", db::getUsername)
            registry.add("spring.datasource.password", db::getPassword)
        }
    }
}
