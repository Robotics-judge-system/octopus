package ru.anarcom.octopus.beans

import Util.TestClock
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class ClockBean {

    @Bean
    fun getClock():Clock = TestClock()
}