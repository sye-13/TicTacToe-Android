package com.example.tictactoe.di

import com.example.tictactoe.domain.usecase.CheckGameStatusUseCase
import com.example.tictactoe.domain.usecase.MoveValidationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TicTacToeModule {

    @Provides
    fun provideMoveValidationUseCase(): MoveValidationUseCase {
        return MoveValidationUseCase()
    }

    @Provides
    fun provideCheckGameStatusUseCase(): CheckGameStatusUseCase {
        return CheckGameStatusUseCase()
    }
}
