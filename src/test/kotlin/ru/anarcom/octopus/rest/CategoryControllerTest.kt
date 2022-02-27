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
import ru.anarcom.octopus.utilus.ResourceReader
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource("json/controllers/team/getCategoriesTest.json")

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
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
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
                    .json(ResourceReader.getResource("json/controllers/team/createCategoryForCompetitionTest.json")
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
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
                    .json( ResourceReader.getResource("json/controllers/team/updateCategoryTest.json")
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/deleteCategoryTest.json")
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/getOneByCompetitionAndId.json")
                    )
            )
    }
}
