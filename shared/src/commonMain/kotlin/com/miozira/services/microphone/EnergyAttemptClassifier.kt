package com.miozira.services.microphone

import kotlin.math.sqrt

/**
 * A deliberately small, local heuristic for speech-like activity. It is not a
 * speech recognizer and exposes no audio outside the detector implementation.
 */
class EnergyAttemptClassifier(
    private val config: Config = Config(),
) {
    data class Config(
        val frameDurationMs: Int = 25,
        val calibrationFrameCount: Int = 8,
        val requiredSustainedEnergyMs: Int = 250,
        val relativeEnergyMultiplier: Double = 3.0,
        val minimumSignalRms: Double = 1.0,
    ) {
        init {
            require(frameDurationMs in 20..40)
            require(calibrationFrameCount > 0)
            require(requiredSustainedEnergyMs > 0)
            require(relativeEnergyMultiplier > 1.0)
            require(minimumSignalRms > 0.0)
        }
    }

    private val calibrationRms = mutableListOf<Double>()
    private var noiseFloorRms = 0.0
    private var sustainedEnergyMs = 0
    private var detected = false

    fun observe(frame: ShortArray, sampleCount: Int = frame.size): Boolean {
        if (detected || sampleCount <= 0) return detected
        require(sampleCount <= frame.size)

        val rms = rootMeanSquare(frame, sampleCount)
        if (calibrationRms.size < config.calibrationFrameCount) {
            calibrationRms += rms
            if (calibrationRms.size == config.calibrationFrameCount) {
                noiseFloorRms = calibrationRms.average()
            }
            return false
        }

        val thresholdRms = maxOf(
            noiseFloorRms * config.relativeEnergyMultiplier,
            config.minimumSignalRms,
        )
        if (rms >= thresholdRms) {
            sustainedEnergyMs += config.frameDurationMs
            if (sustainedEnergyMs >= config.requiredSustainedEnergyMs) {
                detected = true
            }
        } else {
            sustainedEnergyMs = 0
        }
        return detected
    }

    private fun rootMeanSquare(frame: ShortArray, sampleCount: Int): Double {
        val meanSquare = frame.take(sampleCount).fold(0.0) { sum, sample ->
            val value = sample.toDouble()
            sum + value * value
        } / sampleCount
        return sqrt(meanSquare)
    }
}
