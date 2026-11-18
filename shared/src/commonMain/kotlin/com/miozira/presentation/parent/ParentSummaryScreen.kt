package com.miozira.presentation.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    onOpenLanguages: () -> Unit,
    onOpenMicrophone: () -> Unit,
    onFamilyVoice: ((PairId) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { Text("Miozira for parents", style = MaterialTheme.typography.headlineSmall) }
        if (state.notice == ParentUiState.Notice.READ_ERROR) {
            item { Text("Some of this page could not be shown. Try opening it again later.") }
        }
        if (state.wordsForToday.isNotEmpty()) {
            item {
                ParentCard {
                    Text("Four words for today", style = MaterialTheme.typography.titleMedium)
                    state.wordsForToday.forEach { Text(it) }
                }
            }
        }
        item {
            ParentCard { state.sessionLines.forEach { Text(it) } }
        }
        if (state.recentPairs.isNotEmpty()) {
            item {
                ParentCard {
                    Text("Noticed at home", style = MaterialTheme.typography.titleMedium)
                    state.recentPairs.forEach { pairId ->
                        Text(wordLabel(pairId, PrototypeContentRepository.languageNames))
                        Button(onClick = { onRecognized(pairId) }) { Text("He understood it") }
                        Button(onClick = { onUsed(pairId) }) { Text("He said it") }
                        onFamilyVoice?.let { open ->
                            Button(onClick = { open(pairId) }) { Text("Family voice") }
                        }
                    }
                    when (state.notice) {
                        ParentUiState.Notice.SAVED -> Text("Saved.")
                        ParentUiState.Notice.ERROR -> Text("Could not save this yet.")
                        ParentUiState.Notice.READ_ERROR, null -> Unit
                    }
                }
            }
        }
        item { TextButton(onClick = onOpenLanguages, modifier = Modifier.fillMaxWidth()) { Text("How each language is going") } }
        item { TextButton(onClick = onOpenMicrophone, modifier = Modifier.fillMaxWidth()) { Text("Microphone and privacy") } }
    }
}

@Composable
internal fun ParentCard(content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) { content() }
    }
}
