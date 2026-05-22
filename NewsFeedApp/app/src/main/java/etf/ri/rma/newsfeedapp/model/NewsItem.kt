package etf.ri.rma.newsfeedapp.model

data class NewsItem(
    val uuid: String = "",
    val title: String = "",
    val snippet: String = "",
    val imageUrl: String? = "",
    var category: String = "",
    var isFeatured: Boolean = false,
    val source: String = "",
    val publishedDate: String = "",
    var imageTags: ArrayList<String> = ArrayList<String>()
)
