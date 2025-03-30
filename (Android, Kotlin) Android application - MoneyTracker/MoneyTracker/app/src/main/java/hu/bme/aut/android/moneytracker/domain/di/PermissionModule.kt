package hu.bme.aut.android.moneytracker.domain.di

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

// Hilt DI Module --> Activity elérhetővé tétele
@Module
@InstallIn(ActivityComponent::class)
object PermissionModule {

    @Provides
    fun provideActivity(@ActivityContext context: Context): Activity {
        return context as Activity
    }
}