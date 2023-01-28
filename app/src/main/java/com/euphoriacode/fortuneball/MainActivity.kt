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
        clickShop()
    }

    private fun clickShop() {
        binding.buttonShop.setOnClickListener {
            replaceActivity(ShopActivity())
        }
    }

    private fun initSettings() {
        val file =
            File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + fileName)
        try {
            if (!checkFile(file)) {
                settings = Settings(volume = true, vibrate = true)
                saveFile(volume = true, vibrate = true)
            }
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        } finally {
            loadSettings()
        }
    }

    private fun loadSettings() {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        try {
            settings = getSettings(path)
            setIconSettings(settings)
        } catch (e: Exception) {
            e.printStackTrace()
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
                when (settings.volume) {
                    true -> settings.volume = false
                    false -> settings.volume = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                saveSettings(settings)
                setIconSettings(settings)
                showToast("Volume = " + settings.volume)
            }
        }
    }

    private fun clickVibrate() {
        binding.buttonVibrate.setOnClickListener {
            try {
                when (settings.vibrate) {
                    true -> settings.vibrate = false
                    false -> settings.vibrate = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                saveSettings(settings)
                setIconSettings(settings)
                showToast("Vibrate = " + settings.vibrate)
            }
        }
    }

    private fun saveSettings(settings: Settings) {
        saveFile(settings.volume, settings.vibrate)
    }

    private fun saveFile(volume: Boolean, vibrate: Boolean) {
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
        val json = Gson().toJson(Settings(volume, vibrate))
        val file = File(path, fileName)
        val output: Writer

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