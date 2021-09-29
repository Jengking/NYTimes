package my.nytimes.app.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import my.nytimes.app.models.Articles
import my.nytimes.app.services.NewsRepository
import my.nytimes.app.utils.NetworkHelper
import my.nytimes.app.utils.Resource

class ListViewModel(private val repository: NewsRepository, private val networkHelper: NetworkHelper): ViewModel() {
    private val composite = CompositeDisposable()

    private val _data = MutableLiveData<Resource<Articles>>()
    val data: LiveData<Resource<Articles>>
        get() = _data

    fun fetchMostViewed() {
        viewModelScope.launch {
            _data.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val disposer = repository.getMostViewed(30)
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

    fun fetchMostShared() {
        viewModelScope.launch {
            _data.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val disposer = repository.getMostShared(30)
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

    fun fetchMostEmailed() {
        viewModelScope.launch {
            _data.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val disposer = repository.getMostEmailed(30)
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