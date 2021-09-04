package ru.anarcom.octopus.beans

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class ClockBean {
    @Bean
    fun getClock():Clock = Clock.systemDefaultZone()
}