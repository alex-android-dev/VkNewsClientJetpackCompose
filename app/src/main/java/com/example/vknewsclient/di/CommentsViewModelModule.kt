package com.example.vknewsclient.di

import androidx.lifecycle.ViewModel
import com.example.vknewsclient.presentation.ViewModelFactory
import com.example.vknewsclient.presentation.comments.CommentsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CommentsViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(CommentsViewModel::class)
    fun bindCommentsViewModel(vm: CommentsViewModel): ViewModel


}