package com.euphoriacode.fortuneball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.euphoriacode.fortuneball.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var settings: Settings
    private var arrayResponse = arrayOf("yes", "no", "maybe", "100%", "just do it")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSettings()

        clickButton()
        clickVolume()
        clickVibrate()
    }

    private fun initSettings() {
        val file =
            File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + fileName)

        try {
            if (checkFile(file)) {
                loadSettings()
            } else {
                settings = Settings(true, true)
                saveFile(volume = true, vibrate = true, nameFile = fileName)
                loadSettings()
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun loadSettings() {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        try {
            settings = getSettings(path)
            setSettingsButton(settings)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSettingsButton(settings: Settings) {
        when (settings.volume) {
            true -> binding.buttonVolume.setImageResource(R.drawable.ic_volume_up_24)
            false -> binding.buttonVolume.setImageResource(R.drawable.ic_volume_off_24)
        }

        when (settings.vibrate) {
            true -> binding.buttonVibrate.setImageResource(R.drawable.ic_vibration_24)
            false -> binding.buttonVibrate.setImageResource(R.drawable.ic_vibrate_off_24)
        }
    }

    private fun saveFile(volume: Boolean, vibrate: Boolean, nameFile: String) {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        val json = Gson().toJson(Settings(volume, vibrate))
        val file = File(path, nameFile)
        val output: Writer

        output = BufferedWriter(FileWriter(file))
        output.write(json.toString())
        output.close()
    }

    private fun clickButton() {
        binding.buttonClick.setOnClickListener {
            binding.textResponse.text = randomResponse(arrayResponse)
        }
    }

    private fun randomResponse(array: Array<String>): String {
        return array[(array.indices).random()]
    }

    private fun clickVolume() {
        binding.buttonVolume.setOnClickListener {
            try {
                setVolume(settings)
                showToast("Volume = " + settings.volume)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun clickVibrate() {
        binding.buttonVibrate.setOnClickListener {
            try {
                setVibrate(settings)
                showToast("Vibrate = " + settings.vibrate)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setVolume(settings: Settings) {
        when (settings.volume) {
            true -> settings.volume = false
            false -> settings.volume = true
        }
        setSettingsButton(settings)
        saveFile(settings.volume, settings.vibrate, fileName)
    }

    private fun setVibrate(settings: Settings) {
        when (settings.vibrate) {
            true -> settings.vibrate = false
            false -> settings.vibrate = true
        }
        setSettingsButton(settings)
        saveFile(settings.volume, settings.vibrate, fileName)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }
}