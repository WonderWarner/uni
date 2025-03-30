package hu.bme.aut.android.moneytracker.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.moneytracker.data.repository.TransactionRepository
import hu.bme.aut.android.moneytracker.data.repository.TransactionRepositoryRoom
import javax.inject.Singleton

// Repository-t elérhetővé tevő DI modul
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryRoom: TransactionRepositoryRoom
    ): TransactionRepository

}