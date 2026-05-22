package etf.ri.rma.newsfeedapp.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import etf.ri.rma.newsfeedapp.model.NewsItem // Importamo NewsItem

@Entity(tableName = "News")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Primarni ključ, autoGenerate

    @ColumnInfo(name = "uuid")
    val uuid: String, // Mapira se na uuid

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "snippet") // Dodana kolona za snippet
    val snippet: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?, // Može biti nullable

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "source") // Dodana kolona za source
    val source: String,

    @ColumnInfo(name = "published_date") // Dodana kolona za publishedDate (preimenovana)
    val publishedDate: String
) {
    // Pomoćna ekstenzija funkcija za konverziju iz NewsEntity u NewsItem
    // Sada popunjava i snippet, source, publishedDate, te imageTags iz TagEntity
    fun toNewsItem(tags: List<TagEntity> = emptyList()): NewsItem {
        return NewsItem(
            uuid = this.uuid,
            title = this.title,
            snippet = this.snippet,
            imageUrl = this.imageUrl,
            category = this.category,
            source = this.source,
            publishedDate = this.publishedDate,
            imageTags = ArrayList(tags.map { it.value }) // Popunjavamo imageTags iz dohvaćenih TagEntity-ja
        )
    }
}