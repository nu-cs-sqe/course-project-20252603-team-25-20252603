plugins {
    id("java")
    id("application")
    id("checkstyle")
    id("com.github.spotbugs") version "6.0.26"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

application {
    mainClass.set("ui.Main")
}

tasks.compileJava {
    options.release = 11
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("integrationTest") {
    description = "Runs only tests tagged with @Tag(\"integration\")."
    group = "verification"
    useJUnitPlatform {
        includeTags("integration")
    }
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    shouldRunAfter(tasks.test)
}

checkstyle {
    toolVersion = "10.18.2"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    maxWarnings = 0
}

spotbugs {
    toolVersion.set("4.8.6")
    ignoreFailures.set(false)
    showStackTraces.set(true)
    excludeFilter.set(file("config/spotbugs/excludeFilter.xml"))
    reportLevel.set(com.github.spotbugs.snom.Confidence.valueOf("HIGH"))
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    reports.create("html") {
        required.set(true)
        outputLocation.set(layout.buildDirectory.file("reports/spotbugs/${name}.html"))
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "ui/**",
                    "**/*Type.class",
                    "**/PlayerColor.class"
                )
            }
        })
    )
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
    finalizedBy(tasks.pitest)
}

tasks.build {
    dependsOn("pitest")
}

pitest {
    targetClasses.set(setOf("domain.*"))
    targetTests.set(setOf("domain.*", "integration.*"))
    excludedClasses.set(setOf(
        "domain.board.TerrainType",
        "domain.deck.DevelopmentCardType",
        "domain.play.ResourceType",
        "domain.player.PlayerColor"
    ))
    junit5PluginVersion.set("1.2.1")
    pitestVersion.set("1.15.0")

    threads.set(4)
    outputFormats.set(setOf("HTML"))
    timestampedReports.set(false)
    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true)
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage.set(true)
}
