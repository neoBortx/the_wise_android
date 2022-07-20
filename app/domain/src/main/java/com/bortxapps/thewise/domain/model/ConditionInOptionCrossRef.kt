package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    indices = [Index("optId"), Index("condId")],
    primaryKeys = ["optId", "condId"]
)
data class ConditionInOptionCrossRef(
    val optId: Long,
    val condId: Long
)
