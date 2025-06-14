package com.example.lab5quinones

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab5quinones.ui.theme.Lab5QuinonesTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var tts: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
        enableEdgeToEdge()
        setContent {
            Lab5QuinonesTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    TextToSpeechScreen(tts)
                }
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}

@Composable
fun TextToSpeechScreen(tts: TextToSpeech) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var selectedLanguage by remember { mutableStateOf("Español") }
    var selectedVoice by remember { mutableStateOf("Default") }

    val languages = listOf("Español", "Inglés (US)")
    val voices = listOf("Default", "Femenina", "Masculina")

    Spacer(modifier = Modifier.height(20.dp))

    Text("App TRONCO - MEGAFONO",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(25.dp))

    Image(
        painter = painterResource(id = R.drawable.imagenidioma),
        contentDescription = null,
        modifier = Modifier.size(300.dp)
    )

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Escriba un texto para escuchar") },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black, fontSize = 15.sp)
    )

    DropdownSelector(
        label = "Lista de Idiomas",
        options = languages,
        selectedOption = selectedLanguage,
        onOptionSelected = { selectedLanguage = it }
    )

    Spacer(modifier = Modifier.width(5.dp))

    DropdownSelector(
        label = "Lista de Voces",
        options = voices,
        selectedOption = selectedVoice,
        onOptionSelected = { selectedVoice = it }
    )


    Spacer(modifier = Modifier.height(20.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val locale = when (selectedLanguage) {
                    "Español" -> Locale("es", "ES")
                    else -> Locale.US
                }
                tts.language = locale
                tts.speak(text.text, TextToSpeech.QUEUE_FLUSH, null, null)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Habla ahora", color = Color.White)
        }
    }
}


@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = true }) {
            Text(label)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}