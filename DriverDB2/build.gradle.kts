val seleniumVersion = "3.141.59"

plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":Core"))

    api("com.ibm.db2.jcc:db2jcc:db2jcc4")

    api("org.bouncycastle:bcprov-jdk15on:1.64")
    testCompile("junit", "junit", "4.12")
}

tasks.named<Test>("test") {
    systemProperty("file.encoding", "utf-8")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("source")
    from(sourceSets.main.get().allSource)
}

publishing {
    repositories {
        maven {
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots")
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials(PasswordCredentials::class.java) {
                username = rootProject.extra.get("ossrhUser")?.toString()
                password = rootProject.extra.get("ossrhPassword")?.toString()
            }
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourcesJar.get())
            pom {
                name.set("GTA Driver DB2")
                description.set("The db2 driver module of GTA")
                url.set("https://github.com/QualityMinds/gherkin-test-automation/tree/master/DriverDB2")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("tva")
                        name.set("Tobias Varlemann")
                        email.set("tobias.varlemann@qualityminds.de")
                    }
                    developer {
                        id.set("jfo")
                        name.set("Johann Förster")
                        email.set("johann.foerster@qualityminds.de")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/QualityMinds/gherkin-test-automation.git")
                    developerConnection.set("scm:git:https://github.com/QualityMinds/gherkin-test-automation.git")
                    url.set("https://github.com/QualityMinds/gherkin-test-automation/tree/master/DriverDB2")
                }
            }

        }
    }
}


gradle.startParameter.setContinueOnFailure(true)
