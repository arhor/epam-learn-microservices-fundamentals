package com.epam.learn.microservices.fundamentals.song.service.service.exception

class EntityNotFoundException(condition: String) : PropertyConditionException(condition)
