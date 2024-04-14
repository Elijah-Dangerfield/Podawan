#!/usr/bin/env kotlin

@file:Repository("https://maven.google.com")
@file:Repository("https://repo1.maven.org/maven2")
@file:Repository("https://jcenter.bintray.com")
@file:DependsOn("com.google.firebase:firebase-admin:9.1.1")
@file:DependsOn("com.google.code.gson:gson:2.8.6")
@file:DependsOn("com.google.gms:google-services:4.3.14")
@file:DependsOn("com.google.auth:google-auth-library-oauth2-http:1.14.0")

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.File
import com.google.gson.Gson
import java.io.FileInputStream

val red = "\u001b[31m"
val green = "\u001b[32m"
val reset = "\u001b[0m"

fun printRed(text: String) {
    println(red + text + reset)
}

fun printGreen(text: String) {
    println(green + text + reset)
}

fun main() {

    checkArgs()

    val debugServiceAccountJsonFile = File("app/src/debug/service-account-key.json")

    if (!debugServiceAccountJsonFile.isFile) {
        printRed(
            """The service - account - key.json files was not found.please run . / scripts / get_secret_files.main.kts to get all the secret files.""".trimIndent()
        )
        return
    }

    val debugDb = getDb(debugServiceAccountJsonFile.absolutePath)

    printGreen("Parsing packs from json")

    val packsWrapper = Gson().fromJson(File(args[0]).readText(), PacksWrapper::class.java)

    printGreen("Uploading packs to debug db")

    debugDb.uploadPacksToDb(
        packsWrapper.version.toString(),
        packsWrapper.languageCode,
        packsWrapper.packs
    )

    printGreen("Finished Uploading packs to debug db")
}

fun Firestore.uploadPacksToDb(
    version: String,
    languageCode: String,
    packs: List<LocationPack>
) {
    collection("versioned-packs")
        .document(version)
        .collection("location-packs")
        .document(languageCode)
        .set(
            mapOf(
                "type" to "location",
                "packs" to packs
            )
        )
        .get()
}

data class PacksWrapper(
    val type: String,
    val version: Int,
    val languageCode: String,
    val packs: List<LocationPack>
)

data class LocationPack(
    val name: String,
    val locations: List<Location>,
)

data class Location(
    val name: String,
    val roles: List<String>
)

fun checkArgs() {
    val isHelpCall = args.isNotEmpty() && (args[0] == "-h" || args[0].contains("help"))
    if (isHelpCall) {
        @kotlin.Suppress("MaxLineLength")
        printRed(
            """
        This script takes in a json file of packs and uploads it to the debug db as a new pack 
        Usage: ./scripts/upload_location_pack.main.kts <path_to_json>     
        """.trimIndent()
        )

        @kotlin.Suppress("TooGenericExceptionThrown")
        throw Exception(if (isHelpCall) "See Message Above" else "MUST PROVIDE ALL ARGUMENTS")
    }
}

@Suppress("TooGenericExceptionCaught")
fun getDb(serviceAccountJsonPath: String): Firestore {
    val serviceAccount = FileInputStream(serviceAccountJsonPath)
    val credentials = GoogleCredentials.fromStream(serviceAccount)

    val options = FirebaseOptions.builder().setCredentials(credentials).build()

    val app = try {
        println("Initializing Firebase app")
        FirebaseApp.initializeApp(options, serviceAccountJsonPath)
    } catch (e: IllegalStateException) {
        println("Firebase app already initialized. $e")
        null
    }
    return com.google.firebase.cloud.FirestoreClient.getFirestore(app)
}

main()

