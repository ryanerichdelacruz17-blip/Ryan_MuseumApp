package ph.edu.comteq.delacruz_lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ph.edu.comteq.delacruz_lab3.ui.theme.DelaCruz_Lab3Theme

val playfairdisplayregular = FontFamily(
    Font(ph.edu.comteq.delacruz_lab3.R.font.playfairdisplayregular, FontWeight.Normal)
)

val optima = FontFamily(
    Font(ph.edu.comteq.delacruz_lab3.R.font.optima, FontWeight.Normal)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelaCruz_Lab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    // Animation States
    var showMuseum by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showIntro by remember { mutableStateOf(false) }

    // Reveal progress (0 → 1)
    val revealProgress by animateFloatAsState(
        targetValue = if (showMuseum) 1f else 0f,
        animationSpec = tween(1200)
    )

    // Fade-in progress for museum image
    val fadeProgress by animateFloatAsState(
        targetValue = if (showMuseum) 1f else 0f,
        animationSpec = tween(1200)
    )

    // Museum pan effect (slight downward movement)
    val panOffset by animateFloatAsState(
        targetValue = if (showMuseum) 0f else -50f,
        animationSpec = tween(1500)
    )

    // Run animation sequence
    LaunchedEffect(Unit) {
        delay(200)
        showMuseum = true
        delay(1300)
        showTitle = true
        delay(2000)
        showIntro = true
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ⭐ Museum Image Reveal + Fade + Pan
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { clip = true }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1000.dp)
                    .graphicsLayer { clip = true }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.louvre),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            clip = true
                            shape = RectangleShape
                            alpha = fadeProgress            // fade-in effect
                            translationY = panOffset        // slight pan downward
                        }
                        .height((revealProgress * 1000).dp)
                        .align(Alignment.TopStart),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Overlay gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xAA000000),
                            Color(0x88000000),
                            Color.Transparent
                        )
                    )
                )
        )

        // Content Layer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo + Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                if (revealProgress == 1f) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.size(90.dp)
                    )
                }

                if (showTitle) {
                    TypewriterText(
                        text = "Experience Art",
                        fontSize = 34.sp,
                        fontFamily = playfairdisplayregular,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        speed = 65L
                    )
                }
            }

            // Introduction text
            if (showIntro) {
                TypewriterText(
                    text = "We are thrilled to invite you to join us for an\nextraordinary event that will immerse you in\nthe world of art.",
                    fontSize = 18.sp,
                    fontFamily = optima,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    speed = 25L
                )
            }

            // Button
            if (showIntro) {
                Button(
                    onClick = {
                        val intent = Intent(context, ExploreActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDAA520)
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Start Exploring",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = playfairdisplayregular
                    )
                }
            }
        }
    }
}

// Typewriter text composable
@Composable
fun TypewriterText(
    text: String,
    speed: Long = 40L,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontFamily: FontFamily,
    fontWeight: FontWeight? = null,
    color: Color,
    textAlign: TextAlign = TextAlign.Start
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        displayedText = ""
        text.forEachIndexed { i, _ ->
            displayedText = text.substring(0, i + 1)
            delay(speed)
        }
    }

    Text(
        text = displayedText,
        fontSize = fontSize,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        color = color,
        textAlign = textAlign
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DelaCruz_Lab3Theme {
        Greeting(modifier = Modifier.padding())
    }
}
