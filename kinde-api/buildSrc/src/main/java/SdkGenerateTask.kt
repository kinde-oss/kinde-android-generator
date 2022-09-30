import org.gradle.api.DefaultTask
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.support.listFilesOrdered
import java.io.File

open class SdkGenerateTask : DefaultTask() {
    @Input
    val inputProperties: MapProperty<String, String> = project.objects.mapProperty<String, String>()

    @TaskAction
    fun execute() {
        val properties = inputProperties.get()
        val sdkDir = properties.get(SDK_DIR)
        val sdkPackage = properties.get(SDK_PACKAGE)
        val apiPackage = properties.get(API_PACKAGE)
        val modelPackage = properties.get(MODEL_PACKAGE)
        val apiDir = File("$sdkDir$SOURCE_FOLDER${apiPackage?.replace(".", File.separator)}")
        val files = apiDir.listFilesOrdered { file ->
            file.isFile && file.name.contains(API_CLASS_NAME, ignoreCase = true)
        }
        val apis = files.map { file ->
            val apiName = file.nameWithoutExtension
            ApiDesc(apiName.decapitalize(), apiName, file.readLines().filter { line -> line.contains("fun") })
        }

        val sdkFile = File("${properties.get(TEMPLATE_DIR)}${File.separator}$SDK_FILE")
        var sdkContent = sdkFile.readText()
        sdkContent = sdkContent.replace(
            "%IMPORTS%",
            listOf(
                "import $apiPackage.* // ktlint-disable no-wildcard-imports",
                "import $modelPackage.* // ktlint-disable no-wildcard-imports",
                "import $sdkPackage.infrastructure.ApiClient"
            ).joinToString("\n")
        )
        sdkContent = sdkContent.replace(
            "%APIS%",
            apis.map { api -> "private val ${api.fieldName}: ${api.className}" }
                .joinToString("\n")
        )
        sdkContent = sdkContent.replace(
            "%INITIALIZATION%",
            apis.map { api -> "${api.fieldName} = apiClient.createService(${api.className}::class.java)" }
                .joinToString("\n")
        )
        sdkContent = sdkContent.replace(
            "%FUNCTIONS%",
            apis.map { api ->
                api.functions.map { function -> getFuction(api.fieldName, function) }.joinToString("\n\n")
            }
                .joinToString("\n\n")
        )
        val finalSdkFile =
            File("$sdkDir$SOURCE_FOLDER${sdkPackage?.replace(".", File.separator)}${File.separator}$SDK_FILE")
        if (finalSdkFile.exists().not()) {
            finalSdkFile.createNewFile()
        }
        finalSdkFile.writeText(sdkContent)
    }

    private fun getFuction(fieldName: String, functionLine: String): String {
        val funcWithReturn = functionLine.replace(retrofitAnnotationsRegex, "")
            .replace("Call<", "")
            .substringBeforeLast(">") +
                "?"
        val calledFunc = funcWithReturn.split("):").first()
            .replace("fun", "$fieldName.")
            .replace(defaultParamRegex, ",")
            .replace(paramTypeRegex, "")
            .dropLastWhile { it == ',' }
        return "$funcWithReturn = callApi($calledFunc))"
    }

    companion object {
        const val API_PACKAGE = "apiPackage"
        const val SDK_PACKAGE = "sdkPackage"
        const val MODEL_PACKAGE = "modelPackage"
        const val SDK_DIR = "sdkDir"
        const val TEMPLATE_DIR = "templateDir"
        const val SDK_FILE = "KindeSDK.kt"
        val SOURCE_FOLDER = "${File.separator}src${File.separator}main${File.separator}kotlin${File.separator}"
        private const val API_CLASS_NAME = "api"
        private val retrofitAnnotationsRegex = Regex("(@[\\S]*(\\\"\\)))|(@[\\S]*)")
        private val paramTypeRegex = Regex("(: \\S*)")
        private val defaultParamRegex = Regex("= [\\S]*")
        private val callResultRegex = Regex("(Call<.*?>\\\\)")
    }
}

data class ApiDesc(
    val fieldName: String,
    val className: String,
    val functions: List<String>
)