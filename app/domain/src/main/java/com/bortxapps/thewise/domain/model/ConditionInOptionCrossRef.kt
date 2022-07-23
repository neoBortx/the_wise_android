package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    indices = [Index("optId"), Index("condId")],
    primaryKeys = ["optId", "condId"], foreignKeys = [ForeignKey(
        entity = OptionEntity::class,
        parentColumns = ["optId"],
        childColumns = ["optId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = ConditionEntity::class,
        parentColumns = ["condId"],
        childColumns = ["condId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ConditionInOptionCrossRef(
    val optId: Long,
    val condId: Long
)
