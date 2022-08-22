package com.epam.learn.microservices.fundamentals.storage.service.service.exception

import com.epam.learn.microservices.fundamentals.storage.service.service.exception.PropertyConditionException

class EntityNotFoundException(condition: String) : PropertyConditionException(condition)
