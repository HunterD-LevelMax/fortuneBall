package com.euphoriacode.fortuneball

import android.media.MediaPlayer
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
    private var arrayResponse = ResponsesArray(arrayOf())
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSettings()
        initResponses()
        clickButton()
        switchVolume()
        switchVibrate()
        clickShop()

    }

    private fun initResponses() {
        val path =
            File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString(), responsesFile)

        try {
            if (!checkFile(path)) {
                arrayResponse = ResponsesArray(randomResponse)
                saveResponses(arrayResponse)
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        } finally {
            loadResponses()
        }
    }

    //load responses from json file
    private fun loadResponses() {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        try {
            arrayResponse = getResponses(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun randomResponse(array: ResponsesArray): String {
        val random = (0..array.array.size - 1).random()
        return array.array[random]
    }

    private fun initSettings() {
        val file =
            File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString(), settingsFile)
        try {
            if (!checkFile(file)) {
                Log.d("FILE", "SAVE NEW FILE")
                settings = Settings(volume = true, vibrate = true)
                saveSettingsToFile(volume = true, vibrate = true)
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        } finally {
            loadSettings()
        }
    }

    //load settings from json file
    private fun loadSettings() {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        try {
            settings = getSettings(path)
            Log.d("FILE", "LOAD")
            setIconSettings(settings)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //add delay coroutine
    private fun clickButton() {
        binding.buttonClick.setOnClickListener {
            binding.textResponse.text = randomResponse(arrayResponse)
            playSound(settings)
            startVibrate(settings)
        }
    }

    private fun startVibrate(settings: Settings) {
        when (settings.vibrate) {
            true -> return
            else -> return
        }
    }

    //play random sound
    private fun playSound(settings: Settings) {
        val arraySound = arrayOf(R.raw.sound_1, R.raw.sound_2, R.raw.sound_3, R.raw.sound_4)
        mediaPlayer = MediaPlayer.create(this, arraySound[(arraySound.indices).random()])

        when (settings.volume) {
            true -> mediaPlayer.start()
            else -> return
        }
    }

    private fun clickShop() {
        binding.buttonShop.setOnClickListener {
            replaceActivity(ShopActivity())
        }
    }

    private fun setIconSettings(settings: Settings) {
        when (settings.volume) {
            true -> binding.buttonVolume.setImageResource(R.drawable.ic_volume_up)
            false -> binding.buttonVolume.setImageResource(R.drawable.ic_volume_off)
        }

        when (settings.vibrate) {
            true -> binding.buttonVibrate.setImageResource(R.drawable.ic_vibration)
            false -> binding.buttonVibrate.setImageResource(R.drawable.ic_vibrate_off)
        }
    }


    private fun switchVolume() {

        binding.buttonVolume.setOnClickListener {
            try {
                when (settings.volume) {
                    true -> settings.volume = false
                    false -> settings.volume = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                saveSettings(settings)
            }
        }
    }

    private fun switchVibrate() {
        binding.buttonVibrate.setOnClickListener {
            try {
                when (settings.vibrate) {
                    true -> settings.vibrate = false
                    false -> settings.vibrate = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                saveSettings(settings)
            }
        }
    }

    private fun saveSettings(settings: Settings) {
        saveSettingsToFile(settings.volume, settings.vibrate)
        setIconSettings(settings)
    }

    private fun saveSettingsToFile(volume: Boolean, vibrate: Boolean) {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        val json = Gson().toJson(Settings(volume, vibrate))
        val file = File(path, settingsFile)
        val output: Writer
        Log.d("FILE", "SAVE SETTINGS")

        output = BufferedWriter(FileWriter(file))
        output.write(json.toString())
        output.close()
    }

    private fun saveResponses(array: ResponsesArray) {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        val json = Gson().toJson(array)
        val file = File(path, responsesFile)
        val output: Writer
        Log.d("FILE", "SAVE RESPONSES")

        output = BufferedWriter(FileWriter(file))
        output.write(json.toString())
        output.close()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }
}