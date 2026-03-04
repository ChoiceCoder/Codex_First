package com.govtprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.govtprep.R
import com.govtprep.data.model.Test
import com.govtprep.ui.components.HeaderStats
import com.govtprep.ui.components.TrendGraph
import com.govtprep.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    state: HomeUiState,
    onMockTests: () -> Unit,
    onAnalysis: () -> Unit,
    onProfile: () -> Unit,
    onAdmin: () -> Unit
) {
    when (state) {
        is HomeUiState.Loading -> ScreenMessage(stringResource(R.string.loading))
        is HomeUiState.Error -> ScreenMessage(state.message)
        is HomeUiState.Ready -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HeaderStats(
                        streak = state.profile.streak,
                        xp = state.profile.xp,
                        percentile = 88.0,
                        streakLabel = stringResource(R.string.streak_label),
                        consistencyLabel = stringResource(R.string.consistency_label),
                        xpLabel = stringResource(R.string.xp_label),
                        growthLabel = stringResource(R.string.growth_label),
                        leaderboardLabel = stringResource(R.string.leaderboard_label),
                        aheadOfTemplate = stringResource(R.string.ahead_of_template),
                        dominationLabel = stringResource(R.string.domination_label)
                    )
                }
                item {
                    Text(text = stringResource(R.string.performance_trend))
                    TrendGraph(values = state.trend)
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = onMockTests, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.mock_tests)) }
                        Button(onClick = onAnalysis, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.analysis)) }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = onProfile, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.profile)) }
                        if (state.profile.isAdmin) {
                            Button(onClick = onAdmin, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.admin)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MockTestsScreen(tests: List<Test>, onSelect: (Test) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(tests) { test ->
            Card(onClick = { onSelect(test) }, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(test.title)
                    Text(stringResource(R.string.duration_format, test.durationMinutes))
                }
            }
        }
    }
}

@Composable
fun TestScreen(test: Test, onStart: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = test.title)
        Text(text = stringResource(R.string.duration_format, test.durationMinutes))
        Text(text = stringResource(R.string.total_marks_format, test.totalMarks))
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.start_exam)) }
    }
}

@Composable
fun AnalysisScreen(message: String) = ScreenMessage(message)

@Composable
fun ProfileScreen(message: String, onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(message)
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.logout)) }
    }
}

@Composable
fun AdminScreen(message: String) = ScreenMessage(message)

@Composable
private fun ScreenMessage(message: String) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) { Text(message) }
}
