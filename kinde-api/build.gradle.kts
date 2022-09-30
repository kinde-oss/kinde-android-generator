import java.util.Properties
import java.io.FileInputStream

val sdkPackageStr = "au.kinde.sdk"
val apiPackageStr = "$sdkPackageStr.api"
val modelPackageStr = "$apiPackageStr.model"
val sdkDirProp = "sdk_dir"
val yamlPathProp = "yaml_path"

plugins {
    id("java-library")
    id("org.openapi.generator") version "6.0.1"
}

val ktlint by configurations.creating

repositories {
    mavenCentral()
}

dependencies {
    ktlint("com.pinterest:ktlint:0.47.1")
}

fun getPathFromProperties(property: String): String? {
    val envFile = rootProject.file("env.properties")

    return if (envFile.exists()) {
        val properties = Properties()
        properties.load(FileInputStream(envFile))
        properties[property] as? String
    } else ""
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set(file(getPathFromProperties(yamlPathProp).orEmpty()).toString())
    outputDir.set(file(getPathFromProperties(sdkDirProp).orEmpty()).toString())
    apiPackage.set(apiPackageStr)
    packageName.set(sdkPackageStr)
    invokerPackage.set("$sdkPackageStr.invoker")
    modelPackage.set(modelPackageStr)
    generateApiDocumentation.set(false)
    generateApiTests.set(false)
    generateModelDocumentation.set(false)
    generateModelTests.set(false)
    skipOverwrite.set(true)
    configOptions.set(
        mapOf(
            "dateLibrary" to "threetenbp-localdatetime",
            "library" to "jvm-retrofit2",
            "serializationLibrary" to "gson"
        )
    )
}

//// Create a task using the task type
val sdkGenerateTask by tasks.registering(SdkGenerateTask::class) {
    inputProperties.set(
        mapOf(
            SdkGenerateTask.API_PACKAGE to apiPackageStr,
            SdkGenerateTask.SDK_PACKAGE to sdkPackageStr,
            SdkGenerateTask.MODEL_PACKAGE to modelPackageStr,
            SdkGenerateTask.SDK_DIR to file(getPathFromProperties(sdkDirProp).orEmpty()).toString(),
            SdkGenerateTask.TEMPLATE_DIR to rootProject.file(".${File.separator}templates").toString()
        )
    )
}
//
val ktlintFormat by tasks.creating(JavaExec::class) {
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args = listOf(
        "-F",
        "${file(getPathFromProperties(sdkDirProp).orEmpty())}${SdkGenerateTask.SOURCE_FOLDER}${
            sdkPackageStr.replace(
                ".",
                File.separator
            )
        }${File.separator}${SdkGenerateTask.SDK_FILE}"
    )
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}