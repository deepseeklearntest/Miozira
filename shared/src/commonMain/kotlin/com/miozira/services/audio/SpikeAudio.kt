package com.miozira.services.audio

/** Folder under the APK's assets where Compose resources (the bundled clips) are packaged. */
const val BUNDLED_RESOURCES_ROOT = "composeResources/miozira.shared.generated.resources/"

/** Temporary technical-spike asset; this is not a final canonical pronunciation. */
object SpikeAudio {
    const val assetPath = BUNDLED_RESOURCES_ROOT + "files/audio/word_apple_en_spike.m4a"
}
