package ru.anarcom.octopus.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.anarcom.octopus.OctopusApplicationTests


class AuthControllerTest : OctopusApplicationTests() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DatabaseSetup("/db/auth/user.xml")
    fun correctAuthAndSelfDataTest() {
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
        val content = result.response.contentAsString
        val resp_data: MutableMap<*, *>? = ObjectMapper()
            .readValue(
                content,
                MutableMap::class.java
            )
        println(resp_data)
        var token = resp_data?.get("token")
    }

    // TODO: add not correct password and login test
}