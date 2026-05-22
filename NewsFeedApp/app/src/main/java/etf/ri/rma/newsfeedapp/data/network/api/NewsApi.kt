package etf.ri.rma.newsfeedapp.data.network.api

import etf.ri.rma.newsfeedapp.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {
    @GET("news/top")
    suspend fun getTopStoriesByCategory(
        @Query("api_token") token: String,
        @Query("categories") category: String,
        @Query("locale") locale: String,
        @Query("limit") limit: Int,
    ): NewsResponse
    @GET("news/similar/{uuid}")
    suspend fun getSimilarStories(
        @Path("uuid") uuid: String,
        @Query("api_token") token: String,
        @Query("limit") limit: Int,
    ): NewsResponse
    @GET("news/uuid/{uuid}")
    suspend fun getNewsByUUID(
        @Path("uuid") uuid: String,
        @Query("api_token") token: String,
    ): NewsResponse
}