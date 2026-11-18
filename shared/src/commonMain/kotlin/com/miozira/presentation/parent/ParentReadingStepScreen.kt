package com.miozira.presentation.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Shows the number as words. Shows nothing about what was typed, and has no cancel. */
@Composable
fun ParentReadingStepScreen(challenge: ChallengeState, onDigit: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF6F3EE)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Parents only")
        Text("Type this number:")
        Text(challenge.words)
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for (digit in 0..9) {
                Button(
                    modifier = Modifier.width(56.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { onDigit(digit) },
                ) { Text(digit.toString()) }
            }
        }
    }
}
