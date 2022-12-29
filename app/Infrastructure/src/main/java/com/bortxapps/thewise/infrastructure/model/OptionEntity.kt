package com.bortxapps.thewise.infrastructure.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bortxapps.thewise.domain.model.IOptionEntity

@Entity(
    indices = [Index("optId"), Index("electionId")], foreignKeys = [ForeignKey(
        entity = ElectionEntity::class,
        parentColumns = ["electId"],
        childColumns = ["electionId"],
        onDelete = ForeignKey.CASCADE
    )], tableName = "option"
)
data class OptionEntity(
    @PrimaryKey(autoGenerate = true) override val optId: Long,
    override val electionId: Long,
    override val name: String,
    override val imageUrl: String
) : IOptionEntity
