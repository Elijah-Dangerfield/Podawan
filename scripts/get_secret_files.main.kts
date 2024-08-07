#!/usr/bin/env kotlin

@file:Repository("https://repo1.maven.org/maven2")
@file:DependsOn("com.google.auth:google-auth-library-oauth2-http:1.14.0")
@file:DependsOn("com.google.apis:google-api-services-drive:v3-rev197-1.25.0")

import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

// These file ids can be found in the url of the sharable google drive links to these files
// ex: https://drive.google.com/file/d/1IIkODW-U5wvumwAk7SrHWxSil54Wp92Y/view?usp=drive_link
// id would be 1IIkODW-U5wvumwAk7SrHWxSil54Wp92Y
// IMPORTAT: These files have to be shared with the service account email in order to be downloaded
data class FileInfo(val id: String, val pathsToStore: List<String>)

// TODO debug builds dont need the release stuff. Same for the service account keys.
// Neither of these are used in day to day. Seems odd to require them to be installed.

// Used to talk to the release firebase project
val releaseGoogleServicesFileInfo =
    FileInfo(
        // https://drive.google.com/file/d/1UwdN7W1Fmy4Hp1Q2w0BSiGEHJu74cWnQ/view?usp=drive_link
        id = "1Gc_xN1FrxB61j_7cJgyVV_M0xniN2TEJ",
        pathsToStore = listOf(
            "src/release/google-services.json",
            "src/qa/google-services.json",
        )
    )

// Used to make changes to the release firestore project via scripts
val releaseServiceAccountKeyFileInfo =
    FileInfo(
        // https://drive.google.com/file/d/1iv8zTDAaF5Nqgr2mwzGITPgC_b2lmU_I/view?usp=drive_link
        id = "1iv8zTDAaF5Nqgr2mwzGITPgC_b2lmU_I",
        pathsToStore = listOf(
            "src/release/service-account-key.json",
            "src/qa/service-account-key.json",
        )
    )

// Used to talk to the debug firebase project

val debugGoogleServicesFileInfo =
    FileInfo(
        id = "1OCFEqJWoO-3xPNgKzW7fEboQ5FlR6DFa",
        pathsToStore = listOf("src/debug/google-services.json")
    )

// Used to make changes to the debug firestore project via scripts
val debugServiceAccountKeyFileInfo =
    FileInfo(
        //https://drive.google.com/file/d/11EFkoDwNz3th28UQAIdODWkZzGiNYZsD/view?usp=drive_link
        id = "11EFkoDwNz3th28UQAIdODWkZzGiNYZsD",
        pathsToStore = listOf("src/debug/service-account-key.json")
    )

val fileInfoList = listOf(
    releaseGoogleServicesFileInfo,
    releaseServiceAccountKeyFileInfo,
    debugGoogleServicesFileInfo,
    debugServiceAccountKeyFileInfo,
)

val red = "\u001b[31m"
val green = "\u001b[32m"
val reset = "\u001b[0m"

fun printRed(text: String) {
    println(red + text + reset)
}

fun printGreen(text: String) {
    println(green + text + reset)
}

val isHelpCall = args.isNotEmpty() && (args[0] == "-h" || args[0].contains("help"))
if (isHelpCall) {
    printRed(
        """
        This script collects every required files to run the debug and release variants
        from google drive using a service account key file. You must either pass in the path to the service_key.json
        file or have it installed in the root directory. 
        
        You can download the service-key.json file from the following link: 
        https://drive.google.com/file/d/1t456fo07BN9NF0a3e1Ds9KNBccV1X1AQ/view?usp=share_link
        
        Usage: ./create_google_json_files.main.kts [project root path, optional serviceAccountKeyPath]
    """.trimIndent()
    )

    @Suppress("TooGenericExceptionThrown")
    throw Exception("See Message Above")
}

val rootPath = args[0]

val serviceAccountKeyPath = args.getOrNull(1) ?: run {
    if (File("service_key.json").isFile) return@run "service_key.json"
    if (File("app/service_key.json").isFile) return@run "app/service_key.json"

    printRed(
        """
        You must either pass in the path to the service_key.json
        file or have it installed in the root directory. 
        
        You can download the service_key.json file from the following link: 
        https://drive.google.com/file/d/1t456fo07BN9NF0a3e1Ds9KNBccV1X1AQ/view?usp=share_link
    """.trimIndent()
    )
    @Suppress("TooGenericExceptionThrown")
    throw Exception("No service_key.json file found")
}

@Suppress("TooGenericExceptionCaught", "NestedBlockDepth")
fun getFiles() {
    val googleDriveServiceAccountKey = File(serviceAccountKeyPath)
    val credentials = ServiceAccountCredentials.fromStream(googleDriveServiceAccountKey.inputStream())
    val scopedCredentials = credentials.createScoped(listOf(DriveScopes.DRIVE))

    val transport: HttpTransport = NetHttpTransport()
    val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()

    val drive = Drive.Builder(
        transport,
        jsonFactory,
        HttpCredentialsAdapter(scopedCredentials)
    )
        .setApplicationName("Podawan")
        .build()

    ls()

    var successfulFetches = 0
    fileInfoList.forEach {
        try {

            it.pathsToStore.forEach {
                val path = "$rootPath/$it"

                val file = File(path)
                if (!file.isFile) {
                    file.createNewFile()
                }
            }

            val driveFile = drive.files().get(it.id).execute()

            if (driveFile.mimeType == "application/json") {
                val outputStream = ByteArrayOutputStream()
                drive.files().get(it.id).executeMediaAndDownloadTo(outputStream)
                val outputFiles = it.pathsToStore.map {
                    val path = "$rootPath/$it"
                    FileOutputStream(path)
                }

                outputFiles.forEach { output ->
                    outputStream.writeTo(output)
                }
            }

            successfulFetches += 1
        } catch (t: Throwable) {
            printRed("Error getting file ${it.pathsToStore}")
            t.printStackTrace()
        }
    }

    if (successfulFetches == fileInfoList.size) {
        printGreen("Downloaded all secret files")
    } else {
        printRed("DID NOT Download all secret files")
        @Suppress("TooGenericExceptionThrown")
        throw Exception("See Message Above")
    }
}

@Suppress("PrintStackTrace")
fun ls() {
    try {
        val process = Runtime.getRuntime().exec("ls -la")
        val reader = BufferedReader(InputStreamReader(process.inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

getFiles()
