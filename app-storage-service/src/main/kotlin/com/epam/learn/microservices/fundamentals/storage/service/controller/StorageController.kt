package com.epam.learn.microservices.fundamentals.storage.service.controller

import com.epam.learn.microservices.fundamentals.logging.LogExecution
import com.epam.learn.microservices.fundamentals.storage.service.service.StorageService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@LogExecution
@RestController
@RequestMapping("/storages")
class StorageController(private val service: StorageService) {


}
