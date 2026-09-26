package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.DelayAlertLogEntity
import com.example.data.local.OfflineZoneSyncEntity
import com.example.data.local.TrainEntity
import com.example.data.local.TrainStopEntity
import com.example.ui.theme.SignalDelayCrimson
import com.example.ui.theme.SignalOnTimeGreen
import com.example.ui.theme.SignalWarningAmber
import com.example.util.RailNotificationHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OfflineTimetableScreen(
    trains: List<TrainEntity>,
    allStops: List<TrainStopEntity>,
    offlineZones: List<OfflineZoneSyncEntity>,
    selectedStationFilter: String,
    isOfflineModeActive: Boolean,
    onStationFilterChange: (String) -> Unit,
    onToggleOfflineMode: () -> Unit,
    onRefreshZonePack: (OfflineZoneSyncEntity) -> Unit,
    onSelectTrain: (Int) -> Unit
) {
    val stationFilters = remember(allStops) {
        listOf("All Stations") + allStops.map { it.stationName }.distinct().sorted()
    }
    val filteredStops = remember(allStops, selectedStationFilter) {
        if (selectedStationFilter == "All Stations") {
            allStops
        } else {
            allStops.filter { it.stationName == selectedStationFilter }
        }
    }
    val trainMap = remember(trains) { trains.associateBy { it.trainCode } }
    val dateFormat = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("offline_timetable_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Offline Mode Guarantee Card for Low-Connectivity Rural/River Travel
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (isOfflineModeActive) Icons.Default.CloudOff else Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    text = "Low-Connectivity Offline Vault",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = "100% of schedules, platforms & fares cached in Room SQLite",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isOfflineModeActive,
                            onCheckedChange = { onToggleOfflineMode() },
                            modifier = Modifier.testTag("offline_mode_switch")
                        )
                    }

                    Text(
                        text = "Traveling across Jamuna Bridge, Haor wetlands, or hill tracts with no mobile signal? Toggle Offline Mode anytime — all timetables and saved boarding passes work without internet.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Offline Corridor Database Packs
        item {
            Text(
                text = "Downloaded Bangladesh Railway Corridor Packs",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(offlineZones, key = { it.zoneId }) { zone ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DownloadDone,
                                contentDescription = null,
                                tint = SignalOnTimeGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = zone.zoneName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = zone.corridorSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${zone.routesCount} routes • ${zone.stationsCount} stations • ${zone.sizeKb} KB • Verified ${dateFormat.format(Date(zone.lastSyncedTimestamp))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = { onRefreshZonePack(zone) },
                        modifier = Modifier.testTag("sync_zone_${zone.zoneId}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Verify offline corridor pack"
                        )
                    }
                }
            }
        }

        // Offline Station Master Timetable Lookup
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Offline Station & Platform Timetable Directory",
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stationFilters.forEach { station ->
                        FilterChip(
                            selected = selectedStationFilter == station,
                            onClick = { onStationFilterChange(station) },
                            label = { Text(station) }
                        )
                    }
                }
            }
        }

        items(filteredStops, key = { it.id }) { stop ->
            val train = trainMap[stop.trainCode]
            if (train != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    onClick = { onSelectTrain(train.trainCode) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${stop.stationName} (${stop.stationCode}) • ${stop.platformNumber}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${train.name} (#${train.trainCode}) • ${train.originStation} → ${train.destinationStation}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Weekly Off: ${train.offDay} • Distance: ${stop.distanceFromOriginKm} km",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Arr: ${stop.scheduledArrival}",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "Dep: ${stop.scheduledDeparture}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DelayAlertsScreen(
    trains: List<TrainEntity>,
    alertLogs: List<DelayAlertLogEntity>,
    onToggleTrainSubscription: (TrainEntity) -> Unit,
    onTriggerLiveDelayNotification: (TrainEntity, Int) -> Unit,
    onMarkAllRead: () -> Unit,
    onDeleteAlert: (Int) -> Unit
) {
    val context = LocalContext.current
    var hasNotifPermission by remember {
        mutableStateOf(RailNotificationHelper.hasNotificationPermission(context))
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotifPermission = granted
    }
    val timeFormat = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("delay_alerts_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Push Notification Control & Permission Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = "Real-Time Delay & Platform Push Alerts",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = if (hasNotifPermission) "System Push Notifications Enabled"
                                else "Grant notification permission for status-bar delay alerts",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (!hasNotifPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        OutlinedButton(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Enable Android Push Notification Permission")
                        }
                    }

                    Button(
                        onClick = {
                            if (!hasNotifPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            val targetTrain = trains.firstOrNull { it.isSubscribedAlert } ?: trains.firstOrNull()
                            if (targetTrain != null) {
                                onTriggerLiveDelayNotification(targetTrain, 12)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trigger_push_alert_button")
                    ) {
                        Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Broadcast Live Schedule & Delay Push Alert Now")
                    }
                }
            }
        }

        // Subscribed Trains Watchlist
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Train Delay Subscription Watchlist",
                        style = MaterialTheme.typography.titleMedium
                    )
                    trains.forEach { train ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Train,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column {
                                    Text(
                                        text = "${train.name} (#${train.trainCode})",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = "${train.originStation} → ${train.destinationStation}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Switch(
                                checked = train.isSubscribedAlert,
                                onCheckedChange = { onToggleTrainSubscription(train) },
                                modifier = Modifier.testTag("alert_switch_${train.trainCode}")
                            )
                        }
                    }
                }
            }
        }

        // Live Alert Feed Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Schedule & Delay Feed (${alertLogs.size})",
                    style = MaterialTheme.typography.titleLarge
                )
                if (alertLogs.any { !it.isRead }) {
                    OutlinedButton(
                        onClick = onMarkAllRead,
                        modifier = Modifier.testTag("mark_all_alerts_read_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mark Read")
                    }
                }
            }
        }

        items(alertLogs, key = { it.id }) { alert ->
            val badgeColor = when {
                alert.delayMinutes == 0 -> SignalOnTimeGreen
                alert.delayMinutes <= 15 -> SignalWarningAmber
                else -> SignalDelayCrimson
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (!alert.isRead) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = badgeColor.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = if (alert.delayMinutes == 0) "ON SCHEDULE" else "DELAY +${alert.delayMinutes} MIN",
                                style = MaterialTheme.typography.labelSmall,
                                color = badgeColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = timeFormat.format(Date(alert.timestamp)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            IconButton(
                                onClick = { onDeleteAlert(alert.id) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete alert",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = alert.alertTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = alert.alertMessage,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    HorizontalDivider()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = alert.platformInfo,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
