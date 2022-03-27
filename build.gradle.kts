/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.0.2/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    `maven-publish`
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13.1")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.0-jre")
}

/*
buildscript {
    dependencies {
        // Classpath
        classpath("${project.properties["build.classes"]}")
    }
}
*/

application {
    // Define the main class for the application.
    //mainClass.set("nutch.App")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "org.apache.nutch"
            artifactId = "nutch"
            version = "1.18"

            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://repository.apache.org/service/local/staging/deploy/maven2")
        }
    }
}

ant.importBuild("build.xml") {old ->"ant-${old}"}

tasks.register("REEE")
{
    println(project.properties["build.classes"])
}

tasks.register<Delete>("clean-default-lib")
{
    description = "clean the project libraries directory (dependencies)"
    delete("./build/lib")
}

tasks.register<Delete>("clean-test-lib")
{
    description = "clean the project test libraries directory (dependencies)"
    delete("./build/test/lib")
}

tasks.register<Delete>("clean-build")
{
    description = "clean the project built files"
    delete("./build")
}

tasks.register<Delete>("clean-dist")
{
    description = "clean the project dist files"
    delete("./dist")
}

tasks.register<Delete>("clean-runtime")
{
    description = "clean the project runtime area"
    delete("./runtime")
}

tasks.register<Copy>("copy-libs")
{
    description = "copy the libs in lib, which are not ivy enabled"
    from(layout.buildDirectory.dir("${project.properties["lib.dir"]}"))
    include("**/*.jar")
    into(layout.buildDirectory.dir("${project.properties["build.lib.dir"]}"))
}

tasks.register("compile-plugins")
{
    description = "compile plugins only"
    dependsOn("init","resolve-default","deploy")
}

tasks.register<org.gradle.jvm.tasks.Jar>("jar")
{
    description = "make nutch.jar"
    from(layout.buildDirectory.dir("${project.properties["conf.dir"]}/nutch-default.xml"))
    into(layout.buildDirectory.dir("${project.properties["build.classes"]}"))

    from(layout.buildDirectory.dir("${project.properties["conf.dir"]}/nutch-site.xml"))
    into(layout.buildDirectory.dir("${project.properties["build.classes"]}"))
}

tasks.register<Copy>("runtime")
{
    description = "default target for running Nutch"
    mkdir("${project.properties["runtime.dir"]}")
    mkdir("${project.properties["runtime.local"]}")
    mkdir("${project.properties["runtime.deploy"]}")

    from(layout.buildDirectory.dir("${project.properties["build.dir"]}/${project.properties["final.name"]}.job"))
    into(layout.buildDirectory.dir("${project.properties["runtime.deploy"]}"))

    from(layout.buildDirectory.dir("${project.properties["runtime.deploy"]}/bin"))
    into(layout.buildDirectory.dir("src/bin"))

    from(layout.buildDirectory.dir("${project.properties["build.dir"]}/${project.properties["final.name"]}.jar"))
    into(layout.buildDirectory.dir("${project.properties["runtime.local"]}/lib"))

    from(layout.buildDirectory.dir("${project.properties["runtime.local"]}/lib/native"))
    into(layout.buildDirectory.dir("lib/native"))

    from(layout.buildDirectory.dir("${project.properties["runtime.local"]}/conf")) {
        exclude("*.template")
    }
    into(layout.buildDirectory.dir("${project.properties["conf.dir"]}/lib"))

    from(layout.buildDirectory.dir("${project.properties["runtime.local"]}/bin"))
    into(layout.buildDirectory.dir("src/bin"))

    from(layout.buildDirectory.dir("${project.properties["runtime.local"]}/lib"))
    into(layout.buildDirectory.dir("${project.properties["build.dir"]}/lib"))

    from(layout.buildDirectory.dir("${project.properties["runtime.local"]}/plugins"))
    into(layout.buildDirectory.dir("${project.properties["build.dir"]}/plugins"))

    from(layout.buildDirectory.dir("${project.properties["runtime.local"]}/test"))
    into(layout.buildDirectory.dir("${project.properties["build.dir"]}/test"))

    doLast() {
        project.exec() {
            commandLine("chmod","ugo+x","${project.properties["runtime.deploy"]}/bin")
            commandLine("chmod","ugo+x","${project.properties["runtime.local"]}/bin")
        }
    }
}