package com.example.rateanalyzer

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.rateanalyzer.rateanalyzer.RateAnalyzer
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val scope = MainScope()

    private val analyzer = RateAnalyzer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView1)
        val button = findViewById<Button>(R.id.button)
        val myViewModel: MainViewModel by viewModels()
        myViewModel.json.observe(this, Observer<String> {
            textView.text = it
        })

        button.setOnClickListener {
            Toast.makeText(applicationContext, "Updating rates...", Toast.LENGTH_SHORT).show()
            try {
                myViewModel.updateRates()
            } catch (exception: Exception) {
                Toast.makeText(
                    applicationContext,
                    "An error has occurred: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}

