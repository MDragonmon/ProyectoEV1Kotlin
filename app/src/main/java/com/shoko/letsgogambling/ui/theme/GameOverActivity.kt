package com.shoko.letsgogambling

import android.media.MediaPlayer
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import com.shoko.letsgogambling.ui.theme.LetsGoGamblingTheme

class GameOverActivity : ComponentActivity() {
    private var gameOverMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Reproducir música de Game Over
        gameOverMediaPlayer = MediaPlayer.create(this, R.raw.gameoversong)
        gameOverMediaPlayer?.start()

        setContent {
            LetsGoGamblingTheme {
                GameOverScreen(
                    onRetry = { navigateToMain() },
                    onBackToWelcome = { navigateToWelcome() }
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Pausar música al salir de la actividad
        gameOverMediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        // Reanudar música al regresar a la actividad
        gameOverMediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detener y liberar el MediaPlayer
        gameOverMediaPlayer?.release()
        gameOverMediaPlayer = null
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun GameOverScreen(onRetry: () -> Unit, onBackToWelcome: () -> Unit) {
    val casinoFont = FontFamily(Font(R.font.casino))
    val casino3DFont = FontFamily(Font(R.font.casino3d))

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Título y subtítulo arriba
        ) {
            // Título
            Text(
                text = "Fin del juego",
                fontFamily = casino3DFont,
                fontSize = 36.sp,
                color = Color.Black // Texto negro
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espacio pequeño

            // Subtítulo
            Text(
                text = "¿Quieres volver a jugar?",
                fontFamily = casinoFont,
                fontSize = 24.sp,
                color = Color.Black // Texto negro
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Imagen centrada
            Image(
                painter = painterResource(R.drawable.gameover),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp), // Altura fija
                contentScale = ContentScale.Fit // Ajustar imagen sin deformarla
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre imagen y botones

            // Botones justo debajo de la imagen
            Button(
                onClick = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Reintentar", fontFamily = casinoFont, color = Color.Black)
            }
            Spacer(modifier = Modifier.height(8.dp)) // Espacio pequeño
            Button(
                onClick = onBackToWelcome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Volver al inicio", fontFamily = casinoFont, color = Color.Black)
            }
        }
    }
}

@Preview
@Composable
fun GameOverScreenPreview() {
    LetsGoGamblingTheme {
        GameOverScreen(onRetry = {}, onBackToWelcome = {})
    }
}
