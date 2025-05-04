package com.example.vknewsclient.di

import androidx.lifecycle.ViewModel
import com.example.vknewsclient.presentation.comments.CommentsViewModel
import com.example.vknewsclient.presentation.main.MainViewModel
import com.example.vknewsclient.presentation.news.NewsFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(vm: MainViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(NewsFeedViewModel::class)
    fun bindNewsFeedViewModel(vm: NewsFeedViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(CommentsViewModel::class)
    fun bindCommentsViewModel(vm: CommentsViewModel): ViewModel

}