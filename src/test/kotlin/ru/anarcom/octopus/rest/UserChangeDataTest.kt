package ru.anarcom.octopus.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.util.TestClock
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class UserChangeDataTest:TestWithDb() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var clock: Clock

    private fun getTokenAndValidateUsername(
        login: String,
        password: String,
        username: String
    ): String {
        val result: MvcResult = mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"username\":\"$login\",\n" +
                                "    \"password\":\"$password\"\n" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.`is`(username)))
            .andReturn()
        val respData: MutableMap<*, *>? = ObjectMapper()
            .readValue(
                result.response.contentAsString,
                MutableMap::class.java
            )
        return respData?.get("token") as String
    }

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-09-01T00:00:00.00Z"),
            ZoneOffset.UTC
        )
    }

    @DatabaseSetup("/db/auth/user.xml")
    @Test
    @ExpectedDatabase(
        "/db/change_user_data/after_name_changing.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun changeUserData(){
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/user/change/data")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer_${
                        getTokenAndValidateUsername("username", "test", "username")
                    }")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                            "{\n" +
                                    "    \"name\": \"new name1\"\n" +
                                    "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                content().json(
                "{\n" +
                        "    \"id\": 1,\n" +
                        "    \"username\": \"username\",\n" +
                        "    \"name\": \"new name1\",\n" +
                        "    \"email\": \"email@email.ts\",\n" +
                        "    \"status\": \"ACTIVE\"\n" +
                        "}"
            ))
    }

}