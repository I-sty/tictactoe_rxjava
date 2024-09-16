package com.project.tictactoe.domain.usecase

import com.project.tictactoe.data.repository.IHistoryRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import org.koin.core.annotation.Factory

@Factory
class DeleteAllHistoryUseCase(
    private val historyRepository: IHistoryRepository,
    private val ioScheduler: Scheduler
) {
    operator fun invoke(): Observable<Int> =
        historyRepository.deleteAllHistory().subscribeOn(ioScheduler).toObservable()
}