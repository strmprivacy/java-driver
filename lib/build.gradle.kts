import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import java.util.*

val slf4jVersion = "2.0.7"
val jettyVersion = "11.0.15"
val junitVersion = "5.9.3"

plugins {
    id("maven-publish")
    id("signing")
    id("com.github.ben-manes.versions") version "0.47.0"
}

dependencies {
    implementation("io.strmprivacy.schemas:schema-common:2.0.1")
    implementation("org.apache.avro:avro:1.11.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    api("org.slf4j:slf4j-api:$slf4jVersion")
    api("org.eclipse.jetty:jetty-client:$jettyVersion")
    api("org.eclipse.jetty.http2:http2-client:$jettyVersion")
    api("org.eclipse.jetty.http2:http2-http-client-transport:$jettyVersion")

    testImplementation("io.strmprivacy.schemas:demo-avro:1.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("ch.qos.logback:logback-classic:1.4.8")
    testImplementation("com.github.tomakehurst:wiremock:3.0.0-beta-10")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

val sourcesJar = tasks.register("sourcesJar", Jar::class) {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

val javadocJar = tasks.register("javadocJar", Jar::class) {
    from(tasks["javadoc"])
    archiveClassifier.set("javadoc")
}

tasks.withType<Test> {
    useJUnitPlatform()
    maxParallelForks = Runtime.getRuntime().availableProcessors()
    testLogging {

        // set options for log level LIFECYCLE
        events(FAILED)

        // set options for log level DEBUG
        debug {
            events(STARTED, SKIPPED, FAILED)
        }

        info.events = setOf(FAILED, SKIPPED)
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])

            artifact(sourcesJar)
            artifact(javadocJar)

            groupId = "io.strmprivacy"
            artifactId = "java-driver"
            version = project.version.toString()

            pom {
                name.set("$groupId:$artifactId")
                description.set("Java driver for interacting with STRM Privacy.")
                url.set("https://strmprivacy.io")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        name.set("Stream Machine B.V.")
                        email.set("apis@strmprivacy.io")
                        organization.set("Stream Machine B.V.")
                        organizationUrl.set("https://strmprivacy.io")
                    }
                }

                scm {
                    url.set("https://github.com/strmprivacy/java-driver")
                    connection.set("scm:git:ssh://git@github.com:strmprivacy/java-driver.git")
                    developerConnection.set("scm:git:ssh://git@github.com:strmprivacy/java-driver.git")
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(base64Decode("gpgPrivateKey"), base64Decode("gpgPassphrase"))
    sign(*publishing.publications.toTypedArray())
}

tasks.withType<Sign>().configureEach {
    onlyIf { rootProject.extra["tag"] != null }
}

tasks.findByName("publish")?.dependsOn("build")

fun base64Decode(prop: String): String? {
    return project.findProperty(prop)?.let {
        String(Base64.getDecoder().decode(it.toString())).trim()
    }
}
