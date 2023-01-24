package com.rate.rateus.library

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rate.rateus.rateus.RateApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rateApp = RateApp(this)
        rateApp.ratingDialog(R.drawable.ic_gray, R.drawable.ic_yellow, "abc@gmail.com","com.rate.rateus.library" )

    }

}