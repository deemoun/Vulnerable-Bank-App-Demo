// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register("allure") {
    group = "reporting"
    description = "Compatibility task for '/gradlew allure report'."
    dependsOn("allureReport")
}

tasks.register("allureReport") {
    group = "reporting"
    description = "Builds an Allure report from app test results in build/allure-results."

    val resultsDir = layout.projectDirectory.dir("app/build/allure-results")
    val reportDir = layout.buildDirectory.dir("reports/allure-report")


    doLast {
        val inputDir = resultsDir.asFile
        val outputDir = reportDir.get().asFile

        if (!inputDir.exists() || inputDir.listFiles().isNullOrEmpty()) {
            throw GradleException(
                "No Allure results found at ${inputDir.path}. Run tests first (for example ':app:testDebugUnitTest')."
            )
        }

        copy {
            from(inputDir)
            into(outputDir.resolve("allure-results"))
        }

        val indexFile = outputDir.resolve("index.html")
        indexFile.parentFile.mkdirs()
        indexFile.writeText(
            """
            <!doctype html>
            <html lang=\"en\">
              <head><meta charset=\"utf-8\"><title>Allure Results Bundle</title></head>
              <body>
                <h1>Allure results are ready</h1>
                <p>Raw results copied to <code>${outputDir.resolve("allure-results").path}</code>.</p>
                <p>Use the Allure CLI to generate a rich report:</p>
                <pre>allure generate ${outputDir.resolve("allure-results").path} -o ${outputDir.resolve("html").path} --clean</pre>
              </body>
            </html>
            """.trimIndent()
        )

        logger.lifecycle("Prepared Allure results in: ${outputDir.path}")
    }
}

tasks.register("report") {
    group = "reporting"
    description = "Compatibility task for '/gradlew allure report'."
    dependsOn("allureReport")
}
