package com.bortxapps.thewise.infraestructure.injection

import android.content.Context
import androidx.room.Room
import com.bortxapps.thewise.infraestructure.constants.InfrastructureConstants.DATABASE_NAME
import com.bortxapps.thewise.infraestructure.dao.ConditionDao
import com.bortxapps.thewise.infraestructure.dao.ElectionDao
import com.bortxapps.thewise.infraestructure.dao.OptionDao
import com.bortxapps.thewise.infraestructure.database.TheWiseDatabase
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