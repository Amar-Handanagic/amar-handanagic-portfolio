package etf.ri.rma.newsfeedapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import etf.ri.rma.newsfeedapp.data.entities.NewsEntity
import etf.ri.rma.newsfeedapp.data.entities.TagEntity
import etf.ri.rma.newsfeedapp.data.local.entities.NewsTagCrossRef
import etf.ri.rma.newsfeedapp.data.relations.NewsWithTags
import etf.ri.rma.newsfeedapp.model.NewsItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Dao
interface SavedNewsDao {

    @Query("SELECT * FROM News WHERE uuid = :uuid LIMIT 1")
    suspend fun getNewsEntityByUuid(uuid: String): NewsEntity?

    @Query("SELECT * FROM Tags WHERE value = :tagValue LIMIT 1")
    suspend fun getTagByValue(tagValue: String): TagEntity?

    @Query("SELECT id FROM News WHERE uuid = :uuid LIMIT 1")
    suspend fun getNewsIdByUuid(uuid: String): Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewsEntity(newsEntity: NewsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagEntity(tagEntity: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsTagCrossRef(crossRef: NewsTagCrossRef)

    // 1. saveNews(news:NewsItem):Boolean
    @Transaction
    suspend fun saveNews(news: NewsItem): Boolean {
        val existingNews = getNewsEntityByUuid(news.uuid)
        return if (existingNews == null) {
            val newsEntity = NewsEntity(
                uuid = news.uuid,
                title = news.title,
                snippet = news.snippet, // Dodano
                imageUrl = news.imageUrl,
                category = news.category,
                source = news.source, // Dodano
                publishedDate = news.publishedDate // Dodano
            )
            insertNewsEntity(newsEntity) // Vraca ID, ali ga ovdje ne koristimo dalje
            true
        } else {
            false
        }
    }

    // 2. allNews():List<NewsItem>
    @Transaction
    @Query("SELECT * FROM News")
    suspend fun getAllNewsWithTagsInternal(): List<NewsWithTags> // Interna metoda za dohvaćanje sa tagovima

    suspend fun allNews(): List<NewsItem> {
        return getAllNewsWithTagsInternal().map { it.toNewsItem() }
    }

    // 3. getNewsWithCategory(category:String):Lisŧ<NewsItem>
    @Transaction
    @Query("SELECT * FROM News WHERE category = :category")
    suspend fun getNewsWithCategoryWithTagsInternal(category: String): List<NewsWithTags>

    suspend fun getNewsWithCategory(category: String): List<NewsItem> {
        return getNewsWithCategoryWithTagsInternal(category).map { it.toNewsItem() }
    }

    // 4. addTags(tags:List<String>,newsId:Int):Int
    // newsId je tipa Long, ne Int, jer je id u NewsEntity Long
    @Transaction
    suspend fun addTags(tags: List<String>, newsId: Long): Int {
        var newTagsAddedCount = 0
        for (tagValue in tags) {
            var tagEntity = getTagByValue(tagValue)
            val tagId: Long

            if (tagEntity == null) {
                // Tag ne postoji, dodaj ga u Tags tabelu
                tagId = insertTagEntity(TagEntity(value = tagValue))
                newTagsAddedCount++
            } else {
                // Tag već postoji, koristi njegov ID
                tagId = tagEntity.id
            }
            // Poveži vijest i tag u međutabeli
            // OnConflictStrategy.REPLACE će osigurati da se ne dodaju duplikati NewsTags unosi
            insertNewsTagCrossRef(NewsTagCrossRef(newsId = newsId, tagId = tagId))
        }
        return newTagsAddedCount
    }

    // 5. getTags(newsId:Int):List<String>
    // newsId je tipa Long
    @Transaction
    @Query("""
        SELECT T.value FROM Tags AS T
        INNER JOIN NewsTags AS NT ON T.id = NT.tagId
        WHERE NT.newsId = :newsId
    """)
    suspend fun getTags(newsId: Long): List<String>

    // 6. getSimilarNews(tags:List<String>):List<NewsItem>
    @Transaction
    suspend fun getSimilarNews(tags: List<String>): List<NewsItem> {
        if (tags.isEmpty()) return emptyList()

        val tagIds = mutableListOf<Long>()
        for (tagValue in tags) {
            val tagEntity = getTagByValue(tagValue)
            tagEntity?.id?.let { tagIds.add(it) }
        }

        if (tagIds.isEmpty()) return emptyList()

        val newsWithTagsList = getNewsWithAnyTagInternal(tagIds)

        // Sortiranje po datumu objave (publishedDate) od najnovije do najstarije
        // Pretpostavljamo da je publishedDate u formatu koji se može parsirati (npr. "YYYY-MM-DD" ili ISO 8601)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Prilagodite formatu vašeg datuma

        return newsWithTagsList
            .map { it.toNewsItem() }
            .sortedByDescending { newsItem ->
                try {
                    dateFormatter.parse(newsItem.publishedDate) ?: Date(0) // Parsiraj datum
                } catch (e: Exception) {
                    Date(0) // Vrati vrlo stari datum u slučaju greške parsiranja
                }
            }
    }

    // Interna metoda za getSimilarNews - dohvaća vijesti koje imaju bilo koji od tagova iz liste
    @Transaction // Potrebno jer dohvaća i relaciju (Tags)
    @Query("""
        SELECT DISTINCT N.* FROM News AS N
        INNER JOIN NewsTags AS NT ON N.id = NT.newsId
        WHERE NT.tagId IN (:tagIds)
    """)
    suspend fun getNewsWithAnyTagInternal(tagIds: List<Long>): List<NewsWithTags>
}