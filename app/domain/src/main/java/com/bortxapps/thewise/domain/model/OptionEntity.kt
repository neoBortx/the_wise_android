package com.bortxapps.thewise.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("optId"), Index("electionId")], foreignKeys = [ForeignKey(
        entity = ElectionEntity::class,
        parentColumns = ["electId"],
        childColumns = ["electionId"],
        onDelete = ForeignKey.CASCADE
    )], tableName = "option"
)
data class OptionEntity(
    @PrimaryKey(autoGenerate = true) val optId: Long,
    val electionId: Long,
    val name: String,
    val description: String,
    val url: String,
    val imageUrl: String
)
