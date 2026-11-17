package com.miozira.presentation.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miozira.content.PrototypeContentRepository
import com.miozira.core.model.PairId

@Composable
fun ParentSummaryScreen(
    state: ParentUiState,
    onRecognized: (PairId) -> Unit,
    onUsed: (PairId) -> Unit,
    onFamilyVoice: ((PairId) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Most recent session", style = MaterialTheme.typography.headlineSmall)
        }
        items(state.sessionLines) { Text(it) }
        item {
            Text("Noticed at home", style = MaterialTheme.typography.titleMedium)
        }
        items(state.recentPairs, key = { it.value }) { pairId ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(wordLabel(pairId, PrototypeContentRepository.languageNames))
                Button(onClick = { onRecognized(pairId) }) { Text("Recognized outside Miozira") }
                Button(onClick = { onUsed(pairId) }) { Text("Used outside Miozira") }
                onFamilyVoice?.let { openFamilyVoice ->
                    Button(onClick = { openFamilyVoice(pairId) }) { Text("Family voice") }
                }
            }
        }
        state.notice?.let { notice ->
            item { Text(if (notice == ParentUiState.Notice.SAVED) "Saved." else "Could not save this yet.") }
        }
    }
}
