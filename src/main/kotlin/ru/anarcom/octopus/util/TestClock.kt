package ru.anarcom.octopus.util

import com.google.common.annotations.VisibleForTesting
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

/**
 * Testable clock
 */
@Component
class TestClock : Clock() {
    val clock: Clock = systemDefaultZone()
    var testClock: Clock? = null

    override fun getZone(): ZoneId =
        if (testClock == null)
            clock.zone
        else
            testClock!!.zone

    override fun withZone(zone: ZoneId?): Clock =
        if (testClock == null)
            clock.withZone(zone)
        else
            testClock!!.withZone(zone)

    override fun instant(): Instant =
        if (testClock == null)
            clock.instant()
        else
            testClock!!.instant()

    /**
     * Fixes time. Do not use it in Production.
     */
    @VisibleForTesting
    fun setFixed(
        instant: Instant,
        zoneId: ZoneId
    ){
        testClock = fixed(instant, zoneId)
    }
}