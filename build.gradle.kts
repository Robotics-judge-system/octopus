import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
}

group = "ru.anarcom"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("org.apache.commons:commons-lang3:3.0")
	implementation("org.liquibase:liquibase-core")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	testImplementation("io.zonky.test:embedded-database-spring-test:2.1.0")
	testImplementation("io.zonky.test:embedded-postgres:1.3.1")

	testImplementation("org.mockito:mockito-all:1.10.19")
	testImplementation("com.github.springtestdbunit:spring-test-dbunit:1.3.0")
	testImplementation("org.dbunit:dbunit:2.7.0")
	testImplementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.google.guava:guava:11.0.2")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
