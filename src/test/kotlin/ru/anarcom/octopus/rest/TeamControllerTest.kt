package ru.anarcom.octopus.rest

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
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

class TeamControllerTest : TestWithDb() {
    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var mockMvc: MockMvc

    private var token = ""

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-01-29T00:00:00.00Z"),
            ZoneOffset.UTC
        )
        token = getJwtTokenForDefaultUser()
    }

    @Test
    @DisplayName("Register team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/team-participant/before/register.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun normalTeamRegister() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/register"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"team_name\": \"hello robot\"," +
                            "\"participants\":[" +
                            "{\"name\":\"Ivan Ivanov\",\"team_role\":\"COACH\"}," +
                            "{\"name\":\"Семен семенов\",\"team_role\":\"PARTICIPANT\"}," +
                            "{\"name\":\"Oleg g.\",\"team_role\":\"PARTICIPANT\"}" +
                            "]}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json("{" +
                            "\"id\":1," +
                            "\"team_name\":\"hello robot\"," +
                            "\"created\":\"2022-01-29T00:00:00Z\"," +
                            "\"updated\":\"2022-01-29T00:00:00Z\"," +
                            "\"status\":\"ACTIVE\"," +
                            "\"participants\":[" +
                            "{\"id\":1," +
                            "\"name\":\"Ivan Ivanov\"," +
                            "\"created\":\"2022-01-29T00:00:00Z\"," +
                            "\"updated\":\"2022-01-29T00:00:00Z\"," +
                            "\"status\":\"ACTIVE\",\"team_role\":\"COACH\"}," +
                            "{\"id\":2,\"name\":\"Семен семенов\"," +
                            "\"created\":\"2022-01-29T00:00:00Z\"," +
                            "\"updated\":\"2022-01-29T00:00:00Z\"," +
                            "\"status\":\"ACTIVE\"," +
                            "\"team_role\":\"PARTICIPANT\"}," +
                            "{\"id\":3," +
                            "\"name\":\"Oleg g.\"," +
                            "\"created\":\"2022-01-29T00:00:00Z\"," +
                            "\"updated\":\"2022-01-29T00:00:00Z\"," +
                            "\"status\":\"ACTIVE\"," +
                            "\"team_role\":\"PARTICIPANT\"}" +
                            "]}")
            )
    }
}
