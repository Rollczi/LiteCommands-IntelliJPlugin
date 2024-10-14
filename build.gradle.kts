import org.jetbrains.changelog.markdownToHTML

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.jetbrains.intellij.platform") version "2.1.0"
    id("org.jetbrains.changelog") version "2.2.1"
}

group = "dev.rollczi"
version = "3.6.1"

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
        intellijIdeaCommunity("2024.2.3")
        bundledPlugins("com.intellij.java", "org.jetbrains.kotlin")
        instrumentationTools()
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
            sinceBuild = "242"
            untilBuild = "243.*"
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
        ides {
            recommended()
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain(17)
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