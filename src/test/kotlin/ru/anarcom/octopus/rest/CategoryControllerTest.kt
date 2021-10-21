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


class CategoryControllerTest : TestWithDb() {
    private var token = ""

    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-10-20T00:00:00.00Z"),
            ZoneOffset.UTC
        )
        token = getJwtTokenForDefaultUser()
    }

    @Test
    @DisplayName("test of getting all categories for competition")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
        ]
    )
    fun getCategoriesTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer_$token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        "[" +
                                "{" +
                                "\"id\":11," +
                                "\"competition_id\":1," +
                                "\"competition\":{" +
                                "\"id\":1," +
                                "\"name\":\"name\"," +
                                "\"created\":\"2017-03-31T09:30:20Z\"," +
                                "\"updated\":\"2017-03-31T09:30:20Z\"," +
                                "\"date_to\":null," +
                                "\"date_from\":null," +
                                "\"status\":\"ACTIVE\"" +
                                "}," +
                                "\"name\":\"Senior\"," +
                                "\"date_from\":null," +
                                "\"date_to\":null," +
                                "\"created\":\"2021-03-31T09:30:20Z\"," +
                                "\"updated\":\"2021-03-31T09:30:20Z\"," +
                                "\"status\":\"ACTIVE\"" +
                                "},{" +
                                "\"id\":13," +
                                "\"competition_id\":1," +
                                "\"competition\":{" +
                                "\"id\":1," +
                                "\"name\":\"name\"," +
                                "\"created\":\"2017-03-31T09:30:20Z\"," +
                                "\"updated\":\"2017-03-31T09:30:20Z\"," +
                                "\"date_to\":null," +
                                "\"date_from\":null," +
                                "\"status\":\"ACTIVE\"" +
                                "}," +
                                "\"name\":\"Junior\"," +
                                "\"date_from\":null," +
                                "\"date_to\":null," +
                                "\"created\":\"2018-03-31T09:30:20Z\"," +
                                "\"updated\":\"2018-03-31T09:30:20Z\"," +
                                "\"status\":\"ACTIVE\"" +
                                "}" +
                                "]"
                    )
            )
    }

    @Test
    @DisplayName("Create category test")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/CategoryControllerTest/one_created_category.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun createCategoryForCompetitionTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer_$token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\n" +
                            "        \"name\": \"Senior\",\n" +
                            "        \"date_from\": \"2021-03-30T09:30:20Z\",\n" +
                            "        \"date_to\": \"2021-03-31T09:30:20Z\"\n" +
                            "}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        "{\n" +
                                "    \"id\": 1,\n" +
                                "    \"competition_id\": 1,\n" +
                                "    \"competition\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"name\": \"name\",\n" +
                                "        \"created\": \"2017-03-31T09:30:20Z\",\n" +
                                "        \"updated\": \"2017-03-31T09:30:20Z\",\n" +
                                "        \"date_to\": null,\n" +
                                "        \"date_from\": null,\n" +
                                "        \"status\": \"ACTIVE\"\n" +
                                "    },\n" +
                                "    \"name\": \"Senior\",\n" +
                                "    \"date_from\": \"2021-03-30T09:30:20Z\",\n" +
                                "    \"date_to\": \"2021-03-31T09:30:20Z\",\n" +
                                "    \"created\": \"2021-10-20T00:00:00Z\",\n" +
                                "    \"updated\": \"2021-10-20T00:00:00Z\",\n" +
                                "    \"status\": \"ACTIVE\"\n" +
                                "}"
                    )
            )
    }

    @Test
    @DisplayName("update category test")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/before/default_category.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/CategoryControllerTest/after/default_category.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun updateCategoryTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer_$token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\n" +
                            "        \"name\": \"Senior_1\",\n" +
                            "        \"date_from\": \"2021-03-29T09:30:20Z\",\n" +
                            "        \"date_to\": \"2021-03-30T09:30:20Z\"\n" +
                            "}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        "{\n" +
                                "    \"id\": 1,\n" +
                                "    \"competition_id\": 1,\n" +
                                "    \"competition\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"name\": \"name\",\n" +
                                "        \"created\": \"2017-03-31T09:30:20Z\",\n" +
                                "        \"updated\": \"2017-03-31T09:30:20Z\",\n" +
                                "        \"date_to\": null,\n" +
                                "        \"date_from\": null,\n" +
                                "        \"status\": \"ACTIVE\"\n" +
                                "    },\n" +
                                "    \"name\": \"Senior_1\",\n" +
                                "    \"date_from\": \"2021-03-29T09:30:20Z\",\n" +
                                "    \"date_to\": \"2021-03-30T09:30:20Z\",\n" +
                                "    \"created\": \"2021-10-19T00:00:00Z\",\n" +
                                "    \"updated\": \"2021-10-20T00:00:00Z\",\n" +
                                "    \"status\": \"ACTIVE\"\n" +
                                "}"
                    )
            )
    }

    @Test
    @DisplayName("update category test")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/before/default_category.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/CategoryControllerTest/after/deleted_category.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun deleteCategoryTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1/category/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer_$token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        "{\n" +
                                "    \"id\": 1,\n" +
                                "    \"competition_id\": 1,\n" +
                                "    \"competition\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"name\": \"name\",\n" +
                                "        \"created\": \"2017-03-31T09:30:20Z\",\n" +
                                "        \"updated\": \"2017-03-31T09:30:20Z\",\n" +
                                "        \"date_to\": null,\n" +
                                "        \"date_from\": null,\n" +
                                "        \"status\": \"ACTIVE\"\n" +
                                "    },\n" +
                                "    \"name\": \"Senior\",\n" +
                                "    \"date_from\": \"2021-03-30T09:30:20Z\",\n" +
                                "    \"date_to\": \"2021-03-31T09:30:20Z\",\n" +
                                "    \"created\": \"2021-10-19T00:00:00Z\",\n" +
                                "    \"updated\": \"2021-10-20T00:00:00Z\",\n" +
                                "    \"status\": \"DELETED\"\n" +
                                "}"
                    )
            )
    }

    @Test
    @DisplayName("update category test")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/before/default_category.xml",
        ]
    )
    fun getOneByCompetitionAndId(){
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer_$token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        "{\n" +
                                "    \"id\": 1,\n" +
                                "    \"competition_id\": 1,\n" +
                                "    \"competition\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"name\": \"name\",\n" +
                                "        \"created\": \"2017-03-31T09:30:20Z\",\n" +
                                "        \"updated\": \"2017-03-31T09:30:20Z\",\n" +
                                "        \"date_to\": null,\n" +
                                "        \"date_from\": null,\n" +
                                "        \"status\": \"ACTIVE\"\n" +
                                "    },\n" +
                                "    \"name\": \"Senior\",\n" +
                                "    \"date_from\": \"2021-03-30T09:30:20Z\",\n" +
                                "    \"date_to\": \"2021-03-31T09:30:20Z\",\n" +
                                "    \"created\": \"2021-10-19T00:00:00Z\",\n" +
                                "    \"updated\": \"2021-10-19T00:00:00Z\",\n" +
                                "    \"status\": \"ACTIVE\"\n" +
                                "}"
                    )
            )
    }

}
