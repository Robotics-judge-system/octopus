package ru.anarcom.octopus

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureEmbeddedDatabase(
	provider = DatabaseProvider.DEFAULT,
	refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD
)
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@TestExecutionListeners(
	DependencyInjectionTestExecutionListener::class,
	DirtiesContextTestExecutionListener::class,
	DbUnitTestExecutionListener::class,
)
@AutoConfigureMockMvc
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
abstract class TestWithDb {
	@Autowired
	private lateinit var mockMvc: MockMvc

	fun getTokenAndValidateUsername(
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
								"    \"login\":\"$login\",\n" +
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
}
