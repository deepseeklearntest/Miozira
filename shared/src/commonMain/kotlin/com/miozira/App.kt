package com.miozira

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.miozira.content.PrototypeContentRepository
import com.miozira.presentation.child.ContentReviewScreen
import com.miozira.presentation.child.ChildSessionScreen
import com.miozira.presentation.parent.ParentAccess
import com.miozira.presentation.parent.ParentAccessController
import com.miozira.presentation.parent.ParentChallenge
import com.miozira.presentation.parent.ParentReadingStepScreen
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import com.miozira.presentation.parent.ParentController
import com.miozira.presentation.parent.ParentSummaryScreen
import com.miozira.presentation.parent.FamilyRecordingController
import com.miozira.presentation.parent.FamilyRecordingState
import com.miozira.presentation.parent.FamilyVoiceScreen
import com.miozira.presentation.parent.ParentActionsController
import com.miozira.presentation.parent.ParentActionsScreen
import com.miozira.presentation.parent.LanguagesScreen
import com.miozira.core.model.PairId
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.miozira.domain.session.ChildSessionController
import com.miozira.services.audio.AudioService
import com.miozira.services.audio.PlaybackHandle

enum class ParentPage { HOME, LANGUAGES, MICROPHONE }

fun appDisplayName(): String = "Miozira"

@Composable
fun StorageRecoveryScreen() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF2EEE7),
            contentColor = Color(0xFF26312E),
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center,
            ) {
                androidx.compose.material3.Text("Miozira needs attention before learning can continue.")
            }
        }
    }
}

@Composable
fun MioziraApp(
    audioService: AudioService,
    onExposureThreshold: suspend (PlaybackHandle) -> Unit,
) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF2EEE7),
            contentColor = Color(0xFF26312E),
        ) {
            ContentReviewScreen(
                pairs = PrototypeContentRepository.allPairs(),
                audioService = audioService,
                onExposureThreshold = onExposureThreshold,
            )
        }
    }
}

@Composable
fun MioziraApp(
    controller: ChildSessionController,
    parentAccess: ParentAccessController,
    parentController: ParentController? = null,
    familyRecordingController: FamilyRecordingController? = null,
    parentActionsController: ParentActionsController? = null,
    onOpenMicrophoneSettings: () -> Unit = {},
) {
    val access by parentAccess.state.collectAsState()
    var familyPair by remember { mutableStateOf<PairId?>(null) }
    val scope = rememberCoroutineScope()
    var page by remember { mutableStateOf(ParentPage.HOME) }
    var permissionPending by remember { mutableStateOf(false) }
    if (access == ParentAccess.Locked) page = ParentPage.HOME
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF2EEE7),
            contentColor = Color(0xFF26312E),
        ) {
            val challenge = (access as? ParentAccess.Challenge)?.challenge
            if (challenge != null) {
                LaunchedEffect(challenge.typed) {
                    delay(ParentChallenge.TIMEOUT_MS)
                    parentAccess.timedOut()
                }
                ParentReadingStepScreen(challenge, onDigit = parentAccess::digit)
            } else if (parentController != null && access == ParentAccess.Open) {
                val state by parentController.state.collectAsState()
                val familyState = familyRecordingController?.state?.collectAsState()?.value ?: FamilyRecordingState.Idle
                val actionsState = parentActionsController?.state?.collectAsState()?.value
                Column(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFF6F3EE)).padding(top = 48.dp),
                ) {
                    TextButton(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick = parentAccess::lock,
                    ) { androidx.compose.material3.Text("Lock and go back") }
                    if (familyPair != null && familyRecordingController != null) {
                        FamilyVoiceScreen(
                            pairId = familyPair!!,
                            state = familyState,
                            onRequestPermission = familyRecordingController::requestPermission,
                            onRecord = { scope.launch { familyRecordingController.start(familyPair!!, familyState is FamilyRecordingState.Available) } },
                            onStop = { scope.launch { familyRecordingController.stop() } },
                            onPreview = { scope.launch { familyRecordingController.preview() } },
                            onSave = { scope.launch { familyRecordingController.save() } },
                            onCancel = { scope.launch { familyRecordingController.cancel(); familyPair = null } },
                            onDelete = { scope.launch { familyRecordingController.delete(); familyPair = null } },
                        )
                    } else if (page == ParentPage.LANGUAGES) {
                        LanguagesScreen(state, onBack = { page = ParentPage.HOME }, modifier = Modifier.weight(1f))
                    } else if (page == ParentPage.MICROPHONE && actionsState != null && parentActionsController != null) {
                        LaunchedEffect(Unit) { parentActionsController.load() }
                        ParentActionsScreen(
                            state = actionsState,
                            onSetListening = { on ->
                                scope.launch {
                                    if (on) permissionPending = true
                                    try { parentActionsController.setListening(on) } finally { permissionPending = false }
                                }
                            },
                            switchEnabled = !permissionPending,
                            onOpenMicrophoneSettings = onOpenMicrophoneSettings,
                            onRequestReset = parentActionsController::requestReset,
                            onCancelReset = parentActionsController::cancelReset,
                            onConfirmReset = {
                                scope.launch {
                                    familyRecordingController?.onBackground()
                                    parentActionsController.confirmReset()
                                    parentController.refresh()
                                    familyPair = null
                                }
                            },
                            onBack = { page = ParentPage.HOME },
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        ParentSummaryScreen(
                            modifier = Modifier.weight(1f),
                            state = state,
                            onRecognized = { scope.launch { parentController.recordRecognition(it) } },
                            onUsed = { scope.launch { parentController.recordUse(it) } },
                            onOpenLanguages = { page = ParentPage.LANGUAGES },
                            onOpenMicrophone = { page = ParentPage.MICROPHONE },
                            onFamilyVoice = familyRecordingController?.let { { pairId ->
                                familyPair = pairId
                                scope.launch { it.load(pairId) }
                            } },
                        )
                    }
                }
            } else {
                ChildSessionScreen(controller) {
                    parentAccess.cornerHeld()
                    parentController?.let { controller -> scope.launch { controller.refresh() } }
                }
            }
        }
    }
}
