package my.nytimes.app.services

import io.reactivex.Observable
import my.nytimes.app.models.Articles
import my.nytimes.app.models.Searches
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsService {

    @GET("mostpopular/v2/viewed/{period}.json")
    fun getMostViewed(@Path("period") period: Int, @Query("api-key") apiKey:String): Observable<Articles>

    @GET("mostpopular/v2/shared/{period}/facebook.json")
    fun getMostShared(@Path("period") period: Int, @Query("api-key") apiKey: String): Observable<Articles>

    @GET("mostpopular/v2/emailed/{period}.json")
    fun getMostEmailed(@Path("period") period: Int, @Query("api-key") apiKey: String): Observable<Articles>

    @GET("search/v2/articlesearch.json")
    fun getSearchResult(@Query("q") keyword: String, @Query("api-key") apiKey: String): Observable<Searches>
}