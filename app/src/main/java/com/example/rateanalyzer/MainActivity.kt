package com.example.rateanalyzer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.rateanalyzer.rateanalyzer.AnalyzeResult
import com.example.rateanalyzer.rateanalyzer.RateAnalyzer
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val scope = MainScope()
    @RequiresApi(Build.VERSION_CODES.O)
    private val analyzer = RateAnalyzer()
    @SuppressLint("WorldReadableFiles")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView1)
        val button = findViewById<Button>(R.id.button)
        var deferredJson: Deferred<String?>

        scope.launch {
            deferredJson = async {
                try {
                    analyzer.getAnalyzeResult()
                    analyzer.getJson()
                } catch (exception: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "An error has occured: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    null
                }
            }
            if (deferredJson.await() == null) {
                val file = File("saved_rates.json")

                if (file.isFile) {
                    textView.text = file.bufferedReader().readText()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "haven't upload rates yet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
//            textView.text = output.await().toString()

                button.setOnClickListener(View.OnClickListener {

                })
            }
        }

    }
}
