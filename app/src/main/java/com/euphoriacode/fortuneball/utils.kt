package com.euphoriacode.fortuneball

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

const val settingsFile = "Settings.json"
const val responsesFile = "Responses.json"


val randomResponse = arrayOf("yes", "no", "maybe", "100%", "just do it")

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    startActivity(intent)
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun readFile(path: String, encoding: Charset): String {
    return Files.readAllLines(Paths.get(path), encoding)[0]
}

private fun createFile(name: String, paths: String): File {
    return File(paths, name)
}

fun checkFile(file: File): Boolean {
    return file.exists() && !file.isDirectory
}

fun toGson(jsonString: String): Settings {
    val settings = Gson().fromJson(jsonString, Settings::class.java)
    Log.d("result success", settings.volume.toString()  + " " + settings.vibrate.toString())
    return settings
}

fun toGsonResponses(jsonString: String): ResponsesArray {
    val responsesArray = Gson().fromJson(jsonString, ResponsesArray::class.java)
    return responsesArray
}

fun getJsonStringFromFile(storageDir: String, fileName: String): String {
    return File(storageDir, fileName).readText()
}

fun getSettings(storageDir: String): Settings {
    return toGson(getJsonStringFromFile(storageDir, settingsFile))
}
fun getResponses(storageDir: String): ResponsesArray {
    return toGsonResponses(getJsonStringFromFile(storageDir, responsesFile))
}