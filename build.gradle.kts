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

tasks.test {
    useJUnitPlatform()
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
    targetClasses.set(setOf("domain.*", "ui.*"))
    targetTests.set(setOf("domain.*", "integration.*"))
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
