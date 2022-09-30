plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())

    implementation(kotlin("gradle-plugin", "1.6.10"))
}