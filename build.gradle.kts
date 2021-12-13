import org.ajoberstar.grgit.Grgit
import java.util.*

plugins {
    id("java-library")
    id("org.ajoberstar.grgit") version "4.1.1"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

buildscript {
    tasks.named<Wrapper>("wrapper") {
        gradleVersion = "6.8.2"
        distributionType = Wrapper.DistributionType.ALL
    }
}

tasks.withType(GradleBuild::class) {
    // To prevent clashing with directory names and project names
    buildName = project.name
}

val branch = System.getenv("CI_COMMIT_REF_NAME") ?: Grgit.open(mapOf("dir" to project.file("."))).branch.current().name
val tag = System.getenv("CI_COMMIT_TAG")
ext["tag"] = tag

rootProject.version = if (tag != null || branch == "master") project.version else "${project.version}-SNAPSHOT"

nexusPublishing {
    packageGroup.set("io.strmprivacy")

    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(base64Decode("sonatypeUsername"))
            password.set(base64Decode("sonatypePassword"))
        }
    }
}

allprojects {
    version = rootProject.version

    apply {
        plugin("java-library")
    }

    buildscript {
        repositories {
            mavenLocal()
            mavenCentral()
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Jar> {
        onlyIf { !sourceSets["main"].allSource.files.isEmpty() }
    }
}


fun base64Decode(prop: String): String? {
    return project.findProperty(prop)?.let {
        String(Base64.getDecoder().decode(it.toString())).trim()
    }
}
