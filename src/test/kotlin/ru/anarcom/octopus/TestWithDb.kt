package ru.anarcom.octopus

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener

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
abstract class TestWithDb
