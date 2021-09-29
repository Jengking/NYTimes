package my.nytimes.app.services

import my.nytimes.app.BuildConfig

class NewsRepository(private val newsHelper: NewsHelper) {
    private val apikey = BuildConfig.API_KEY

    fun getMostViewed(period: Int) = newsHelper.getMostViewed(period, apikey)
    fun getMostShared(period: Int) = newsHelper.getMostShared(period, apikey)
    fun getMostEmailed(period: Int) = newsHelper.getMostEmailed(period, apikey)
    fun getSearchResult(keyword: String) = newsHelper.getSearchResult(keyword, apikey)
}