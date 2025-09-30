package ph.edu.comteq.delacruz_lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.delacruz_lab3.ui.theme.DelaCruz_Lab3Theme
import java.time.Instant
import java.time.Duration

class DelaCruz_TicketingApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelaCruz_Lab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ticketing(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ticketing(name: String, modifier: Modifier = Modifier) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().
            plus(Duration.ofDays(2)).toEpochMilli(),
        selectableDates = object : SelectableDates { // Made anonymous object explicit for clarity
            override fun isSelectableDate(utcTimeMillis: Long): Boolean { // Corrected typo here
                return utcTimeMillis >= Instant.now()
                    .plus(Duration.ofDays(1)).toEpochMilli()
            }
        }
    )



    Column(
        modifier = modifier.background(Color.Black)
    ){
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ){
            Box(
                modifier = Modifier.fillMaxWidth().height(230.dp),
                contentAlignment = Alignment.Center
            ){

                Image(
                    painter = painterResource(id = ph.edu.comteq.delacruz_lab3.R.drawable.ticket),
                    contentDescription = "Museum",
                    modifier = Modifier.fillMaxWidth().height(230.dp),
                    contentScale = ContentScale.Crop
                )

                // Black overlay
                Box(
                    modifier = Modifier.fillMaxWidth().height(230.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                )
                Text(
                    text = "Official\nTicketing Service",
                    fontSize = 32.sp,
                    fontFamily = playfairdisplayregular, // Ensure this font is defined
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
            // inner container for date ticket types
            Column(
                modifier = Modifier.fillMaxWidth() // Removed .height(20.dp)
            ){
                DatePicker(
                    modifier = Modifier.padding(0.dp).fillMaxWidth(),
                    state = datePickerState,
                    title = null,
                    showModeToggle = false,
                    headline = {
                        Text(
                            text = "1. Date to Visit",
                            fontSize = 26.sp,
                            fontFamily = playfairdisplayregular // Ensure this font is defined
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        titleContentColor = Color(color = 0xFFd29f1b),
                        headlineContentColor = Color(color = 0xFFd29f1b),
                        weekdayContentColor = Color(color = 0xFFd29f1b),
                        containerColor = Color.Transparent,
                        dayContentColor = Color.White,
                        todayContentColor = Color(color = 0xFFd29f1b),
                        todayDateBorderColor = Color(color = 0xFFd29f1b),
                        selectedDayContentColor = Color.Black,
                        selectedYearContentColor = Color(color = 0xFFd29f1b),
                        disabledDayContentColor = Color.Gray
                    )
                )
            }
        // General Admission ticket


        // Free Tickets
        }
        // Bottom bar for total
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(color = 0xFFd29f1b))
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically, // Added
            horizontalArrangement = Arrangement.SpaceBetween // Added
        ){
            Text(
                text = "Total: P500",
                fontSize = 26.sp,
                fontFamily = playfairdisplayregular, // Ensure this font is defined
                color = Color.Black
            )
            Button(
                modifier = Modifier.padding(start = 8.dp), // Adjusted padding slightly
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                )
            ){
                Text(
                    text = "CheckOut",
                    fontSize = 20.sp,
                    fontFamily = playfairdisplayregular, // Ensure this font is defined
                    color = Color(color=0xFFd29f1b)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketingPreview2() {
    DelaCruz_Lab3Theme {
        Ticketing("Android")
    }
}
