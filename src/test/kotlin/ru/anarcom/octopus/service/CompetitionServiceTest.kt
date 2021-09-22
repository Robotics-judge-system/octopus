package ru.anarcom.octopus.service

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.util.TestClock
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class CompetitionServiceTest : TestWithDb() {

    @Autowired
    private lateinit var clock: Clock

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-09-01T00:00:00.00Z"),
            ZoneOffset.UTC
        )
    }

    @Autowired
    private lateinit var competitionService: CompetitionService

    @Test
    @DisplayName("Test for .delete method")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/service/competition-test/before/one_competition.xml"
        ]
    )
    @ExpectedDatabase(
        "/db/service/competition-test/after/one_competition.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun deleteMethodTest() {
        competitionService.deleteById(1)
    }

    @Test
    @DisplayName("Test for .hardDelete method")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/service/competition-test/before/one_competition.xml"
        ]
    )
    @ExpectedDatabase(
        "/db/auth/user.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun hardDeleteMethodTest() {
        competitionService.hardDeleteById(1)
    }

}
