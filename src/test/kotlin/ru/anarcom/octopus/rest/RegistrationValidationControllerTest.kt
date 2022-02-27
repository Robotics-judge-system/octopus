package ru.anarcom.octopus.rest

import com.github.springtestdbunit.annotation.DatabaseSetup
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.anarcom.octopus.TestWithDb
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationValidationControllerTest : TestWithDb() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private fun getValidationDataForTest() =
        Stream.of(
            Arguments.of(
                "/api/v1/registration/validation/username",
                "{\"username\":\"username\"}",
                "{\"message\":\"already used\"}",
                409,
            ),
            Arguments.of(
                "/api/v1/registration/validation/username",
                "{\"username\":\"username1\"}",
                "{\"message\":\"ok\"}",
                200,
            ),
            Arguments.of(
                "/api/v1/registration/validation/email",
                "{\"email\":\"email@email.ts\"}",
                "{\"message\":\"already used\"}",
                409,
            ),
            Arguments.of(
                "/api/v1/registration/validation/email",
                "{\"email\":\"email231@email.ts\"}",
                "{\"message\":\"ok\"}",
                200,
            ),
        )

    @ParameterizedTest
    @MethodSource("getValidationDataForTest")
    @DatabaseSetup("/db/auth/user.xml")
    fun registrationValidationTest(path: String, req: String, resp: String, code: Int) {
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(path)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(req)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().`is`(code))
            .andExpect(MockMvcResultMatchers.content().json(resp))
    }
}
