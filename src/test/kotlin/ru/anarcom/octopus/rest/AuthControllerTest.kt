package ru.anarcom.octopus.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.anarcom.octopus.TestWithDb


class AuthControllerTest : TestWithDb() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private fun getTokenAndValidateUsername(
        login: String,
        password: String,
        username: String
    ): String {
        val result: MvcResult = mockMvc
            .perform(
                get("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"username\":\"$login\",\n" +
                                "    \"password\":\"$password\"\n" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username", `is`(username)))
            .andReturn()
        val respData: MutableMap<*, *>? = ObjectMapper()
            .readValue(
                result.response.contentAsString,
                MutableMap::class.java
            )
        return respData?.get("token") as String
    }

    @Test
    @DatabaseSetup("/db/auth/user.xml")
    @ExpectedDatabase(
        value = "/db/auth/user.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun correctAuthAndSelfDataTest() {
        val token = getTokenAndValidateUsername(
            "username",
            "test",
            "username"
        )
        mockMvc
            .perform(
                get("/api/v1/self")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer_$token")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"id\":1," +
                            "\"username\":\"username\"," +
                            "\"name\":\"name\"," +
                            "\"email\":\"email@email.ts\"}"
                )
            )
    }

    @Test
    @DatabaseSetup("/db/auth/user.xml")
    @ExpectedDatabase(
        value = "/db/auth/user.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun refreshTokenTest() {
        val result: MvcResult = mockMvc
            .perform(
                get("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"username\":\"username\",\n" +
                                "    \"password\":\"test\"\n" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username", `is`("username")))
            .andReturn()
        val respData: MutableMap<*, *>? = ObjectMapper()
            .readValue(
                result.response.contentAsString,
                MutableMap::class.java
            )
        val token = respData?.get("token")
        val refreshToken = respData?.get("refresh_token")
        mockMvc
            .perform(
                get("/api/v1/self")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer_$token")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"id\":1," +
                            "\"username\":\"username\"," +
                            "\"name\":\"name\"," +
                            "\"email\":\"email@email.ts\"}"
                )
            )
        mockMvc
            .perform(
                get("/api/v1/auth/refresh")
                    .content(
                        "{\n" +
                                "    \"refresh\":\"$refreshToken\"\n" +
                                "}"
                    )
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.username", `is`("username")))
            .andExpect(jsonPath("$.refresh_token", `is`(refreshToken)))
    }

    // TODO: add not correct password and login test
}
