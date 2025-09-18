package ph.edu.comteq.delacruz_lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.delacruz_lab3.ui.theme.DelaCruz_Lab3Theme


val playfairdisplayregular1 = FontFamily(
    Font(R.font.playfairdisplayregular,FontWeight.Normal)
)

val optima1 = FontFamily(
    Font(R.font.optima,FontWeight.Normal)
)

class ExploreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelaCruz_Lab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Explore(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Explore(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F3EA))
    )
    {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(40.dp)
        )
        {
            // Heading
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(alignment = Alignment.Start),
                text = "Explore",
                color = Color(0xFF723F0C),
                fontFamily = playfairdisplayregular1,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
            )


            // Divider
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color.Gray)

            // Subheading
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    "Upcoming Event",
                    color = Color.Black,
                    fontFamily = playfairdisplayregular1,
                    fontSize = 20.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {}
                ) {
                    //Ticket link
                    Text(
                        text = "Tickets",
                        color = Color.Black,
                        fontFamily = playfairdisplayregular1,
                        fontSize = 20.sp
                    )
                    Image(
                        painter = painterResource(id = R.drawable.chevron_right),
                        contentDescription = "chevron",
                        colorFilter = ColorFilter.tint(Color.Black),
                        modifier = Modifier
                            .height(17.dp)


                    )
                }
            }

            // Event Card
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                //Image
                Image(
                    painter = painterResource(id = R.drawable.renaissance),
                    contentDescription = "Renaissance Exhibition",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier

                        .fillMaxWidth()
                        .height(300.dp)
                )
                Row(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF2E2E2E))
                        .padding(18.dp)
                )
                {
                    // Date
                    Column(
                        modifier = Modifier
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            "10",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = optima1,
                            fontSize = 25.sp

                        )

                        Text(
                            "OCT",
                            color = Color.White,
                            fontFamily = optima1,
                            fontSize = 18.sp
                        )
                    }

                    // Details
                    Column(
                        modifier = Modifier
                            .padding(18.dp)
                    )
                    {
                        //Event Title
                        Text(
                            "Renaissance Exhibition",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = optima1,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        //time
                        Text(
                            "9:00 AM - 6:00 PM",
                            color = Color.White,
                            fontFamily = optima1,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        //description
                        Text(
                            "Indulge in the rich tapestry of Renaissance art",
                            color = Color(0xFFD4AF37),
                            fontFamily = optima1,
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.Underline,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        //contact
                        Text(
                            "+33 (0)1 23 45 67 89",
                            color = Color.White,
                            fontFamily = optima1,
                            fontSize = 18.sp,
                            textDecoration = TextDecoration.Underline
                        )

                    }
                }
                // Visit Gallery button
                Button(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4AF37)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Visit Gallery",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = optima1
                    )

                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ExplorePreview() {
    DelaCruz_Lab3Theme {
        Explore()
    }
}