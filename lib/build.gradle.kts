import java.util.*

val lombokVersion by lazy { "1.18.12" }
val slf4jVersion by lazy { "1.7.30" }
val jerseyVersion by lazy { "2.31" }
val jettyVersion by lazy { "9.4.38.v20210224" }

plugins {
    id("maven-publish")
    id("signing")
}

val sourcesJar = tasks.register("sourcesJar", Jar::class) {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

val javadocJar = tasks.register("javadocJar", Jar::class) {
    from(tasks["javadoc"])
    archiveClassifier.set("javadoc")
}

dependencies {
    implementation("io.strmprivacy.schemas:schema-common:2.0.0")
    implementation("com.github.fge:json-schema-validator:2.2.6")
    implementation("org.apache.avro:avro:1.10.0")
    implementation("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.8.8")
    api("org.glassfish.jersey.core:jersey-client:$jerseyVersion")
    api("org.glassfish.jersey.media:jersey-media-sse:$jerseyVersion")
    api("org.glassfish.jersey.inject:jersey-hk2:$jerseyVersion")

    api("org.eclipse.jetty:jetty-client:$jettyVersion")
    api("org.eclipse.jetty.http2:http2-client:$jettyVersion")
    api("org.eclipse.jetty.http2:http2-http-client-transport:$jettyVersion")
    api("org.eclipse.jetty.websocket:websocket-client:$jettyVersion")
    api("org.eclipse.jetty.websocket:websocket-common:$jettyVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("com.github.tomakehurst:wiremock:2.26.1")
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
