package hu.bme.aut.android.moneytracker.domain.di

import android.content.Context
import androidx.annotation.StringRes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

// String erőforrásokat kiolvasó osztály elérhetővé tétele context-tel nem rendelkező osztályoknak is
@Module
@InstallIn(SingletonComponent::class)
object StringProviderModule {
    @Provides
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider {
        return StringProviderImpl(context)
    }
}

interface StringProvider {
    fun getString(@StringRes resId: Int): String
}

class StringProviderImpl @Inject constructor(
    private val context: Context
) : StringProvider {

    override fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}