package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun NewsList(news: List<NewsItem>, onNewsClick: (String) -> Unit) {
    if (news.isEmpty()) {
        MessageCard(message = "Nema dostupnih vijesti.")
    } else {
        LazyColumn(modifier = Modifier.testTag("news_list")) {
            items(news, key = { it.uuid }) { item ->
                if (item.isFeatured) {
                    FeaturedNewsCard(newsItem = item, onClick = { onNewsClick(item.uuid) })
                } else {
                    StandardNewsCard(newsItem = item, onClick = { onNewsClick(item.uuid) })
                }
            }
        }
    }
}