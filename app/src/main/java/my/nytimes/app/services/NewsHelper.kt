package my.nytimes.app.services

import io.reactivex.Observable
import my.nytimes.app.models.Articles
import my.nytimes.app.models.Searches

interface NewsHelper {

    fun getMostViewed(period: Int, apikey: String): Observable<Articles>
    fun getMostShared(period: Int, apikey: String): Observable<Articles>
    fun getMostEmailed(period: Int, apikey: String): Observable<Articles>
    fun getSearchResult(keyword: String, apikey: String): Observable<Searches>
}