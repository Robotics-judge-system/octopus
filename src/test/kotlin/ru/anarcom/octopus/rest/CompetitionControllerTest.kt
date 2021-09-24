package ru.anarcom.octopus.rest

import com.github.springtestdbunit.annotation.DatabaseSetup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.util.TestClock
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class CompetitionControllerTest : TestWithDb() {

    private var token = ""

    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-09-01T00:00:00.00Z"),
            ZoneOffset.UTC
        )
        token = getJwtTokenForDefaultUser()
    }

    @Test
    @DatabaseSetup("/db/auth/user.xml")
    @DisplayName("testing of competitionCreate")
    fun createCompetitionTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/create"
            )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\n" +
                            "        \"name\":\"competition_name\"\n" +
                            "}"
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer_$token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        "{" +
                                "\"id\":1," +
                                "\"name\":\"competition_name\"," +
                                "\"created\":\"2021-09-01T00:00:00Z\"," +
                                "\"updated\":\"2021-09-01T00:00:00Z\"," +
                                "\"date_to\":null," +
                                "\"date_from\":null" +
                                "}"
                    )
            )
    }

}