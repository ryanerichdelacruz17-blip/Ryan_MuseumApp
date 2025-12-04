package ph.edu.comteq.delacruz_lab3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import ph.edu.comteq.delacruz_lab3.ui.theme.DelaCruz_Lab3Theme

// --- Data Classes ---
data class Artwork(
    val title: String,
    val years: String,
    val born_at: String,
    val comment: String
)

data class Artist(
    val name: String,
    val years: String,
    val avatarResId: Int,
    val artworkNames: List<String>,
    val artworkImageResIds: List<Int>
)

data class ExhibitData(
    val artwork: Artwork,
    val imageResId: Int
)

// --- Mock Data ---
object MockData {
    val leonardoDaVinci = Artist(
        name = "Leonardo da Vinci",
        years = "1452 - 1519",
        avatarResId = R.drawable.leonardo_da_vinci,
        artworkNames = listOf("Mona Lisa", "Lady Ermine", "Litta Madonna"),
        artworkImageResIds = listOf(
            R.drawable.mona_lisa,
            R.drawable.lady_ermine,
            R.drawable.litta_madonna
        )
    )

    val michelangelo = Artist(
        name = "Michelangelo",
        years = "1475 - 1564",
        avatarResId = R.drawable.michelangelo,
        artworkNames = listOf("David", "Delphic Sibyl", "Torment of Saint Anthony"),
        artworkImageResIds = listOf(
            R.drawable.david,
            R.drawable.delphic_sibyl,
            R.drawable.torment_of_saint_anthony
        )
    )

    val artists = listOf(leonardoDaVinci, michelangelo)
}

// --- JSON Parsing Function ---
fun loadArtworksFromJson(context: Context): List<Artwork> {
    return try {
        val fileName = "artworks.json"
        val inputStream = context.assets.open(fileName)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val gson = com.google.gson.Gson()
        val artworkArray = gson.fromJson(jsonString, Array<Artwork>::class.java)
        Log.d("JSON_DEBUG", "Loaded ${artworkArray.size} artworks successfully using GSON")
        artworkArray.toList()
    } catch (e: Exception) {
        Log.e("JSON_ERROR", "Failed to load artworks.json from assets (GSON): ${e.message}", e)
        listOf(
            Artwork(
                title = "Error Loading JSON",
                years = "N/A",
                born_at = "N/A",
                comment = "Failed to load artworks.json. Check 1) JSON file syntax and 2) GSON dependency."
            )
        )
    }
}

// --- Fonts & Colors ---
val OptimaFamily = FontFamily(Font(R.font.optima))
val PlayfairDisplayFamily = FontFamily(Font(R.font.playfairdisplayregular))
val AbeeZeeFamily = FontFamily(
    Font(R.font.abeezee, FontWeight.Normal),
    Font(R.font.abeezee, FontWeight.Light)
)

val RenaissanceGold = Color(0xFFC7A747)
val BackgroundColor = Color(0xFFEDEADF)
val DarkBackground = Color(0xFF333333)
val ArtistTextDark = Color(0xFF5A5A5A)
val ArtistTextLight = Color(0xFF909090)
val ArtworkCornerRadius = 16.dp
val ImageArcRadius = 40.dp
val ArtworkImageStandardSize = 100.dp

// --- Main Activity ---
class ArtistPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelaCruz_Lab3Theme {
                AppScreen()
            }
        }
    }
}

// --- App Screen ---
@Composable
fun AppScreen() {
    val context = LocalContext.current
    val allArtworks = remember { loadArtworksFromJson(context) }
    var selectedArtist by remember { mutableStateOf<Artist?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background Texture",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f
        )

        if (selectedArtist != null) {
            // Create ExhibitData list for the artist
            val exhibitDataList = selectedArtist!!.artworkImageResIds.mapIndexed { index, resId ->
                val artworkName = selectedArtist!!.artworkNames.getOrElse(index) { "Unknown Artwork" }
                val artworkDetails = allArtworks.firstOrNull { it.title.lowercase() == artworkName.lowercase() }
                    ?: Artwork(
                        title = artworkName,
                        years = "Data Missing",
                        born_at = "Unknown",
                        comment = "Could not find specific data for '$artworkName'."
                    )
                ExhibitData(artwork = artworkDetails, imageResId = resId)
            }

            SwipeExhibitPage(
                exhibitDataList = exhibitDataList,
                onBackClick = { selectedArtist = null }
            )
        } else {
            ArtistPageContent(
                artists = MockData.artists,
                onArtworkClick = { artist ->
                    selectedArtist = artist
                }
            )
        }
    }
}

// --- Artist Page Content ---
@Composable
fun ArtistPageContent(artists: List<Artist>, onArtworkClick: (Artist) -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Artists", "Artworks")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection(title = "Explore the art of\nRenaissance")

        TabSection(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
        Spacer(modifier = Modifier.height(20.dp))

        if (selectedTabIndex == 0) {
            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                artists.forEach { artist ->
                    ArtistItem(artist = artist, onArtworkClick = onArtworkClick)
                }
            }
        } else {
            Text(
                text = "Artworks content coming soon...",
                modifier = Modifier.padding(20.dp),
                color = ArtistTextDark,
                fontFamily = OptimaFamily
            )
        }
    }
}

// --- Artist Item Dispatcher ---
@Composable
fun ArtistItem(artist: Artist, onArtworkClick: (Artist) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = artist.avatarResId),
                contentDescription = artist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.offset(y = (-5).dp)) {
                Text(
                    text = artist.name,
                    fontFamily = OptimaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ArtistTextDark
                )
                Text(
                    text = artist.years,
                    fontFamily = OptimaFamily,
                    fontSize = 14.sp,
                    color = ArtistTextLight
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(start = 20.dp)
        ) {
            items(artist.artworkImageResIds.size) { index ->
                val imageResId = artist.artworkImageResIds[index]
                ArtworkImage(
                    resourceId = imageResId,
                    artworkIndex = index,
                    artistName = artist.name,
                    onClick = { onArtworkClick(artist) }
                )
            }
        }
    }
}

// --- Artwork Image ---
@Composable
fun ArtworkImage(resourceId: Int, artworkIndex: Int, artistName: String, onClick: () -> Unit) {
    var width = ArtworkImageStandardSize
    var height = ArtworkImageStandardSize
    var shape = RoundedCornerShape(ArtworkCornerRadius)

    when (artistName) {
        "Leonardo da Vinci" -> {
            when (artworkIndex) {
                1 -> { width = 80.dp; height = 100.dp; shape = CircleShape }
                else -> { width = 100.dp; height = 100.dp; shape = CircleShape }
            }
        }
        "Michelangelo" -> {
            when (artworkIndex) {
                1 -> { shape = CircleShape }
                else -> { shape = RoundedCornerShape(12.dp) }
            }
        }
    }

    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(shape)
            .clickable(onClick = onClick)
    )
}

// --- Header Section ---
@Composable
fun HeaderSection(title: String) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = title.substringBefore('\n'),
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            fontFamily = PlayfairDisplayFamily,
            color = Color.Black.copy(alpha = 0.8f)
        )
        Text(
            text = title.substringAfter('\n'),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = PlayfairDisplayFamily,
            color = RenaissanceGold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Type to search...", color = Color.Gray, fontFamily = OptimaFamily) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search icon", tint = Color.Gray) },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_scan),
                        contentDescription = "Scan icon",
                        tint = Color.Gray,
                        modifier = Modifier.padding(end = 4.dp).size(20.dp)
                    )
                    Icon(Icons.Filled.Fullscreen, contentDescription = "Fullscreen icon", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray.copy(alpha = 0.7f),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.7f),
                cursorColor = RenaissanceGold,
                focusedContainerColor = Color.White.copy(alpha = 0.7f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.7f)
            ),
            singleLine = true
        )
    }
}

// --- Tab Section ---
@Composable
fun TabSection(tabs: List<String>, selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index
            Column(
                modifier = Modifier.padding(end = 40.dp).clickable { onTabSelected(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontFamily = OptimaFamily,
                    color = if (isSelected) ArtistTextDark else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (isSelected) {
                    Divider(
                        color = RenaissanceGold,
                        thickness = 3.dp,
                        modifier = Modifier.width(50.dp).clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                    )
                }
            }
        }
    }
}

// --- Swipeable Exhibit Page ---
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SwipeExhibitPage(
    exhibitDataList: List<ExhibitData>,
    onBackClick: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        HorizontalPager(
            count = exhibitDataList.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 40.dp),
            modifier = Modifier.fillMaxSize()
        ) { page ->

            val exhibitData = exhibitDataList[page]
            val artwork = exhibitData.artwork
            val imageResId = exhibitData.imageResId

            // Animate scale of current page
            val scale by animateFloatAsState(
                targetValue = if (pagerState.currentPage == page) 1f else 0.85f
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Artwork Image
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = artwork.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f / 1.5f)
                        .clip(RoundedCornerShape(topStart = ImageArcRadius, topEnd = ImageArcRadius))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Artwork Info
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = artwork.title,
                        fontFamily = OptimaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Text(
                        text = "${artwork.years}, ${artwork.born_at}",
                        fontFamily = OptimaFamily,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = artwork.comment,
                        fontFamily = AbeeZeeFamily,
                        fontSize = 16.sp,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Optional: You can add PagerIndicator at bottom
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            activeColor = RenaissanceGold,
            inactiveColor = Color.Gray.copy(alpha = 0.5f)
        )
    }
}


