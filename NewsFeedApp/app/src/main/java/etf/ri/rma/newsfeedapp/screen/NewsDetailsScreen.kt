package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun NewsDetailsScreen(
    navController: NavController,
    newsId: String
) {
    val newsDao = remember { NewsDAO() }
    val imagaDao = remember { ImagaDAO() }
    val allNews by produceState<List<NewsItem>>(initialValue = emptyList()) {
        value = newsDao.getAllStories()
    }
    val newsItem = allNews.find { it.uuid == newsId }
    var tags by remember { mutableStateOf<List<String>>(emptyList()) }
    var relatedNews by remember { mutableStateOf<List<NewsItem>>(emptyList()) }

    LaunchedEffect(newsItem?.imageUrl) {
        newsItem?.imageUrl?.let { url ->
            try {
                tags = imagaDao.getTags(url)
            } catch (_: Exception) { }
        }
    }

    LaunchedEffect(newsItem?.uuid) {
        newsItem?.uuid?.let { uuid ->
            try {
                relatedNews = newsDao.getSimilarStories(uuid)
            } catch (_: Exception) { }
        }
    }

    if (newsItem != null) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(
                text = newsItem.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(8.dp)
                    .testTag("details_title")
            )

            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = newsItem.imageUrl,
                contentDescription = "News image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tagovi slike: ${tags.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = newsItem.snippet,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag("details_snippet")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kategorija: ${newsItem.category}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("details_category")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Izvor: ${newsItem.source}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("details_source")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Datum objave: ${newsItem.publishedDate}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("details_date")
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Povezane vijesti iz iste kategorije", style = MaterialTheme.typography.headlineSmall)

            relatedNews.forEachIndexed { index, relatedNewsItem ->
                TextButton(
                    onClick = {
                        navController.navigate("newsDetails/${relatedNewsItem.uuid}") {
                            popUpTo(navController.currentBackStackEntry?.destination?.route ?: "") {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.testTag("related_news_title_${index + 1}")
                ) {
                    Text(text = relatedNewsItem.title)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }, modifier = Modifier.testTag("details_close_button")) {
                Text("Zatvori detalje")
            }
        }
    } else {
        Text(text = "Vijest nije pronađena.", style = MaterialTheme.typography.bodyMedium)
    }
}