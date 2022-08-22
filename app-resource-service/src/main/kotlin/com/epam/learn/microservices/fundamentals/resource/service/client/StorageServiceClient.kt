package com.epam.learn.microservices.fundamentals.resource.service.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "storage-service", path = "/api/storages")
interface StorageServiceClient {

    @GetMapping
    fun getStorages(
        @RequestParam("type") type: StorageType,
        @RequestParam("single") single: Boolean,
    ): List<Map<String, Any>>

    @GetMapping
    fun getStoragesByIds(
        @RequestParam("ids") ids: List<Long>,
    ): List<StorageResponseDTO>

    @GetMapping("/{id}")
    fun getStorageId(
        @PathVariable("id") id: Long,
    ): StorageResponseDTO
}
