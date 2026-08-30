package com.miozira

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.miozira.MioziraApp
import com.miozira.content.PrototypeContentRepository
import com.miozira.data.db.ContentSeedService
import com.miozira.data.db.DatabaseFactory
import com.miozira.data.repository.RoomRecoveryService
import com.miozira.core.time.AndroidClock
import com.miozira.services.audio.AndroidAudioService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val serviceScope = MainScope()
    private lateinit var audioService: AndroidAudioService
    private lateinit var database: com.miozira.data.db.MioziraDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioService = AndroidAudioService(applicationContext)
        database = DatabaseFactory.create(applicationContext, "miozira.db")
        serviceScope.launch {
            ContentSeedService(
                contentDao = database.contentDao(),
                nowMs = System::currentTimeMillis,
            ).seedContent(PrototypeContentRepository.contentVersion)
            RoomRecoveryService(database.recoveryDao(), AndroidClock).recoverInterruptedSessions()
        }
        setContent {
            MioziraApp(audioService) { }
        }
    }

    override fun onStop() {
        serviceScope.launch { audioService.stopAll() }
        super.onStop()
    }

    override fun onDestroy() {
        serviceScope.cancel()
        database.close()
        super.onDestroy()
    }
}
