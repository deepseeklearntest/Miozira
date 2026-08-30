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

@Composable
fun ParentSummaryScreen(
    state: ParentUiState,
    onRecognized: (ParentPairActivity) -> Unit,
    onUsed: (ParentPairActivity) -> Unit,
    onFamilyVoice: ((ParentPairActivity) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Recent learning", style = MaterialTheme.typography.headlineSmall)
        }
        items(state.activities, key = { it.pairId.value }) { activity ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(activity.pairId.value)
                Text("Heard ${activity.exposureCount} times")
                Button(onClick = { onRecognized(activity) }) { Text("Recognized outside Miozira") }
                Button(onClick = { onUsed(activity) }) { Text("Used outside Miozira") }
                onFamilyVoice?.let { openFamilyVoice ->
                    Button(onClick = { openFamilyVoice(activity) }) { Text("Family voice") }
                }
            }
        }
        state.notice?.let { notice ->
            item { Text(if (notice == ParentUiState.Notice.SAVED) "Saved." else "Could not save this yet.") }
        }
    }
}
