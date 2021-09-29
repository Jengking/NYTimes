package my.nytimes.app.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import my.nytimes.app.models.Articles
import my.nytimes.app.services.NewsRepository
import my.nytimes.app.utils.NetworkHelper
import my.nytimes.app.utils.Resource

class MainViewModel(private val repository: NewsRepository,
                    private val networkHelper: NetworkHelper): ViewModel() {
    private val composite = CompositeDisposable()

    private val _data = MutableLiveData<Resource<List<Articles>>>()
    val data: LiveData<Resource<List<Articles>>>
        get() = _data

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _data.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val disposer = Observable.zip(
                    repository.getMostViewed(1),
                    repository.getMostShared(1),
                    repository.getMostEmailed(1),
                    Function3<Articles, Articles, Articles, List<Articles>>{ viewed, shared, emailed ->
                        return@Function3 combineData(viewed, shared, emailed)
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        _data.postValue(Resource.success(result))
                    }, { t: Throwable? ->
                        t?.printStackTrace()
                        _data.postValue(t?.message?.let { Resource.error(it, null) })
                    })
                composite.add(disposer)
            } else {
                _data.postValue(Resource.error("No Internet Connection", null))
            }
        }
    }

    private fun combineData(viewed: Articles, shared: Articles, emailed: Articles): List<Articles> {
        val combinations = ArrayList<Articles>()
        combinations.add(viewed)
        combinations.add(shared)
        combinations.add(emailed)
        return combinations
    }

    override fun onCleared() {
        super.onCleared()
        composite.clear()
    }
}