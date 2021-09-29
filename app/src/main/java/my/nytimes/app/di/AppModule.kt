package my.nytimes.app.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import my.nytimes.app.BuildConfig
import my.nytimes.app.services.NewsHelper
import my.nytimes.app.services.NewsHelperImpl
import my.nytimes.app.services.NewsService
import my.nytimes.app.utils.NetworkHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single { provideOkHttpClient() }
    single { provideMoshi() }
    single { provideRetrofit(get(), get()) }
    single { provideApiService(get()) }
    single { provideNetworkHelper(androidContext()) }

    single<NewsHelper>{
        return@single NewsHelperImpl(get())
    }
}

private fun provideMoshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)

private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInspector = HttpLoggingInterceptor()
    loggingInspector.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(loggingInspector)
        .build()
} else OkHttpClient.Builder().build()

private fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .baseUrl(BuildConfig.BASE_URL)
    .client(okHttpClient)
    .build()

private fun provideApiService(retrofit: Retrofit): NewsService = retrofit.create(NewsService::class.java)