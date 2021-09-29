package my.nytimes.app.core

import android.app.Application
import my.nytimes.app.di.appModule
import my.nytimes.app.di.repoModule
import my.nytimes.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApp)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}