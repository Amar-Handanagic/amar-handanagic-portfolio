package etf.ri.rma.newsfeedapp.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import etf.ri.rma.newsfeedapp.data.entities.NewsEntity
import etf.ri.rma.newsfeedapp.data.entities.TagEntity
import etf.ri.rma.newsfeedapp.data.local.entities.NewsTagCrossRef
import etf.ri.rma.newsfeedapp.model.NewsItem

data class NewsWithTags(
    @Embedded
    val news: NewsEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NewsTagCrossRef::class,
            parentColumn = "newsId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
) {
    // Pomoćna funkcija za konverziju u NewsItem (ako se dohvaća NewsWithTags)
    fun toNewsItem(): NewsItem {
        return NewsItem(
            uuid = news.uuid,
            title = news.title,
            snippet = news.snippet, // Dodano
            imageUrl = news.imageUrl,
            category = news.category,
            source = news.source, // Dodano
            publishedDate = news.publishedDate, // Dodano (publishedDate)
            imageTags = ArrayList(tags.map { it.value })
        )
    }
}