// etf.ri.rma.newsfeedapp.screen/NewsFeedScreen.kt
package etf.ri.rma.newsfeedapp.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.model.NewsItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import etf.ri.rma.newsfeedapp.R
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsFeedScreen(
    navController: NavController,
    news: List<NewsItem>,
    currentCategory: String,
    changeCategory: (String) -> Unit,
    startDate: String? = null,
    endDate: String? = null,
    unwantedWords: List<String> = emptyList()
) {
    val categories = listOf("Sve", "politics", "sports", "tech", "health", "science", "business")
    val filteredNews = remember(news, currentCategory, startDate, endDate, unwantedWords) {
        news.filter { newsItem ->
            val categoryMatch = if (currentCategory == "Sve") true else newsItem.category == currentCategory
            val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val newsDate = try {
                LocalDate.parse(newsItem.publishedDate, dateFormat)
            } catch (e: DateTimeParseException) {
                null
            }
            val startDateParsed = startDate?.let {
                try { LocalDate.parse(it, dateFormat) } catch (e: DateTimeParseException) { null }
            }
            val endDateParsed = endDate?.let {
                try { LocalDate.parse(it, dateFormat) } catch (e: DateTimeParseException) { null }
            }
            val dateMatch = if (newsDate != null) {
                (startDateParsed == null || !newsDate.isBefore(startDateParsed)) &&
                        (endDateParsed == null || !newsDate.isAfter(endDateParsed))
            } else {
                true
            }
            val unwantedWordsMatch = unwantedWords.all { unwantedWord ->
                !newsItem.title.contains(unwantedWord, ignoreCase = true) &&
                        !newsItem.snippet.contains(unwantedWord, ignoreCase = true)
            }
            categoryMatch && dateMatch && unwantedWordsMatch
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Vijesti", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                val tag = when (category) {
                    "Sve" -> "filter_chip_all"
                    "politics" -> "filter_chip_pol"
                    "sports" -> "filter_chip_spo"
                    "tech" -> "filter_chip_tech"
                    "science" -> "filter_chip_sci"
                    "business" -> "filter_chip_bus"
                    "health" -> "filter_chip_hea"
                    "entertainment" -> "filter_chip_ent"
                    "general" -> "filter_chip_gen"
                    "food" -> "filter_chip_foo"
                    "travel" -> "filter_chip_tra"
                    else -> ""
                }
                FilterChip(
                    modifier = Modifier
                        .testTag(tag)
                        .widthIn(min = 80.dp),
                    selected = currentCategory == category,
                    onClick = {
                        changeCategory(category)
                        Log.d("NewsFeedScreen", "FilterChip clicked. currentCategory changed to: $category")
                    },
                    label = {
                        Text(
                            text = if (category == "Sve") "Sve" else category.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            AssistChip(
                onClick = {
                    val encodedCategory = Uri.encode(currentCategory)
                    navController.navigate("filters/$encodedCategory")
                    Log.d("NewsFeedScreen", "Navigating to filters screen for category: $currentCategory")
                },
                label = { Text("Još filtera") },
                modifier = Modifier.testTag("filter_chip_more")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredNews, key = { it.uuid }) { newsItem ->
                NewsItemCard(newsItem) {
                    val encodedNewsId = Uri.encode(newsItem.uuid)
                    navController.navigate("newsDetails/$encodedNewsId")
                }
            }
        }
    }
}

@Composable
fun NewsItemCard(newsItem: NewsItem, onClick: (NewsItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(newsItem) }
            .testTag("news_item_card_${newsItem.uuid}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Log.d("DEBUG_IMAGE", "News Title: ${newsItem.title}, Image URL: ${newsItem.imageUrl}")
            newsItem.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "News image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.news),
                    error = painterResource(id = R.drawable.news)
                )
                Spacer(modifier = Modifier.height(8.dp))
            } ?: run {
                Image(
                    painter = painterResource(id = R.drawable.news),
                    contentDescription = "Placeholder image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = newsItem.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = newsItem.snippet,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = newsItem.source,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = newsItem.publishedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (newsItem.isFeatured) {
                Text(
                    text = "Istaknuto",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}