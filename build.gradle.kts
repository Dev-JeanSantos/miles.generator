import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.IllegalArgumentException


plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"

    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"

    this.jacoco
}

group = "com.academy.fourtk"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(true)
}

dependencies {

    // Detekt
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.5")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

jacoco {
    toolVersion = "0.8.9"
}

tasks.compileKotlin {
}

val excludesPackages: Iterable<String> =
    listOf(
        "**/com.academy.fourtk.miles.generator.entrypoint.web.controllers/*"
    )

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = true
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }

    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludesPackages)
        }
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    violationRules {
        rule {
            limit {
                minimum = 0.0.toBigDecimal()
                counter = "LINE"
            }
        }
        rule {
            includes = listOf("com.academy.fourtk.*")
            limit {
                minimum = 0.80.toBigDecimal()
                counter = "BRANCH"
            }
        }
    }

    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludesPackages)
        }
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Detekt> {
    description = "Runs detekt static analisys."
    setSource(files("src/main/kotlin"))
    config.setFrom(files("config/detekt/detekt.yml"))
    debug = true
    autoCorrect = true
    finalizedBy("ktlintCheck")
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}

tasks.withType<Test> {
    loadEnv(environment, file("test.env"))
    useJUnitPlatform()
    dependsOn("ktlintCheck", "detekt")
    finalizedBy("jacocoTestCoverageVerification", "jacocoTestReport")
}

fun loadEnv(
    enviroment: MutableMap<String, Any>,
    file: File
) {
    if (!file.exists()) throw IllegalArgumentException("Failed to load envs for file, ${file.name} not found!")

    file.readLines().forEach { line ->
        if (!(line.isNotBlank() && !line.startsWith("#"))) {
            return@forEach
        }
        line.split("=", limit = 2)
            .takeIf { it.size == 2 && !it[0].isBlank() }
            ?.run { Pair(this[0].trim(), this[1].trim()) }
            ?.run {
                enviroment[this.first] = this.second
            }
    }
}
