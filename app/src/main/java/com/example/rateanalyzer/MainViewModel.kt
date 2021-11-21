package com.example.rateanalyzer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rateanalyzer.rateanalyzer.AnalyzeResult
import com.example.rateanalyzer.rateanalyzer.RateAnalyzer
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.getLastModifiedTime
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val analyzer = RateAnalyzer()
    private val path =
        getApplication<Application>().applicationContext.getFilesDir().toString() + "/saved.json"
    private val file = File(path)
    var json = MutableLiveData<String>()
    var result = MutableLiveData<AnalyzeResult>()

    init {
        onStart()
    }

    fun updateRates() {
        // for clicking button
        viewModelScope.launch {
            updateAnalyze()
        }
    }

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
                    analyzer.saveToFile(file.outputStream())
                }
            } else {
                // if file with json wasn't created yet
                updateAnalyze()
            }
        }
    }

    private fun isNotOutdated(lastModified: String): Boolean {
        val currentDate = SimpleDateFormat("yyyy-mm-dd".format(Calendar.getInstance().time)).toString()
        return currentDate > lastModified.substring(0, 10)
    }

    private suspend fun updateAnalyze() {
        viewModelScope.launch {
            try {
                val tmp = analyzer.getAnalyzeResult()
                if (tmp != null) {
                    json.value = analyzer.getJson()
                    result.value = tmp
                    analyzer.saveToFile(file.outputStream())
                }
            } catch(exception: Exception) {
                println("An error has occurred: ${exception.message}")
            }
        }
    }
}