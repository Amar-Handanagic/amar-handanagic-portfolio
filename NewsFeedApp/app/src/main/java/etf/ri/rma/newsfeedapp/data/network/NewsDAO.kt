package etf.ri.rma.newsfeedapp.data.network

import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.data.network.api.NewsApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import etf.ri.rma.newsfeedapp.data.toNews
import etf.ri.rma.newsfeedapp.model.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class NewsDAO {
    companion object {
        private var api = RetrofitInstance.api
        private val apiToken = "SfOBIHMyH3Z3SKTgc1UeFvYgIlA8wWsgkGQEnenE"
        val cachedNews = NewsData.getAllNews().toMutableList()
        private val lastFetchTimes = mutableMapOf<String, Long>()
        private const val delay = 30_000L
        private val similarStoriesCache = mutableMapOf<String, List<NewsItem>>()
    }
    fun setApiService(newApi: NewsApiService) {
        api = newApi
    }
    fun getAllStories(): List<NewsItem> {
        cachedNews.forEach { it.isFeatured = false }
        return cachedNews
    }
    /*suspend fun getNewsByUUID(uuid: String): NewsItem = withContext(Dispatchers.IO) {
        val cached = cachedNews.find { it.uuid == uuid }
        if (cached != null) {
            return@withContext cached
        }
        try {
            val response = api.getNewsByUUID(uuid=uuid, token=apiToken)
            if(response.data.isNotEmpty()){
                val formatted = response.data[0].toNews()
                if(cachedNews.none { it.uuid == formatted.uuid }) {
                    cachedNews.add(formatted)
                }
                return@withContext formatted
            }
            else{
                throw InvalidUUIDException("No news found for UUID: $uuid")
            }
        } catch (e: Exception) {
            println("Error fetching news by UUID: ${e.message}")
            return@withContext NewsItem()
        }
    }*/
    suspend fun getSimilarStories(uuid: String): List<NewsItem> = withContext(Dispatchers.IO){
        similarStoriesCache[uuid]?.let {
            return@withContext it
        }
        try {
            UUID.fromString(uuid)
        } catch(e: Exception) {
            throw InvalidUUIDException(e.message.toString())
        }
        try {
            val similarStoriesFromApi = api.getSimilarStories(uuid = uuid, token = apiToken, limit = 2)
            val formattedStories = similarStoriesFromApi.data.map { it.toNews() }.take(2)
            similarStoriesCache[uuid] = formattedStories

            val newStoriesToAdd = formattedStories.filterNot { newStory ->
                cachedNews.any { cachedStory -> cachedStory.uuid == newStory.uuid }
            }
            cachedNews.addAll(newStoriesToAdd)
            return@withContext formattedStories
        } catch (e: Exception) {
            throw InvalidUUIDException(e.message.toString())
        }
    }
    suspend fun getTopStoriesByCategory(category: String): List<NewsItem> = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()
        val lastFetchTime = lastFetchTimes[category] ?: 0
        val timeSinceLastFetch = currentTime - lastFetchTime

        if (timeSinceLastFetch < delay) {
            return@withContext cachedNews
                .filter { it.category == category }
                .sortedByDescending { it.isFeatured }
        }
        try {
            val newStoriesFromApi = api.getTopStoriesByCategory(
                category = category,
                token = apiToken,
                locale = "us",
                limit = 3
            )
            val formattedNewsFromApi = newStoriesFromApi.data.map { it.toNews() }
            formattedNewsFromApi.forEach { it.category = category }
            val existingCategoryStories =
                cachedNews.filter { it.category == category }.toMutableList()
            val newFeaturedStories = mutableListOf<NewsItem>()
            for (newStory in formattedNewsFromApi) {
                val existingStory =
                    cachedNews.find { it.uuid == newStory.uuid || it.title == newStory.title }

                if (existingStory != null) {
                    existingStory.isFeatured = true
                    existingCategoryStories.removeAll { it.uuid == existingStory.uuid }
                    newFeaturedStories.add(existingStory)
                } else {
                    newStory.isFeatured = true
                    cachedNews.add(newStory)
                    println("added story ${newStory.title}")
                    newFeaturedStories.add(newStory)
                }
            }
            existingCategoryStories.forEach { it.isFeatured = false }
            lastFetchTimes[category] = currentTime
            return@withContext newFeaturedStories
        } catch (e: Exception) {
            println(e.message)
            return@withContext cachedNews.filter { it.category == category }
        }
    }
}