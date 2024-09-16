package com.project.tictactoe.di

import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class SchedulersModule {
    @Single
    fun provideSchedulerIO() = Schedulers.io()
}