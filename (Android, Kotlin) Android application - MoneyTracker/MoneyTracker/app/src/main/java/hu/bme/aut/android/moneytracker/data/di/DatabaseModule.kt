package hu.bme.aut.android.moneytracker.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.moneytracker.data.TransactionDatabase
import hu.bme.aut.android.moneytracker.data.dao.TransactionDao
import javax.inject.Singleton

// Hilt DI Module -> az adatbázis által biztosított elemek automatikus injektálásához
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabaseInstance(
        @ApplicationContext context: Context
    ) : TransactionDatabase = Room.databaseBuilder(
        context,
        TransactionDatabase::class.java,
        "transaction_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideTransactionDao(
        db: TransactionDatabase
    ): TransactionDao = db.dao

}