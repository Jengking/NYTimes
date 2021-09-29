package my.nytimes.app.services

import io.reactivex.Observable
import my.nytimes.app.models.Articles
import my.nytimes.app.models.Searches

class NewsHelperImpl(private val service: NewsService): NewsHelper {
    override fun getMostViewed(period: Int, apikey: String): Observable<Articles> = service.getMostViewed(period, apikey)

    override fun getMostShared(period: Int, apikey: String): Observable<Articles> = service.getMostShared(period, apikey)

    override fun getMostEmailed(period: Int, apikey: String): Observable<Articles> = service.getMostEmailed(period, apikey)

    override fun getSearchResult(keyword: String, apikey: String): Observable<Searches> = service.getSearchResult(keyword, apikey)
}