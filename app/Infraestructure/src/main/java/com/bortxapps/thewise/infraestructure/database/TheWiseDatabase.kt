package com.bortxapps.thewise.infraestructure.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bortxapps.thewise.domain.model.ConditionEntity
import com.bortxapps.thewise.domain.model.ConditionInOptionCrossRef
import com.bortxapps.thewise.domain.model.ElectionEntity
import com.bortxapps.thewise.domain.model.OptionEntity
import com.bortxapps.thewise.infraestructure.dao.ConditionDao
import com.bortxapps.thewise.infraestructure.dao.ElectionDao
import com.bortxapps.thewise.infraestructure.dao.OptionDao

@Database(
    entities = [ElectionEntity::class, OptionEntity::class, ConditionEntity::class, ConditionInOptionCrossRef::class],
    version = 7
)
abstract class TheWiseDatabase : RoomDatabase() {
    abstract fun electionDao(): ElectionDao
    abstract fun conditionDao(): ConditionDao
    abstract fun optionDao(): OptionDao
}