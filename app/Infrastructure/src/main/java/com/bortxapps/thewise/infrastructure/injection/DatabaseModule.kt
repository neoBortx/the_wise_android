package com.bortxapps.thewise.infrastructure.injection

import android.content.Context
import androidx.room.Room
import com.bortxapps.thewise.infrastructure.constants.InfrastructureConstants.DATABASE_NAME
import com.bortxapps.thewise.infrastructure.dao.ConditionDao
import com.bortxapps.thewise.infrastructure.dao.ElectionDao
import com.bortxapps.thewise.infrastructure.dao.OptionDao
import com.bortxapps.thewise.infrastructure.database.TheWiseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule
{
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context)
    = Room.databaseBuilder(
        context,
        TheWiseDatabase::class.java,
        DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideElectionDao(appDatabase: TheWiseDatabase): ElectionDao {
        return appDatabase.electionDao()
    }

    @Provides
    fun provideConditionDao(appDatabase: TheWiseDatabase): ConditionDao {
        return appDatabase.conditionDao()
    }

    @Provides
    fun provideOptionDao(appDatabase: TheWiseDatabase): OptionDao {
        return appDatabase.optionDao()
    }
}