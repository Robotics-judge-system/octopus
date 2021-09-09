package ru.anarcom.octopus.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Util for auto creating of logger in class.
 * (like @slf4j in Lombok)
 */
inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}