package com.example.rateanalyzer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.rateanalyzer.rateanalyzer.AnalyzeResult
import com.example.rateanalyzer.rateanalyzer.Rate
import com.example.rateanalyzer.rateanalyzer.RateAnalyzer
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.UnknownHostException
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.getLastModifiedTime

class MainActivity : AppCompatActivity() {
    private val scope = MainScope()

    @RequiresApi(Build.VERSION_CODES.O)
    private val analyzer = RateAnalyzer()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView1)
        val button = findViewById<Button>(R.id.button)
        val myViewModel: MyViewModel by viewModels()
        myViewModel.json.observe(this, Observer<String> {
            textView.text = it
        })

        button.setOnClickListener {
            myViewModel.viewModelScope.launch {
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
}

@RequiresApi(Build.VERSION_CODES.O)
class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val analyzer = RateAnalyzer()
    private val path =
        getApplication<Application>().applicationContext.getFilesDir().toString() + "/saved.json"
    private val file = File(path)
    var json = MutableLiveData<String>()
    var result = MutableLiveData<AnalyzeResult>()

    init {
        onStart()
    }

    suspend fun updateRates() {
        // for clicking button
        viewModelScope.launch {
            updateAnalyze()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onStart() {

        viewModelScope.launch {
            if (file.isFile) {
                // if file is already initialized
                val lastModified = Path(file.path).getLastModifiedTime().toString()

                if (isNotOutdated(lastModified)) {
                    file.bufferedReader().use {
                        json.value = it.readText()
                    }
                } else {
                    updateAnalyze()
                }
            } else {
                // if file with json wasn't created yet
                updateAnalyze()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isNotOutdated(lastModified: String): Boolean {
        val currentDate = LocalDateTime.now().toString()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateAnalyze() {
        viewModelScope.launch {
            try {
                val tmp = analyzer.getAnalyzeResult()
                if (tmp != null) {
                    json.value = analyzer.getJson()
                    result.value = tmp
                }
            } catch(exception: UnknownHostException) {
                println("An error has occurred:  ${exception.message}")
            }
        }
    }
}
