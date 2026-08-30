package com.miozira.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "real_world_observation",
    foreignKeys = [ForeignKey(ConceptLanguagePairEntity::class, ["pair_id"], ["pair_id"])],
    indices = [Index(value = ["pair_id"])],
)
data class RealWorldObservationEntity(
    @PrimaryKey
    @ColumnInfo(name = "observation_id")
    val observationId: String,
    @ColumnInfo(name = "pair_id")
    val pairId: String,
    @ColumnInfo(name = "observation_type")
    val observationType: String,
    @ColumnInfo(name = "recorded_at_ms")
    val recordedAtMs: Long,
    @ColumnInfo(name = "observed_at_ms")
    val observedAtMs: Long?,
    val context: String?,
    @ColumnInfo(name = "source", defaultValue = "'PARENT_REPORT'")
    val source: String = "PARENT_REPORT",
)
