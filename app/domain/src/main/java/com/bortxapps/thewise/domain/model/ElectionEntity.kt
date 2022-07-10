package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "election")
data class ElectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String)
