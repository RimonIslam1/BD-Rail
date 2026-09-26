package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.TrainEntity
import com.example.ui.StopWithEstimate
import com.example.ui.TimeCalculator
import com.example.ui.theme.SignalDelayCrimson
import com.example.ui.theme.SignalOnTimeGreen
import com.example.ui.theme.SignalWarningAmber
import kotlin.math.roundToInt

@Composable
fun TrainDetailScreen(
    train: TrainEntity,
    stopsWithEstimates: List<StopWithEstimate>,
    onBack: () -> Unit,
    onBookSeats: () -> Unit,
    onSimulateDelayUpdate: (Int) -> Unit,
    onToggleAlertSubscription: () -> Unit
) {
    BackHandler(onBack = onBack)

    val delayColor = when {
        train.delayMinutes == 0 -> SignalOnTimeGreen
        train.delayMinutes <= 15 -> SignalWarningAmber
        else -> SignalDelayCrimson
    }
    val distanceCovered = (train.totalDistanceKm * train.currentProgressFraction).roundToInt()
    val distanceRemaining = (train.totalDistanceKm - distanceCovered).coerceAtLeast(0)
    val finalEta = TimeCalculator.addMinutesToTime(train.arrivalTime, train.delayMinutes)
    val coaches = train.coachesCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("train_detail_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Bar with Back button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_to_live_list_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to train list"
                        )
                    }
                    Column {
                        Text(
                            text = "${train.name} (#${train.trainCode})",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "${train.bengaliName} • ${train.zone} • Weekly Off: ${train.offDay}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onToggleAlertSubscription,
                    modifier = Modifier.testTag("detail_alert_subscribe_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Subscribe to delay alerts",
                        tint = if (train.isSubscribedAlert) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Live Telemetry & GPS Position Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = delayColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (train.delayMinutes == 0) Icons.Default.CheckCircle else Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = delayColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (train.delayMinutes == 0) "RUNNING ON TIME" else "DELAYED BY +${train.delayMinutes} MIN",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = delayColor
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "${train.currentSpeedKmh} km/h",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Text(
                        text = train.currentStatusNote,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Schematic Linear Track Progress Canvas
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val trackBg = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cy = size.height / 2f
                            val startX = 12f
                            val endX = size.width - 12f
                            val totalW = endX - startX
                            val activeX = startX + totalW * train.currentProgressFraction

                            drawLine(
                                color = trackBg,
                                start = Offset(startX, cy),
                                end = Offset(endX, cy),
                                strokeWidth = 10f,
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = primaryColor,
                                start = Offset(startX, cy),
                                end = Offset(activeX, cy),
                                strokeWidth = 10f,
                                cap = StrokeCap.Round
                            )

                            stopsWithEstimates.forEachIndexed { idx, _ ->
                                val ratio = if (stopsWithEstimates.size > 1) {
                                    idx.toFloat() / (stopsWithEstimates.size - 1).toFloat()
                                } else 0f
                                val sx = startX + totalW * ratio
                                drawCircle(
                                    color = if (sx <= activeX) primaryColor else trackBg,
                                    radius = 8f,
                                    center = Offset(sx, cy)
                                )
                            }

                            drawCircle(
                                color = delayColor.copy(alpha = 0.3f),
                                radius = 18f,
                                center = Offset(activeX, cy)
                            )
                            drawCircle(
                                color = delayColor,
                                radius = 10f,
                                center = Offset(activeX, cy)
                            )
                        }
                    }

                    // 3 Telemetry Metric Pillars
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TelemetryStatBox(
                            label = "Covered",
                            value = "$distanceCovered km",
                            subtext = "of ${train.totalDistanceKm} km"
                        )
                        TelemetryStatBox(
                            label = "Remaining",
                            value = "$distanceRemaining km",
                            subtext = "to ${train.destinationStation}"
                        )
                        TelemetryStatBox(
                            label = "Est. Terminus Arrival",
                            value = finalEta,
                            subtext = "Sched: ${train.arrivalTime}"
                        )
                    }
                }
            }
        }

        // Live Schedule & Delay Push Notification Simulator Bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Column {
                            Text(
                                text = "Real-Time Delay & Push Notification Control",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "Test live schedule shifts & receive instant Android push alerts",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { onSimulateDelayUpdate(10) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sim_delay_plus_10")
                        ) {
                            Text("+10m Delay Alert")
                        }
                        FilledTonalButton(
                            onClick = { onSimulateDelayUpdate(-train.delayMinutes) },
                            enabled = train.delayMinutes > 0,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("sim_delay_reset")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restore,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Restore On-Time")
                        }
                    }
                }
            }
        }

        // Rake / Coach Composition & Seat Booking CTA
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Coach Rake Composition & Fares",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "S_Chair: ৳${train.baseFareSChair} • Snigdha AC: ৳${train.baseFareSnigdha} • AC Berth: ৳${train.baseFareAcBerth}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip(
                            onClick = {},
                            label = { Text("LOCO ENGINE") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Train,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                        coaches.forEach { coach ->
                            AssistChip(
                                onClick = onBookSeats,
                                label = { Text(coach) }
                            )
                        }
                    }

                    Button(
                        onClick = onBookSeats,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("detail_book_seats_button")
                    ) {
                        Icon(imageVector = Icons.Default.ConfirmationNumber, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select Coach & Book Seats on ${train.name}")
                    }
                }
            }
        }

        // Station-by-Station Live Timeline Header
        item {
            Text(
                text = "Station-by-Station Live ETA & Platform Schedule",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        itemsIndexed(stopsWithEstimates, key = { _, item -> item.stop.id }) { index, item ->
            StationTimelineStepCard(
                item = item,
                delayMinutes = train.delayMinutes,
                isFirst = index == 0,
                isLast = index == stopsWithEstimates.lastIndex
            )
        }
    }
}

@Composable
private fun TelemetryStatBox(
    label: String,
    value: String,
    subtext: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtext,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StationTimelineStepCard(
    item: StopWithEstimate,
    delayMinutes: Int,
    isFirst: Boolean,
    isLast: Boolean
) {
    val stop = item.stop
    val highlightColor = when {
        item.isCurrentOrNext -> MaterialTheme.colorScheme.primary
        item.isPassed -> SignalOnTimeGreen
        else -> MaterialTheme.colorScheme.outline
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCurrentOrNext) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Track Node Indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (item.isCurrentOrNext) 18.dp else 12.dp)
                        .clip(CircleShape)
                        .background(highlightColor)
                )
            }

            // Station Info & Platform Badge
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stop.stationName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = stop.stationCode,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (item.isCurrentOrNext) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "NEXT STOP",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stop.platformNumber,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = "${stop.distanceFromOriginKm} km from origin • ${if (stop.haltMinutes > 0) "${stop.haltMinutes}m halt" else if (isFirst) "Origin" else if (isLast) "Terminus" else "Pass-through"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Right Column: Scheduled vs Estimated Arrival/Departure
            Column(horizontalAlignment = Alignment.End) {
                if (!isFirst) {
                    Text(
                        text = "ETA: ${item.estimatedArrival}",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (delayMinutes > 0) SignalWarningAmber else SignalOnTimeGreen
                    )
                    Text(
                        text = "Sched: ${stop.scheduledArrival}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (!isLast) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(70.dp)
                            .padding(vertical = 2.dp)
                    )
                    Text(
                        text = "ETD: ${item.estimatedDeparture}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
