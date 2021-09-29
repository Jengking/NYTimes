package my.nytimes.app.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import my.nytimes.app.models.Searches
import my.nytimes.app.services.NewsRepository
import my.nytimes.app.utils.NetworkHelper
import my.nytimes.app.utils.Resource

class SearchViewModel(private val repository: NewsRepository, private  val networkHelper: NetworkHelper): ViewModel() {
    private val composite = CompositeDisposable()

    private val _data = MutableLiveData<Resource<Searches>>()
    val data: LiveData<Resource<Searches>>
        get() = _data

    fun fetchSearch(keyword: String) {
        viewModelScope.launch {
            _data.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val disposer = repository.getSearchResult(keyword)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        _data.postValue(Resource.success(result))
                    }, { t:Throwable? ->
                        t?.printStackTrace()
                        _data.postValue(t?.message?.let { Resource.error(it, null) })
                    })
                composite.add(disposer)
            } else {
                _data.postValue(Resource.error("No Internet Connection", null))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        composite.clear()
    }
}