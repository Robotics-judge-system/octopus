package ru.anarcom.octopus.rest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.anarcom.octopus.OctopusApplicationTests

class StateControllerTest : OctopusApplicationTests() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun checkPing() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/state/ping")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("pong"))
            .andReturn().request.contentAsString
    }
}