package com.example.rateanalyzer

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rateanalyzer.rateanalyzer.AnalyzeResult
import com.example.rateanalyzer.rateanalyzer.RateAnalyzer
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val scope = MainScope()

    private val analyzer = RateAnalyzer()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val myViewModel: MainViewModel by viewModels()
        val recyclerView = findViewById<RecyclerView>(R.id.listOfGrowing)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val minTextView = findViewById<TextView>(R.id.minTextView)
        val maxTextView = findViewById<TextView>(R.id.maxTextView)
        val avgTextView = findViewById<TextView>(R.id.avgTextView)

        myViewModel.result.observe(this, Observer<AnalyzeResult> {
            recyclerView.adapter = RatesRecyclerAdapter(it.growing)
            minTextView.text = "Min: " + it.min.toString()
            maxTextView.text = "Max: " + it.max.toString()
            avgTextView.text = "Avg: " + it.avg.toString()
        })

        initButtonClicker(button, myViewModel)
    }

    private fun initButtonClicker(button: Button, myViewModel: MainViewModel) {
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

