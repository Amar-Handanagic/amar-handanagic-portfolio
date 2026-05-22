package etf.ri.rma.newsfeedapp.data

import com.google.gson.annotations.SerializedName
import etf.ri.rma.newsfeedapp.model.NewsItem

data class NewsResponse(
    @SerializedName("data") val data: List<NewsFromApi>
)
data class NewsFromApi(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("keywords") val keywords: String?,
    @SerializedName("snippet") val snippet: String,
    @SerializedName("url") val url: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("language") val language: String,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("source") val source: String,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("relevance_score") val relevanceScore: Double?,
    @SerializedName("locale") val locale: String?
)
data class ImaggaResponse(
    @SerializedName("result") val result: ResultObject,
    @SerializedName("status") val status: StatusObject
)
data class StatusObject(
    @SerializedName("type") val type: String,
    @SerializedName("text") val text: String
)
data class ResultObject(
    @SerializedName("tags") val tags: List<TagObject>
)
data class TagObject(
    @SerializedName("confidence") val confidence: Float,
    @SerializedName("tag") val tag: TagName
)
data class TagName(
    @SerializedName("en") val en: String
)
fun NewsFromApi.toNews(): NewsItem {
    return NewsItem(
        uuid = this.uuid,
        title = this.title,
        snippet = this.snippet,
        imageUrl = this.imageUrl,
        category = this.categories[0],
        isFeatured = false,
        source = this.source,
        publishedDate = this.publishedAt,
        imageTags = ArrayList<String>()
    )
}