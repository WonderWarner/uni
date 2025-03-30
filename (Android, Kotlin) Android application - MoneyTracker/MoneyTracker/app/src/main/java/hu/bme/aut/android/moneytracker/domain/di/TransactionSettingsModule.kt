package hu.bme.aut.android.moneytracker.domain.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.moneytracker.R
import javax.inject.Singleton

// SharedPreferences injektálása. Jelen esetben csak egy van, de ha több lenne nem kellene beleégetni a felhasználó osztályba/függvénybe
@Module
@InstallIn(SingletonComponent::class)
object TransactionSettingsModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(context.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE)
    }
}