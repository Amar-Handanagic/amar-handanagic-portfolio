package etf.ri.rma.newsfeedapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import etf.ri.rma.newsfeedapp.data.entities.NewsEntity
import etf.ri.rma.newsfeedapp.data.entities.TagEntity

@Entity(
    tableName = "NewsTags",
    primaryKeys = ["newsId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = NewsEntity::class,
            parentColumns = ["id"],
            childColumns = ["newsId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["newsId"]), Index(value = ["tagId"])]
)
data class NewsTagCrossRef(
    val newsId: Long,
    val tagId: Long
)