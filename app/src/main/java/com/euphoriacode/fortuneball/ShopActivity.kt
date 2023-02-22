package com.euphoriacode.fortuneball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.euphoriacode.fortuneball.databinding.ActivityShopBinding

class ShopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }








}