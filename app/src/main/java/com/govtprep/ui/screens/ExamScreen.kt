package com.govtprep.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.govtprep.R
import com.govtprep.viewmodel.ExamUiState

@Composable
fun ExamScreen(
    state: ExamUiState,
    onSelect: (String, Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSubmit: () -> Unit
) {
    when (state) {
        is ExamUiState.Loading -> Text(stringResource(R.string.loading))
        is ExamUiState.Error -> Text(state.message)
        is ExamUiState.Submitted -> {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.score_format, state.result.score))
                Text(stringResource(R.string.percentage_format, state.result.percentage))
                Text(stringResource(R.string.percentile_format, state.result.percentile))
                Text(stringResource(R.string.xp_earned_format, state.result.xpEarned))
            }
        }
        is ExamUiState.Running -> {
            val q = state.questions[state.index]
            Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(stringResource(R.string.timer_format, state.remainingSeconds))
                Text(stringResource(R.string.question_number_format, state.index + 1, state.questions.size))
                Text(q.prompt)
                q.options.forEachIndexed { idx, option ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        RadioButton(selected = state.selections[q.id] == idx, onClick = { onSelect(q.id, idx) })
                        Text(option, modifier = Modifier.padding(top = 12.dp))
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onPrev) { Text(stringResource(R.string.previous)) }
                    Button(onClick = onNext) { Text(stringResource(R.string.next)) }
                    Button(onClick = onSubmit) { Text(stringResource(R.string.submit)) }
                }
            }
        }
        ExamUiState.Idle -> Text(stringResource(R.string.loading))
    }
}
