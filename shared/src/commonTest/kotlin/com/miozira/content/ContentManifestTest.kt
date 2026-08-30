package com.miozira.content

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ContentManifestTest {
    @Test
    fun appleEnglishAndTamil_shareOneVisual_andUseDistinctLocalAudio() {
        val english = PrototypeContentRepository.resolve(ConceptId("apple"), LanguageId("en"))
        val tamil = PrototypeContentRepository.resolve(ConceptId("apple"), LanguageId("ta"))

        assertEquals(english.visual, tamil.visual)
        assertNotEquals(english.audio.logicalId, tamil.audio.logicalId)
        assertEquals("files/audio/word_apple_en_draft.m4a", english.audio.assetPath)
        assertEquals("files/audio/word_apple_ta_draft.m4a", tamil.audio.assetPath)
    }
}
