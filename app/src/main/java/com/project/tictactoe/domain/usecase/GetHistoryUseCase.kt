package com.project.tictactoe.domain.usecase

import com.project.tictactoe.data.repository.IHistoryRepository
import com.project.tictactoe.domain.model.History
import com.project.tictactoe.domain.model.toDAODate
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import org.koin.core.annotation.Factory

@Factory
class GetHistoryUseCase(
    private val historyRepository: IHistoryRepository,
    private val ioScheduler: Scheduler
) {
    operator fun invoke(): Observable<List<History>> =
        historyRepository.getHistory().subscribeOn(ioScheduler)
            .map { data ->
                data.map {
                    History(
                        uid = it.uid,
                        timestamp = it.timestamp.toDAODate(),
                        playerName = it.playerName,
                        playerScore = it.playerScore,
                        playerSymbol = it.playerSymbol,
                        opponentSymbol = it.opponentSymbol,
                        opponentName = it.opponentName,
                        opponentScore = it.opponentScore
                    )

                }.reversed()
            }.toObservable()
}