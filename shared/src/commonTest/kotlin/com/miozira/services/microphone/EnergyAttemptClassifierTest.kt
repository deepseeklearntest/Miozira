package com.miozira.services.microphone

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EnergyAttemptClassifierTest {
    private val config = EnergyAttemptClassifier.Config(
        frameDurationMs = 25,
        calibrationFrameCount = 8,
        requiredSustainedEnergyMs = 250,
        relativeEnergyMultiplier = 3.0,
    )

    @Test
    fun ambientFrames_doNotReturnAnAttempt() {
        val classifier = EnergyAttemptClassifier(config)

        repeat(108) {
            assertFalse(classifier.observe(frameWithAmplitude(500)))
        }
    }

    @Test
    fun silentZeroFrames_doNotReturnAnAttempt() {
        val classifier = EnergyAttemptClassifier(config)

        repeat(108) {
            assertFalse(classifier.observe(frameWithAmplitude(0)))
        }
    }

    @Test
    fun sustainedEnergyAboveMeasuredNoiseFloor_returnsAttemptAfter250Ms() {
        val classifier = EnergyAttemptClassifier(config)

        repeat(config.calibrationFrameCount) {
            assertFalse(classifier.observe(frameWithAmplitude(500)))
        }
        repeat(9) {
            assertFalse(classifier.observe(frameWithAmplitude(2_000)))
        }

        assertTrue(classifier.observe(frameWithAmplitude(2_000)))
    }

    @Test
    fun singleImpulse_doesNotReturnAnAttempt() {
        val classifier = EnergyAttemptClassifier(config)

        repeat(config.calibrationFrameCount) {
            assertFalse(classifier.observe(frameWithAmplitude(500)))
        }
        assertFalse(classifier.observe(frameWithAmplitude(6_000)))
        repeat(20) {
            assertFalse(classifier.observe(frameWithAmplitude(500)))
        }
    }

    private fun frameWithAmplitude(amplitude: Short): ShortArray = ShortArray(400) { amplitude }
}
