package my.nytimes.app.di

import my.nytimes.app.services.NewsRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        NewsRepository(get())
    }
}