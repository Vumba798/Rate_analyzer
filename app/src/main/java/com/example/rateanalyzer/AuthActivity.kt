package com.example.rateanalyzer

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rateanalyzer.ui.theme.RateAnalyzerTheme
import com.example.rateanalyzer.MainViewModel

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: MainViewModel by viewModels()
        setContent() {
            LoginScreen()
        }
    }

    fun logIn() {

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreen(model: MainViewModel = viewModel()) {
    RateAnalyzerTheme {
        var login: String by remember { mutableStateOf("")}
        LoginContent(
            login = login,
            onLoginChange = { login = it }
        )
    }
}

@Composable
fun LoginContent(login: String, onLoginChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color(0xCF000000))
            .fillMaxSize()
            .padding(0.dp, 200.dp),
        verticalArrangement = Arrangement.spacedBy(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current
        var password: String by remember { mutableStateOf("") }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.DarkGray)
        ) {
            InitLoginTextField(login, onLoginChange, focusManager)
            InitPasswordTextField(login, password, { password = it }, focusManager)
        }
        val context = LocalContext.current
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("login", login)
        Button(
            modifier = Modifier
                .size(200.dp, 50.dp),
            onClick = {
                logIn(login, password)
                startActivity(context, intent, null)
            }
        ) {
            Text("Log in")
        }
    }
}

@Composable
fun InitLoginTextField(login: String, onLoginChange: (String) -> Unit, focusManager: FocusManager) {
    TextField(
        value = login,
        placeholder = { Text("Login") },
        onValueChange = onLoginChange,
        label = { Text("Login") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
        singleLine = true
    )
}

@Composable
fun InitPasswordTextField(
    login: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    focusManager: FocusManager
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    val intent = Intent(LocalContext.current, MainActivity::class.java)
    intent.putExtra("login", login)
    val context = LocalContext.current
    TextField(
        value = password,
        placeholder = { Text("Password") },
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            logIn(login, password)
            focusManager.clearFocus()
            startActivity(context, intent, null)
        }),
        visualTransformation =
        if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val image =
                if (passwordVisibility) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(imageVector = image, "password visibility")
            }
        }
    )
}

fun logIn(login: String, password: String) {
    // TODO throw exception if incorrect login/password
}