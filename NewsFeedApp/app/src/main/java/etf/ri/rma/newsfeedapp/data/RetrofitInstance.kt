
import etf.ri.rma.newsfeedapp.data.network.api.ImagaApiService
import etf.ri.rma.newsfeedapp.data.network.api.NewsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thenewsapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val retrofit2 by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.imagga.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
    val api2: ImagaApiService by lazy {
        retrofit2.create(ImagaApiService::class.java)
    }
}