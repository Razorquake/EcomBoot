import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	id ("com.google.cloud.tools.jib") version "3.4.5"
}

group = "com.razorquake"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
	implementation("org.springframework.cloud:spring-cloud-config-server")
	implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

jib{
	from.image = "gcr.io/distroless/java17"
	to.image = "razorquake/config-server"
	container.ports = listOf("8888")
	extraDirectories {
		paths {
			path {
				// The 'from' path is resolved right away
				setFrom(project.layout.projectDirectory.dir("healthcheck"))

				// The 'into' string is fixed
                into = "/app"
			}
		}
	}
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {

	imageName = "config-server"
	builder = "paketobuildpacks/builder-jammy-java-tiny"

}
