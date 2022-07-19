package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ElectionEntity::class,
            parentColumns = ["electionId"],
            childColumns = ["electionId"],
            onDelete = ForeignKey.CASCADE
        )]
    ,tableName = "option")
data class OptionEntity(
    @PrimaryKey(autoGenerate = true)
    val optionId: Long,
    val electionId: Long,
    val name: String,
    val description: String,
    val url: String,
    val imageUrl: String
)
