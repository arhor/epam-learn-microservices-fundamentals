package com.epam.learn.microservices.fundamentals.resource.processor.service.impl

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.resource.processor.model.ResourceMetadata
import com.epam.learn.microservices.fundamentals.resource.processor.service.ResourceMetadataProcessor
import org.apache.tika.metadata.Metadata
import org.apache.tika.metadata.TikaCoreProperties
import org.apache.tika.metadata.XMPDM
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.ParseContext
import org.apache.tika.sax.BodyContentHandler
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
@LogExecution
class ResourceMetadataProcessorImpl : ResourceMetadataProcessor {

    override fun extractMetadata(resourceId: Long, data: InputStream): ResourceMetadata {
        val autoDetectParser = AutoDetectParser()
        val handler = BodyContentHandler()
        val metadata = Metadata()
        val context = ParseContext()

        autoDetectParser.parse(data, handler, metadata, context)

        return ResourceMetadata(
            name = metadata[TikaCoreProperties.TITLE],
            year = metadata[XMPDM.RELEASE_DATE],
            album = metadata[XMPDM.ALBUM],
            artist = metadata[XMPDM.ARTIST] ?: metadata[XMPDM.ALBUM_ARTIST],
            length = metadata[XMPDM.DURATION],
            resourceId = resourceId,
        )
    }
}
