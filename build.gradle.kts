import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.19.0"
    id("org.jetbrains.changelog") version "2.5.0"
}

group = "dev.rollczi"
version = "3.11.0"

repositories {
    mavenCentral()
    maven("https://repo.eternalcode.pl/releases")
    maven("https://repo.eternalcode.pl/snapshots")

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation("dev.rollczi:litecommands-framework:${version}")
    intellijPlatform {
        intellijIdea("2026.1")
        bundledPlugins("com.intellij.java", "org.jetbrains.kotlin")
        pluginVerifier()
        zipSigner()
    }
}

intellijPlatform {
    projectName = project.name

    pluginConfiguration {
        name = "LiteCommands"
        id = "dev.rollczi.litecommands.intellijplugin"
        version.set(project.version.toString())
        description = readDescriptionFrom("README.md")

        ideaVersion {
            sinceBuild = "252"
            untilBuild = "262.*"
        }
    }

    signing {
        certificateChain.set(providers.environmentVariable("CERTIFICATE_CHAIN"))
        privateKey.set(providers.environmentVariable("PRIVATE_KEY"))
        password.set(providers.environmentVariable("PRIVATE_KEY_PASSWORD"))
    }

    publishing {
        token.set(providers.environmentVariable("PUBLISH_TOKEN"))
    }

    pluginVerification {
        // Deprecated, experimental and internal API usages are reported, but they do not fail the
        // verification - only actual compatibility problems with the supported IDEs do.
        failureLevel.set(listOf(
            VerifyPluginTask.FailureLevel.COMPATIBILITY_PROBLEMS,
            VerifyPluginTask.FailureLevel.MISSING_DEPENDENCIES,
            VerifyPluginTask.FailureLevel.INVALID_PLUGIN,
        ))

        ides {
            create(IntelliJPlatformType.IntellijIdeaCommunity, "2025.2")
            create(IntelliJPlatformType.IntellijIdea, "2026.1")
            create(IntelliJPlatformType.IntellijIdea, "2026.2")
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

fun readDescriptionFrom(file: String) = providers.fileContents(layout.projectDirectory.file(file)).asText.map {
    val start = "<!-- Plugin description -->"
    val end = "<!-- Plugin description end -->"

    with(it.lines()) {
        if (!containsAll(listOf(start, end))) {
            throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
        }
        subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
    }
}