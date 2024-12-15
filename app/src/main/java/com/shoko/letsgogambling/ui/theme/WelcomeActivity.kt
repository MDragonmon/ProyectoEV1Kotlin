package com.shoko.letsgogambling

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoko.letsgogambling.ui.theme.LetsGoGamblingTheme

class WelcomeActivity : ComponentActivity() {
    companion object {
        var mediaPlayer: MediaPlayer? = null // MediaPlayer compartido entre actividades
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el MediaPlayer si no está ya creado
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
            mediaPlayer?.isLooping = true // Reproduce en bucle
            mediaPlayer?.start() // Inicia la reproducción
        }

        setContent {
            LetsGoGamblingTheme {
                var isMuted by remember { mutableStateOf(false) }
                var volume by remember { mutableStateOf(1.0f) }

                WelcomeScreen(
                    onStartGame = { navigateToGame() },
                    onMuteToggle = {
                        isMuted = !isMuted
                        if (isMuted) {
                            mediaPlayer?.setVolume(0f, 0f) // Silenciar
                        } else {
                            mediaPlayer?.setVolume(volume, volume) // Restaurar volumen
                        }
                    },
                    volume = volume,
                    onVolumeChange = { newVolume ->
                        volume = newVolume
                        if (!isMuted) {
                            mediaPlayer?.setVolume(volume, volume) // Ajustar volumen
                        }
                    }
                )
            }
        }
    }

    private fun navigateToGame() {
        // Detener y liberar el MediaPlayer
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Cierra WelcomeActivity para evitar volver atrás
    }

    override fun onDestroy() {
        super.onDestroy()
        // Liberar el MediaPlayer si aún no se ha hecho
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
fun WelcomeScreen(
    onStartGame: () -> Unit,
    onMuteToggle: () -> Unit,
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    // Fuente personalizada para botones
    val casinoFontFamily = FontFamily(Font(R.font.casino3d))

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fondo de pantalla
        Image(
            painter = painterResource(id = R.drawable.fondo_pantalla),
            contentDescription = "Fondo de pantalla",
            contentScale = ContentScale.Crop, // Ajusta la imagen al tamaño de la pantalla
            modifier = Modifier.fillMaxSize()
        )

        // Contenido superpuesto al fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween, // Distribuye los elementos
            horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
        ) {
            // Título en la parte superior con tamaño aumentado
            Text(
                text = "¡Bienvenido a Let's Go Gambling!",
                fontSize = 24.sp, // Tamaño de la fuente aumentado
                modifier = Modifier.padding(top = 16.dp)
            )

            // Botones en el centro
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado entre botones
            ) {
                Button(onClick = onStartGame) {
                    Text(
                        text = "Empezar Juego",
                        fontFamily = casinoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp // Tamaño de fuente ajustable
                    )
                }
                Button(onClick = onMuteToggle) {
                    Text(
                        text = "Silenciar/Reanudar Música",
                        fontFamily = casinoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            // Barra de sonido en la parte inferior
            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Subir/Bajar Volumen",
                    fontSize = 18.sp)
                Slider(
                    value = volume,
                    onValueChange = { onVolumeChange(it) },
                    valueRange = 0f..1f,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
