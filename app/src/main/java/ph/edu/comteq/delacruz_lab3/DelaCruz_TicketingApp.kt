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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.delacruz_lab3.ui.theme.DelaCruz_Lab3Theme
import java.time.Instant
import java.time.Duration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ticketing(name: String, modifier: Modifier = Modifier) {
    var generalAdmissionPrice by remember { mutableIntStateOf(500) } 
    var generalAdmissionTickets by remember { mutableIntStateOf(0) }
    var freeTickets by remember { mutableIntStateOf(0) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().plus(Duration.ofDays(2)).toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= Instant.now()
                    .plus(Duration.ofDays(1)).toEpochMilli()
            }
        }
    )

    Column(
        modifier = modifier // This modifier is from Scaffold, includes padding
            .fillMaxSize()    // Fill available space after padding
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .weight(1f) // Takes up available space, pushing the Row below to the bottom
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = ph.edu.comteq.delacruz_lab3.R.drawable.ticket),
                    contentDescription = "Museum",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                )
                Text(
                    text = "Official\nTicketing Service",
                    fontSize = 32.sp,
                    fontFamily = playfairdisplayregular, 
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth() 
            ) {
                DatePicker(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(),
                    state = datePickerState,
                    title = null,
                    showModeToggle = false,
                    headline = {
                        Text(
                            text = "1. Date to Visit",
                            fontSize = 26.sp,
                            fontFamily = playfairdisplayregular
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

            Text(
                text = "2. Number of Tickets",
                fontSize = 26.sp,
                fontFamily = playfairdisplayregular,
                color = Color(0xFFd29f1b), 
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ) 
            )
            HorizontalDivider(
                Modifier.padding(horizontal = 16.dp), 
                thickness = DividerDefaults.Thickness,
                color = Color.Gray
            )
//General Admission
            TicketTypeRow(
                ticketName = "General Admission",
                ticketPriceInfo = "P$generalAdmissionPrice each.",
                quantity = generalAdmissionTickets,
                onQuantityChange = { newQuantity ->
                    generalAdmissionTickets = newQuantity.coerceAtLeast(0)
                }
            )
            HorizontalDivider(
                Modifier.padding(horizontal = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = Color.Gray
            )
//Free Ticket
            TicketTypeRow(
                ticketName = "Free Ticket",
                ticketPriceInfo = "Under 18s, Under 26s \nresidents of the EEA, \nMuseum Members, \nProfessionals",
                quantity = freeTickets,
                onQuantityChange = { newQuantity ->
                    freeTickets = newQuantity.coerceAtLeast(0)
                }
            )
            HorizontalDivider(
                Modifier.padding(horizontal = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom bar for total
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(color = 0xFFd29f1b))
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total: P${generalAdmissionTickets * generalAdmissionPrice}",
                fontSize = 26.sp,
                fontFamily = playfairdisplayregular,
                color = Color.Black
            )
            Button(
                modifier = Modifier.padding(start = 8.dp),
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                )
            ) {
                Text(
                    text = "CheckOut",
                    fontSize = 20.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color(color = 0xFFd29f1b)
                )
            }
        }
    }
}

@Composable
fun TicketTypeRow(
    ticketName: String,
    ticketPriceInfo: String,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = ticketName,
                fontSize = 18.sp,
                fontFamily = playfairdisplayregular,
                color = Color.White
            )
            Text(
                text = ticketPriceInfo,
                fontSize = 14.sp,
                fontFamily = playfairdisplayregular,
                color = Color.Gray
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { onQuantityChange(quantity - 1) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFd29f1b))
            ) {
                Text("-", color = Color.Black)
                
            }
            Text(
                text = quantity.toString(),
                modifier = Modifier.padding(horizontal = 12.dp),
                fontSize = 18.sp,
                fontFamily = playfairdisplayregular,
                color = Color.White
            )
            Button(
                onClick = { onQuantityChange(quantity + 1) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFd29f1b))
            ) {
                Text("+", color = Color.Black)
                
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
