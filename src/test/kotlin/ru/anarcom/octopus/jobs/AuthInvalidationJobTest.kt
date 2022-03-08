package ru.anarcom.octopus.jobs

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.jobs.auth.AuthInvalidationJob
import ru.anarcom.octopus.util.TestClock
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class AuthInvalidationJobTest : TestWithDb() {
    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var authInvalidationJob: AuthInvalidationJob

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-03-08T00:00:00.00Z"),
            ZoneOffset.UTC
        )
    }

    @Test
    @DisplayName("normal invalidation")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/jobs/before/many_auth.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/jobs/after/many_auth_after_invalidation_job.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun invalidateAuthJobTest() {
        authInvalidationJob.invalidate(15)
    }
}
