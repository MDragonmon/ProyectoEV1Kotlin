package com.shoko.letsgogambling

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoko.letsgogambling.ui.theme.LetsGoGamblingTheme

public class MainActivity : ComponentActivity() {
    private val PREFS_NAME = "GamblingPrefs"
    private val MAX_SCORE_KEY = "max_score"
    private var mainActivityMediaPlayer: MediaPlayer? = null
    private var soundEffectMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Iniciar la música de fondo en bucle
        mainActivityMediaPlayer = MediaPlayer.create(this, R.raw.background_music2)
        mainActivityMediaPlayer?.isLooping = true
        mainActivityMediaPlayer?.start()

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedMaxScore = prefs.getInt(MAX_SCORE_KEY, 0)

        setContent {
            LetsGoGamblingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FocusOnGambling(
                        modifier = Modifier.padding(innerPadding),
                        savedMaxScore = savedMaxScore,
                        onSaveMaxScore = this::saveMaxScore,
                        onGameOver = this::navigateToGameOver,
                        stopMusic = this::stopMusic,
                        startMusic = this::startMusic,
                        playSoundEffects = this::playSoundEffects
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detener y liberar el reproductor de música
        mainActivityMediaPlayer?.release()
        mainActivityMediaPlayer = null
        soundEffectMediaPlayer?.release()
        soundEffectMediaPlayer = null
    }

    private fun saveMaxScore(maxScore: Int) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(MAX_SCORE_KEY, maxScore)
        editor.apply()
    }

    private fun navigateToGameOver() {
        stopMusic()
        val intent = Intent(this, GameOverActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun stopMusic() {
        mainActivityMediaPlayer?.pause()
    }

    private fun startMusic() {
        mainActivityMediaPlayer?.start()
    }

    private fun playSoundEffects() {
        // Reproducir vine.mp3
        val vineMediaPlayer = MediaPlayer.create(this, R.raw.vine)
        vineMediaPlayer.start()

        // Reproducir disparos.mp3 al mismo tiempo
        val disparosMediaPlayer = MediaPlayer.create(this, R.raw.disparos)
        disparosMediaPlayer.start()

        // Liberar recursos cuando cada sonido termine
        vineMediaPlayer.setOnCompletionListener {
            it.release()
        }

        disparosMediaPlayer.setOnCompletionListener {
            it.release()
        }
    }
}

@Composable
fun FocusOnGambling(
    modifier: Modifier = Modifier,
    savedMaxScore: Int,
    onSaveMaxScore: (Int) -> Unit,
    onGameOver: () -> Unit,
    stopMusic: () -> Unit,
    startMusic: () -> Unit,
    playSoundEffects: () -> Unit
) {
    var result by remember { mutableStateOf(0) }
    var target by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var streak by remember { mutableStateOf(0) }
    var maxScore by remember { mutableStateOf(savedMaxScore) }
    var showScreamImage by remember { mutableStateOf(false) }
    var showScream2Image by remember { mutableStateOf(false) }

    val casinoFont = FontFamily(Font(R.font.casino))

    val imageResource = when (result) {
        0 -> R.drawable.zero
        1 -> R.drawable.one
        2 -> R.drawable.two
        3 -> R.drawable.three
        4 -> R.drawable.four
        5 -> R.drawable.five
        6 -> R.drawable.six
        7 -> R.drawable.seven
        8 -> R.drawable.eight
        else -> R.drawable.nine
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(R.drawable.fondo_pantalla2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ensures the image scales properly to fill the screen
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Score and Max Score
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.score, score),
                    fontFamily = casinoFont,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = stringResource(R.string.safeScore, maxScore),
                    fontFamily = casinoFont,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            // Number Image
            Image(
                painter = painterResource(imageResource),
                contentDescription = result.toString(),
                modifier = Modifier
                    .size(100.dp)
            )

            // Buttons Row
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        result = (0..9).random()
                        if (result == target) {
                            result = if (result == 9) result - 1 else result + 1
                        }
                        if (result > target) {
                            score += result * streak + 1
                            target = result
                            streak++
                            stopMusic()
                            playSoundEffects()
                            showScreamImage = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                showScream2Image = true
                            }, 500)
                            Handler(Looper.getMainLooper()).postDelayed({
                                showScreamImage = false
                                showScream2Image = false
                                startMusic()
                            }, 1000)
                        } else {
                            onGameOver()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.higher), fontFamily = casinoFont, color = Color.Black)
                }

                Button(
                    onClick = {
                        result = (0..9).random()
                        if (result == target) {
                            result = if (result == 9) result - 1 else result + 1
                        }
                        if (result < target) {
                            score += result * streak + 1
                            target = result
                            streak++
                            stopMusic()
                            playSoundEffects()
                            showScreamImage = true
                            startMusic()
                            Handler(Looper.getMainLooper()).postDelayed({
                                showScream2Image = true
                            }, 500)
                            Handler(Looper.getMainLooper()).postDelayed({
                                showScreamImage = false
                                showScream2Image = false
                            }, 1000)
                        } else {
                            onGameOver()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.lower), fontFamily = casinoFont, color = Color.Black)
                }

                Button(
                    onClick = {
                        if (score > maxScore) {
                            maxScore = score
                            onSaveMaxScore(maxScore)
                        }
                        score = 0
                        streak = 0
                        showScreamImage = false
                        showScream2Image = false
                    }
                ) {
                    Text(text = stringResource(R.string.save), fontFamily = casinoFont, color = Color.Black)
                }
            }
        }

        // Display Scream Image when correct
        if (showScreamImage) {
            Image(painter = painterResource(R.drawable.scream),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            )
        }

        if (showScream2Image) {
            Image(
                painter = painterResource(R.drawable.scream2),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun GamblingPreview() {
    LetsGoGamblingTheme {
        FocusOnGambling(
            savedMaxScore = 0,
            onSaveMaxScore = {},
            onGameOver = {},
            stopMusic = {},
            startMusic = {},
            playSoundEffects = {}
        )
    }
}

