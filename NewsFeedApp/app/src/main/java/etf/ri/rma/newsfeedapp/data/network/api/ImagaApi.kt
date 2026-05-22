package etf.ri.rma.newsfeedapp.data.network.api

import etf.ri.rma.newsfeedapp.data.ImaggaResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ImagaApiService {
    @GET("v2/tags")
    @Headers("Authorization: Basic YWNjX2JhMGVjZTI1NjcyOWRkOTpkOWMwODY0YmZiZGEyZGY3NjVmNWZkOWRiZGVkYzU1NQ==")
    suspend fun getTags(
        @Query("image_url") imageUrl: String,
    ): ImaggaResponse
}