package com.miozira.core.model

enum class AttemptState {
    ATTEMPT_DETECTED,
    NO_ATTEMPT_DETECTED,
    MICROPHONE_UNAVAILABLE,
    NOT_MEASURED,
}

enum class InteractionCompletionReason {
    NORMAL,
    EARLY_SESSION_END,
    APP_BACKGROUND,
    PROCESS_INTERRUPTION,
    CONTENT_ERROR,
}
