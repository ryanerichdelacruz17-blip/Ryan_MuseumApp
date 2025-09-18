package ph.edu.comteq.delacruz_lab3

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import ph.edu.comteq.delacruz_lab3.ui.theme.DelaCruz_Lab3Theme
import kotlin.jvm.java


val playfairdisplayregular = FontFamily(
    Font(ph.edu.comteq.delacruz_lab3.R.font.playfairdisplayregular,FontWeight.Normal)
)

val optima = FontFamily(
    Font(ph.edu.comteq.delacruz_lab3.R.font.optima,FontWeight.Normal)
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
fun Greeting( modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = ph.edu.comteq.delacruz_lab3.R.drawable.louvre),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color(0xAA000000), Color(0x88000000), Color.Transparent),
                        startY = 0f,
                        endY = 1000f
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
                Image(
                    painter = painterResource(id = ph.edu.comteq.delacruz_lab3.R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(90.dp)
                )
                Text(
                    text = "Experience Art",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = playfairdisplayregular,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Center Description
            Text(
                text = "We are thrilled to invite you to join us for an\nExtraordenary event that will immerse you in\nthe world or art.",
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = optima,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            // Explore Button
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DelaCruz_Lab3Theme {
        Greeting(modifier = Modifier.padding())
    }
}