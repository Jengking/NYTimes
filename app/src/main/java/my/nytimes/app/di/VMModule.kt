package my.nytimes.app.di

import my.nytimes.app.vm.ListViewModel
import my.nytimes.app.vm.MainViewModel
import my.nytimes.app.vm.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get())
    }
    viewModel {
        ListViewModel(get(), get())
    }
    viewModel{
        SearchViewModel(get(), get())
    }
}