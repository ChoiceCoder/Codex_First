package com.govtprep.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.govtprep.ui.theme.Accent
import com.govtprep.ui.theme.Card
import com.govtprep.ui.theme.TextSecondary

@Composable
fun PremiumCard(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
    val alpha by animateFloatAsState(targetValue = 1f, label = "card_alpha")
    Card(
        modifier = modifier.alpha(alpha),
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, color = TextSecondary, style = MaterialTheme.typography.labelLarge)
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
            Text(text = subtitle, color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun TrendGraph(values: List<Float>, modifier: Modifier = Modifier) {
    val safeValues = if (values.isEmpty()) listOf(0f) else values
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(Card, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        val max = (safeValues.maxOrNull() ?: 1f).coerceAtLeast(1f)
        val stepX = size.width / (safeValues.size - 1).coerceAtLeast(1)
        val points = safeValues.mapIndexed { i, v -> Offset(i * stepX, size.height - ((v / max) * size.height)) }
        for (i in 0 until points.lastIndex) {
            drawLine(Accent, points[i], points[i + 1], strokeWidth = 5f)
        }
        points.forEach { p ->
            drawCircle(Accent, radius = 6f, center = p)
            drawCircle(color = Accent.copy(alpha = 0.25f), radius = 12f, center = p, style = Stroke(2f))
        }
    }
}

@Composable
fun HeaderStats(
    streak: Int,
    xp: Int,
    percentile: Double,
    streakLabel: String,
    consistencyLabel: String,
    xpLabel: String,
    growthLabel: String,
    leaderboardLabel: String,
    aheadOfTemplate: String,
    dominationLabel: String
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        PremiumCard(title = streakLabel, value = "$streak", subtitle = consistencyLabel, modifier = Modifier.weight(1f))
        PremiumCard(title = xpLabel, value = "$xp", subtitle = growthLabel, modifier = Modifier.weight(1f))
    }
    PremiumCard(
        title = leaderboardLabel,
        value = aheadOfTemplate.format(percentile.toInt()),
        subtitle = dominationLabel,
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
    )
}
