package com.spyfall.podawan.util

import org.gradle.api.Project
import java.io.File
import java.lang.System.getenv

internal fun Project.checkForAppModuleSecretFiles() {

    val secretFiledNeeded = listOf(
        // used to talk to release firebase project by app
        File("${project.projectDir.path}/src/release/google-services.json"),

        // used to talk to release firebase project by scripts
        File("${project.projectDir.path}/src/release/service-account-key.json"),

        // used to talk to release firebase project by app for qa
        File("${project.projectDir.path}/src/qa/google-services.json"),

        // used to talk to release firebase project by scripts for qa
        File("${project.projectDir.path}/src/qa/service-account-key.json"),

        // used to talk to debug firebase project by app
        File("${project.projectDir.path}/src/debug/google-services.json"),

        // used to talk to debug firebase project by scripts
        File("${project.projectDir.path}/src/debug/service-account-key.json")
    )

    fun isAppMissingSecretFiles(): Boolean = secretFiledNeeded.any { !it.isFile }

    fun installSecretFiles() {

        val paths = listOfNotNull(
            projectDir.path + "/service_key.json",
            "${project.rootDir}/service_key.json",
            "${project.rootDir}/app/service_key.json"
        )
        val serviceKeyPath = getenv("SPYFALL_SERVICE_KEY_PATH") ?: paths.firstOrNull {
            File(it).isFile
        }

        if (serviceKeyPath == null || !File(serviceKeyPath).isFile) {
            @Suppress("UseCheckOrError")
            throw IllegalStateException(
                """
                   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                    
                    Could not find the service_key.json needed to download secret files from google drive. 
                    
                    We checked the following paths: 
                    `${paths.joinToString(", ")}`
                    Please download the service_key.json here: 
                    https://drive.google.com/file/d/1t456fo07BN9NF0a3e1Ds9KNBccV1X1AQ/view?usp=share_link
                    
                    Once the file is saved, 
                    you can either
                    1. move the file to the `app` folder
                    
                    2. copy the path and add the following line to your ~/.bashrc 
                    or ~/.zshrc (depending on your set up)
                    `export SPYFALL_SERVICE_KEY_PATH="INSERT_PATH"`
                    
                    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

                """.trimIndent()
            )
        } else {
            val result = ProcessBuilder("./scripts/get_secret_files.main.kts", projectDir.path, serviceKeyPath)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
                .waitFor()

            @Suppress("TooGenericExceptionThrown")
            if (result != 0) {
                @Suppress("TooGenericExceptionThrown")
                throw Exception(
                    """
                   Failed to run ./scripts/get_secret_files.main.kts with input "$serviceKeyPath"
                   Please see AppModuleSecretFilesCheck.kt
                """.trimIndent()
                )
            } else {
                printGreen("Finished downloading all secret files")
            }
        }
    }

    if (isAppMissingSecretFiles()) {
        printRed(
            "MISSING SECRET FILES FOR PROJECT ${project.name.toUpperCase()}. " +
                    "\nUPDATING ALL SECRET FILES."
        )
        installSecretFiles()
    } else {
        printGreen("Project ${project.name} has all Secret files needed.")
    }
}
