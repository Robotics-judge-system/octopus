package ru.anarcom.octopus.utilus

import java.io.IOException
import java.nio.charset.StandardCharsets


class ResourceReader {
companion object {
    @kotlin.jvm.Throws(IOException::class)
    fun getResource(path: String): String {
        var path = path
        if (path.indexOf('/') == 0) {
            path = path.substring(1)
        }
        val tmp = ClassLoader.getSystemResourceAsStream(path)
        return String(tmp.readAllBytes(), StandardCharsets.UTF_8)
    }
}
}
