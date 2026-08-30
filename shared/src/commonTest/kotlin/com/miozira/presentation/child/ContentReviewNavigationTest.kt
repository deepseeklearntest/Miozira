package com.miozira.presentation.child

import com.miozira.content.PrototypeContentRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ContentReviewNavigationTest {
    @Test
    fun reviewNavigation_wrapsAcrossTheFixedPairList() {
        val pairs = PrototypeContentRepository.allPairs()

        assertEquals("water:ta", pairs[previousContentIndex(0, pairs.size)].pairId.value)
        assertEquals("apple:en", pairs[nextContentIndex(pairs.lastIndex, pairs.size)].pairId.value)
    }

    @Test
    fun reviewNavigation_isUnavailableWhileTheCurrentWordIsPlaying() {
        assertEquals(false, isReviewNavigationEnabled(isPlaying = true))
        assertEquals(true, isReviewNavigationEnabled(isPlaying = false))
    }
}
