package etf.ri.rma.newsfeedapp.data.network

import etf.ri.rma.newsfeedapp.data.network.api.ImagaApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidImageURLException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.MalformedURLException
import java.net.URL

class ImagaDAO {
    private var api = RetrofitInstance.api2
    suspend fun getTags(imageUrl: String): List<String> = withContext(Dispatchers.IO) {
        try {
            URL(imageUrl)
        } catch (e: MalformedURLException) {
            throw InvalidImageURLException("Invalid image URL format: ${e.message}", e)
        }
        try {
            val results = api.getTags(imageUrl = imageUrl)
            if (results.status == null || results.status.type != "success") {
                val errorMessage = if (results.status == null) {
                    "API response status object was null."
                } else {
                    "API returned non-success status: ${results.status.type}. " +
                            "Error message: ${results.status.text ?: "No specific error message provided."}"
                }
                throw InvalidImageURLException(errorMessage)
            }
            val tags = mutableListOf<String>()
            results.result.tags.forEach { tags.add(it.tag.en) }
            tags.take(5)
        } catch(e: Exception) {
            throw InvalidImageURLException(e.message.toString())
        }
    }
    fun setApiService(newApi: ImagaApiService) {
        api = newApi
    }
}
