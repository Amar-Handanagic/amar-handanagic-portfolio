package etf.ri.rma.newsfeedapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import etf.ri.rma.newsfeedapp.data.dao.SavedNewsDao
import etf.ri.rma.newsfeedapp.data.entities.NewsEntity
import etf.ri.rma.newsfeedapp.data.entities.TagEntity
import etf.ri.rma.newsfeedapp.data.local.entities.NewsTagCrossRef

@Database(
    entities = [NewsEntity::class, TagEntity::class, NewsTagCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun savedNewsDao(): SavedNewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}