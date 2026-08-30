package com.miozira.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miozira.data.db.dao.ContentDao
import com.miozira.data.db.dao.InteractionDao
import com.miozira.data.db.dao.RecoveryDao
import com.miozira.data.db.dao.SessionDao
import com.miozira.data.db.entity.ConceptEntity
import com.miozira.data.db.entity.ConceptLanguagePairEntity
import com.miozira.data.db.entity.ContentVersionEntity
import com.miozira.data.db.entity.FamilyRecordingEntity
import com.miozira.data.db.entity.InteractionEntity
import com.miozira.data.db.entity.LanguageEntity
import com.miozira.data.db.entity.PairLearningStateEntity
import com.miozira.data.db.entity.RealWorldObservationEntity
import com.miozira.data.db.entity.SessionEntity

@Database(
    entities = [
        ContentVersionEntity::class,
        ConceptEntity::class,
        LanguageEntity::class,
        ConceptLanguagePairEntity::class,
        SessionEntity::class,
        InteractionEntity::class,
        PairLearningStateEntity::class,
        RealWorldObservationEntity::class,
        FamilyRecordingEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class MioziraDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao

    abstract fun sessionDao(): SessionDao

    abstract fun interactionDao(): InteractionDao

    abstract fun recoveryDao(): RecoveryDao
}
