package com.miozira.services.microphone

data class SpeechAttemptConfig(
    val version: String = "prototype-0.1-v1",
    val windowDurationMs: Long = 2_500L,
    val frameDurationMs: Int = 25,
    val noiseCalibrationMs: Long = 200L,
    val thresholdMargin: Double = 3.0,
    val minimumActiveDurationMs: Int = 250,
    val maximumSilenceGapMs: Int = 150,
) {
    init {
        require(windowDurationMs > 0)
        require(frameDurationMs in 20..40)
        require(noiseCalibrationMs >= frameDurationMs)
        require(thresholdMargin > 1.0)
        require(minimumActiveDurationMs >= frameDurationMs)
        require(maximumSilenceGapMs >= 0)
    }

    val calibrationFrameCount: Int
        get() = (noiseCalibrationMs / frameDurationMs).toInt().coerceAtLeast(1)
}
