package ph.edu.comteq.delacruz_lab3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.delacruz_lab3.ui.theme.DelaCruz_Lab3Theme


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

// JSON Parsing Function
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

// Activity Class
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




@Composable
fun AppScreen() {
    val context = LocalContext.current
    val allArtworks = remember { loadArtworksFromJson(context) }
    var selectedArtworkData by remember { mutableStateOf<Pair<String, Int>?>(null) }

    val exhibitData = remember(selectedArtworkData) {
        val (name, imageId) = selectedArtworkData ?: Pair("Lady Ermine", R.drawable.lady_ermine)

        val nameToMatch = name.trim().lowercase()
        val artworkDetails = allArtworks.firstOrNull { it.title.trim().lowercase() == nameToMatch }
        val isErrorState = allArtworks.size == 1 && allArtworks.first().title.contains("Error")

        val finalArtwork = if (isErrorState) {
            allArtworks.first()
        } else {
            artworkDetails
                ?: allArtworks.firstOrNull { it.title.trim().lowercase() == "lady ermine" }
                ?: Artwork(
                    title = name,
                    years = "Data Missing",
                    born_at = "Unknown",
                    comment = "Could not find specific data for '$name' in JSON."
                )
        }

        ExhibitData(
            artwork = finalArtwork,
            imageResId = imageId
        )
    }

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

        if (selectedArtworkData != null) {
            ExhibitPage(
                exhibitData = exhibitData,
                onBackClick = { selectedArtworkData = null }
            )
        } else {
            ArtistPageContent(
                artists = MockData.artists,
                onArtworkClick = { name, resId -> selectedArtworkData = Pair(name, resId) }
            )
        }
    }
}

@Composable
fun ArtistPageContent(artists: List<Artist>, onArtworkClick: (String, Int) -> Unit) {
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

//  ArtistItem Dispatcher
@Composable
fun ArtistItem(artist: Artist, onArtworkClick: (String, Int) -> Unit) {
    val isRightAligned = artist.name == MockData.michelangelo.name

    if (isRightAligned) {
        RightAlignedArtistItem(artist = artist, onArtworkClick = onArtworkClick)
    } else {
        LeftAlignedArtistItem(artist = artist, onArtworkClick = onArtworkClick)
    }
}


@Composable
fun LeftAlignedArtistItem(artist: Artist, onArtworkClick: (String, Int) -> Unit) {
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

            Column(
                modifier = Modifier.offset(y = (-5).dp)
            ) {
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
                val artworkName = artist.artworkNames.getOrElse(index) { "Unknown Artwork" }
                val imageResId = artist.artworkImageResIds[index]

                ArtworkImage(
                    resourceId = imageResId,
                    artworkIndex = index,
                    artistName = artist.name,
                    onClick = { onArtworkClick(artworkName, imageResId) }
                )
            }
        }
    }
}

@Composable
fun RightAlignedArtistItem(artist: Artist, onArtworkClick: (String, Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.offset(y = (-5).dp)
            ) {
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
            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = artist.avatarResId),
                contentDescription = artist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 20.dp),
            reverseLayout = true,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(artist.artworkImageResIds.size) { index ->
                val artworkName = artist.artworkNames.getOrElse(index) { "Unknown Artwork" }
                val imageResId = artist.artworkImageResIds[index]

                ArtworkImage(
                    resourceId = imageResId,
                    artworkIndex = index,
                    artistName = artist.name,
                    onClick = { onArtworkClick(artworkName, imageResId) }
                )
            }
        }
    }
}

@Composable
fun ArtworkImage(resourceId: Int, artworkIndex: Int, artistName: String, onClick: () -> Unit) {
    var width = ArtworkImageStandardSize
    var height = ArtworkImageStandardSize
    var shape = RoundedCornerShape(ArtworkCornerRadius)

    when (artistName) {
        "Leonardo da Vinci" -> {
            when (artworkIndex) {
                1 -> {
                    width = 80.dp
                    height = 100.dp
                    shape = CircleShape
                }
                else -> {
                    width = 100.dp
                    height = 100.dp
                    shape = CircleShape
                }
            }
        }
        "Michelangelo" -> {
            when (artworkIndex) {
                1 -> {
                    shape = CircleShape
                }
                else -> {
                    shape = RoundedCornerShape(12.dp)
                }
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
                    Icon(
                        Icons.Filled.Fullscreen,
                        contentDescription = "Fullscreen icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
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

@Composable
fun TabSection(tabs: List<String>, selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index
            Column(
                modifier = Modifier
                    .padding(end = 40.dp)
                    .clickable { onTabSelected(index) },
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
                        modifier = Modifier
                            .width(50.dp)
                            .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                    )
                }
            }
        }
    }
}


@Composable
fun ExhibitPage(exhibitData: ExhibitData, onBackClick: () -> Unit) {
    val artwork = exhibitData.artwork
    val imageResId = exhibitData.imageResId
    val archedShape = remember {
        RoundedCornerShape(topStart = ImageArcRadius, topEnd = ImageArcRadius)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp)
                    .aspectRatio(1f / 1.5f)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = artwork.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(archedShape)
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(RenaissanceGold),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        Text(
                            text = artwork.title,
                            fontFamily = OptimaFamily,
                            color = DarkBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${artwork.years}, ${artwork.born_at}",
                            fontFamily = OptimaFamily,
                            color = DarkBackground.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(DarkBackground)
                            .clickable(onClick = onBackClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Go to details",
                            tint = RenaissanceGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .padding(top = 20.dp, bottom = 40.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.quote),
                    contentDescription = "Quotation Mark",
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                        .align(Alignment.Top)
                        .offset(y = (-5).dp),
                    alpha = 0.3f
                )

                Text(
                    text = artwork.comment,
                    fontFamily = AbeeZeeFamily,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .weight(1f)
                        .offset(x = (-15).dp)
                )
            }
        }
    }
}