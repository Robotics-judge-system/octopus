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
import ru.anarcom.octopus.utilus.ResourceReader
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class ParticipantControllerTest : TestWithDb() {
    private var token = ""

    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-02-20T00:00:00.00Z"),
            ZoneOffset.UTC
        )
        token = getJwtTokenForDefaultUser()
    }

    @Test
    @DisplayName("Get all participants of team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    fun getAllParticipants() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/team/1/participant"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/participant/getTeamParticipants.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("add participant")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    fun addParticipant() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/1/participant"
            )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    ResourceReader.getResource(
                        "json/controllers/participant/addNewParticipant.json"
                    )
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(
//                MockMvcResultMatchers.content()
//                    .json(
//                        ResourceReader.getResource(
//                            "json/controllers/participant/getTeamParticipants.json"
//                        )
//                    )
//            )
    }


}