package com.project.tictactoe.domain.usecase

import com.project.tictactoe.data.model.HistoryEntity
import com.project.tictactoe.data.repository.IHistoryRepository
import com.project.tictactoe.domain.model.Player
import com.project.tictactoe.domain.model.toDAOString
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import org.koin.core.annotation.Factory
import java.util.Date

@Factory
class AddHistoryUseCase(
    private val historyRepository: IHistoryRepository,
    private val ioScheduler: Scheduler
) {
    operator fun invoke(winner: Player, opponent: Player): Observable<Completable> {
        return Observable.fromCallable {
            historyRepository
                .addHistory(
                    HistoryEntity(
                        timestamp = Date().toDAOString(),
                        playerName = winner.username,
                        playerScore = winner.score,
                        playerSymbol = winner.symbol,
                        opponentSymbol = opponent.symbol,
                        opponentName = opponent.username,
                        opponentScore = opponent.score
                    )
                )
        }.subscribeOn(ioScheduler)
    }
}