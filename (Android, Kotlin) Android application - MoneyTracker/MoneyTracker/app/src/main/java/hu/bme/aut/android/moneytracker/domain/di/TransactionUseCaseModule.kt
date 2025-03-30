package hu.bme.aut.android.moneytracker.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.moneytracker.data.repository.TransactionRepository
import hu.bme.aut.android.moneytracker.domain.usecases.DeleteTransactionUseCase
import hu.bme.aut.android.moneytracker.domain.usecases.DeleteTransactionsUseCase
import hu.bme.aut.android.moneytracker.domain.usecases.LoadTransactionUseCase
import hu.bme.aut.android.moneytracker.domain.usecases.LoadTransactionsUseCase
import hu.bme.aut.android.moneytracker.domain.usecases.SaveTransactionUseCase
import hu.bme.aut.android.moneytracker.domain.usecases.TransactionUseCases
import hu.bme.aut.android.moneytracker.domain.usecases.UpdateTransactionUseCase
import javax.inject.Singleton

// Adatbázis manipuláció elérhetővé tétele a UseCase-eken keresztül
@Module
@InstallIn(SingletonComponent::class)
object TransactionUseCaseModule {

    @Provides
    @Singleton
    fun provideLoadTransactionsUseCase(
        repository: TransactionRepository
    ): LoadTransactionsUseCase = LoadTransactionsUseCase(repository)

    @Provides
    @Singleton
    fun provideLoadTransactionUseCase(
        repository: TransactionRepository
    ): LoadTransactionUseCase = LoadTransactionUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveTransactionUseCase(
        repository: TransactionRepository
    ): SaveTransactionUseCase = SaveTransactionUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateTransactionUseCase(
        repository: TransactionRepository
    ): UpdateTransactionUseCase = UpdateTransactionUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteTransactionUseCase(
        repository: TransactionRepository
    ): DeleteTransactionUseCase = DeleteTransactionUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteTransactionsUseCase(
        repository: TransactionRepository
    ): DeleteTransactionsUseCase = DeleteTransactionsUseCase(repository)

    @Provides
    @Singleton
    fun provideTransactionUseCases(
        loadTransactions: LoadTransactionsUseCase,
        loadTransaction: LoadTransactionUseCase,
        saveTransaction: SaveTransactionUseCase,
        updateTransaction: UpdateTransactionUseCase,
        deleteTransaction: DeleteTransactionUseCase,
        deleteTransactions: DeleteTransactionsUseCase
    ) : TransactionUseCases = TransactionUseCases(loadTransactions, loadTransaction, saveTransaction, updateTransaction, deleteTransaction, deleteTransactions)
}