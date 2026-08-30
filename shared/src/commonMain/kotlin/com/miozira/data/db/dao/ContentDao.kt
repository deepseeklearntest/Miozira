package com.miozira.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.miozira.data.db.ContentSeed
import com.miozira.data.db.entity.ConceptEntity
import com.miozira.data.db.entity.ConceptLanguagePairEntity
import com.miozira.data.db.entity.ContentVersionEntity
import com.miozira.data.db.entity.LanguageEntity
import com.miozira.data.db.entity.PairLearningStateEntity

@Dao
interface ContentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContentVersionsIgnore(values: List<ContentVersionEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConceptsIgnore(values: List<ConceptEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLanguagesIgnore(values: List<LanguageEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPairsIgnore(values: List<ConceptLanguagePairEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPairStatesIgnore(values: List<PairLearningStateEntity>)

    @Transaction
    suspend fun seedContent(seed: ContentSeed) {
        insertContentVersionsIgnore(listOf(seed.contentVersion))
        insertLanguagesIgnore(seed.languages)
        insertConceptsIgnore(seed.concepts)
        insertPairsIgnore(seed.pairs)
        insertPairStatesIgnore(seed.pairStates)
    }

    @Query("SELECT COUNT(*) FROM concept")
    suspend fun conceptCount(): Int

    @Query("SELECT COUNT(*) FROM language")
    suspend fun languageCount(): Int

    @Query("SELECT COUNT(*) FROM concept_language_pair")
    suspend fun pairCount(): Int

    @Query("SELECT COUNT(*) FROM pair_learning_state WHERE learning_state = 'NEW'")
    suspend fun newPairStateCount(): Int
}
