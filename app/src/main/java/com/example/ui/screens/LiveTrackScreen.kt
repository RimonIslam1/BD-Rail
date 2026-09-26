package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.TrainEntity
import com.example.data.local.TrainStopEntity
import com.example.ui.TimeCalculator
import com.example.ui.theme.SignalDelayCrimson
import com.example.ui.theme.SignalOnTimeGreen
import com.example.ui.theme.SignalWarningAmber
import kotlin.math.roundToInt

@Composable
fun LiveTrackScreen(
    trains: List<TrainEntity>,
    allStops: List<TrainStopEntity>,
    searchQuery: String,
    selectedZoneFilter: String,
    isOfflineModeActive: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onZoneFilterChange: (String) -> Unit,
    onSelectTrain: (Int) -> Unit,
    onBookTrain: (Int) -> Unit,
    onToggleFavorite: (TrainEntity) -> Unit,
    onToggleAlert: (TrainEntity) -> Unit,
    onToggleOfflineMode: () -> Unit
) {
    var showNetworkRadar by remember { mutableStateOf(true) }
    val zoneFilters = listOf("All", "East Zone", "West Zone", "On Time", "Delayed", "Favorites")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("live_track_list"),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Banner with Live Telemetry & Offline Status
        item {
            HeroRailHeaderCard(
                totalTrains = trains.size,
                onTimeCount = trains.count { it.delayMinutes == 0 },
                isOfflineModeActive = isOfflineModeActive,
                showRadar = showNetworkRadar,
                onToggleRadar = { showNetworkRadar = !showNetworkRadar },
                onToggleOfflineMode = onToggleOfflineMode
            )
        }

        // Interactive Live Bangladesh Railway Network Corridor Radar
        item {
            AnimatedVisibility(visible = showNetworkRadar) {
                LiveNetworkCorridorCard(
                    trains = trains,
                    onSelectTrain = onSelectTrain
                )
            }
        }

        // Search & Filter Controls
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_trains_input"),
                    placeholder = {
                        Text(
                            "Search train name, code (701, 814), or station…",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search trains"
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchQueryChange("") },
                                modifier = Modifier.testTag("clear_search_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    zoneFilters.forEach { filter ->
                        val selected = selectedZoneFilter == filter
                        FilterChip(
                            selected = selected,
                            onClick = { onZoneFilterChange(filter) },
                            label = {
                                Text(
                                    text = filter,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.testTag("filter_chip_${filter.lowercase().replace(" ", "_")}")
                        )
                    }
                }
            }
        }

        if (trains.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Train,
                            contentDescription = null,
                            modifier = Modifier.size(42.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "No matching Bangladesh Railway trains",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Try searching by train number (e.g. 701, 787, 814, 759) or station name.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(trains, key = { it.trainCode }) { train ->
                val trainStops = remember(allStops, train.trainCode) {
                    allStops.filter { it.trainCode == train.trainCode }.sortedBy { it.stopOrder }
                }
                LiveTrainTrackerCard(
                    train = train,
                    stops = trainStops,
                    onSelectTrain = { onSelectTrain(train.trainCode) },
                    onBookTrain = { onBookTrain(train.trainCode) },
                    onToggleFavorite = { onToggleFavorite(train) },
                    onToggleAlert = { onToggleAlert(train) }
                )
            }
        }
    }
}

@Composable
private fun HeroRailHeaderCard(
    totalTrains: Int,
    onTimeCount: Int,
    isOfflineModeActive: Boolean,
    showRadar: Boolean,
    onToggleRadar: () -> Unit,
    onToggleOfflineMode: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(205.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_rail),
                contentDescription = "Bangladesh Railway Express crossing bridge",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x99031A13),
                                Color(0xCC041E16),
                                Color(0xF206231A)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFF005B42).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.clickable { onToggleOfflineMode() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isOfflineModeActive) Icons.Default.CloudOff else Icons.Default.CloudDone,
                                contentDescription = "Offline database state",
                                tint = Color(0xFF65FFCE),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isOfflineModeActive) "OFFLINE MODE ACTIVE • SQLITE" else "LIVE BR TELEMETRY + OFFLINE READY",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }

                    AssistChip(
                        onClick = onToggleRadar,
                        label = {
                            Text(
                                text = if (showRadar) "Hide Radar" else "Live Radar",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Radar,
                                contentDescription = "Toggle live corridor radar",
                                tint = Color(0xFF43E2B3),
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color.Black.copy(alpha = 0.45f)
                        )
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "বাংলাদেশ রেলওয়ে • LIVE TRACKER",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF65FFCE)
                    )
                    Text(
                        text = "Real-Time Train Location, Platforms & ETAs",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Text(
                        text = "$totalTrains Intercity Expresses Active • $onTimeCount Running On Schedule",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD5E8E0)
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveNetworkCorridorCard(
    trains: List<TrainEntity>,
    onSelectTrain: (Int) -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Radar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "Bangladesh Rail Live Corridor Radar",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Tap any train pill below to inspect live station platforms & ETAs",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Schematic Canvas of Bangladesh Railway Corridors centered at Dhaka Kamalapur
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                    .padding(12.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val dhaka = Offset(w * 0.48f, h * 0.52f)
                    val chattogram = Offset(w * 0.82f, h * 0.78f)
                    val coxsBazar = Offset(w * 0.93f, h * 0.92f)
                    val sylhet = Offset(w * 0.86f, h * 0.18f)
                    val rajshahi = Offset(w * 0.14f, h * 0.46f)
                    val rangpur = Offset(w * 0.20f, h * 0.14f)
                    val khulna = Offset(w * 0.24f, h * 0.86f)

                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)

                    // Draw railway corridors
                    val corridors = listOf(
                        dhaka to chattogram,
                        chattogram to coxsBazar,
                        dhaka to sylhet,
                        dhaka to rajshahi,
                        dhaka to rangpur,
                        dhaka to khulna
                    )
                    corridors.forEach { (start, end) ->
                        drawLine(
                            color = outlineColor,
                            start = start,
                            end = end,
                            strokeWidth = 5f,
                            cap = StrokeCap.Round,
                            pathEffect = dashEffect
                        )
                    }

                    // Draw hub nodes
                    val hubs = listOf(dhaka, chattogram, coxsBazar, sylhet, rajshahi, rangpur, khulna)
                    hubs.forEach { hub ->
                        drawCircle(
                            color = primaryColor.copy(alpha = 0.25f),
                            radius = 12f,
                            center = hub
                        )
                        drawCircle(
                            color = primaryColor,
                            radius = 6f,
                            center = hub
                        )
                    }

                    // Plot live trains along corridors
                    trains.forEachIndexed { idx, train ->
                        val p = train.currentProgressFraction.coerceIn(0.15f, 0.88f)
                        val (start, end) = when (train.trainCode) {
                            701 -> chattogram to dhaka
                            787 -> dhaka to chattogram
                            814 -> dhaka to coxsBazar
                            759 -> dhaka to rajshahi
                            709 -> dhaka to sylhet
                            726 -> dhaka to khulna
                            771, 793 -> dhaka to rangpur
                            else -> dhaka to chattogram
                        }
                        val pos = Offset(
                            x = start.x + (end.x - start.x) * p,
                            y = start.y + (end.y - start.y) * p + (if (idx % 2 == 0) -4f else 4f)
                        )
                        val dotColor = if (train.delayMinutes == 0) SignalOnTimeGreen else secondaryColor
                        drawCircle(
                            color = dotColor.copy(alpha = 0.3f),
                            radius = 16f,
                            center = pos
                        )
                        drawCircle(
                            color = dotColor,
                            radius = 7.5f,
                            center = pos
                        )
                    }
                }

                // Hub Labels overlay
                Text(
                    text = "RGP / PCG",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = "SYLHET",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
                Text(
                    text = "DHAKA HUB",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
                Text(
                    text = "KHULNA (Padma Link)",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
                Text(
                    text = "CTG • COX'S BAZAR",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }

            // Quick horizontal train pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                trains.forEach { train ->
                    val statusColor = when {
                        train.delayMinutes == 0 -> SignalOnTimeGreen
                        train.delayMinutes <= 15 -> SignalWarningAmber
                        else -> SignalDelayCrimson
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { onSelectTrain(train.trainCode) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                            Text(
                                text = "${train.trainCode} ${train.name.substringBefore(" ")}",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = if (train.delayMinutes == 0) "On Time" else "+${train.delayMinutes}m",
                                style = MaterialTheme.typography.labelSmall,
                                color = statusColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveTrainTrackerCard(
    train: TrainEntity,
    stops: List<TrainStopEntity>,
    onSelectTrain: () -> Unit,
    onBookTrain: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleAlert: () -> Unit
) {
    val currentDistKm = (train.totalDistanceKm * train.currentProgressFraction).roundToInt()
    val nextStop = stops.firstOrNull { it.distanceFromOriginKm > currentDistKm } ?: stops.lastOrNull()
    val originStop = stops.firstOrNull()
    val destStop = stops.lastOrNull()

    val estimatedFinalArrival = TimeCalculator.addMinutesToTime(train.arrivalTime, train.delayMinutes)
    val nextStopEta = nextStop?.let {
        val baseTime = if (it.scheduledArrival == "Origin") train.departureTime else it.scheduledArrival
        TimeCalculator.addMinutesToTime(baseTime, train.delayMinutes)
    } ?: estimatedFinalArrival

    val delayColor = when {
        train.delayMinutes == 0 -> SignalOnTimeGreen
        train.delayMinutes <= 15 -> SignalWarningAmber
        else -> SignalDelayCrimson
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onSelectTrain() }
            .testTag("train_card_${train.trainCode}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row: Train Code, Name, Bengali Name, Alert & Bookmark buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "#${train.trainCode}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = train.zone.substringBefore(" ").uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = train.name,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${train.bengaliName} • Off-Day: ${train.offDay}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleAlert,
                        modifier = Modifier.testTag("toggle_alert_${train.trainCode}")
                    ) {
                        Icon(
                            imageVector = if (train.isSubscribedAlert) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                            contentDescription = "Toggle delay push alerts",
                            tint = if (train.isSubscribedAlert) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.testTag("toggle_fav_${train.trainCode}")
                    ) {
                        Icon(
                            imageVector = if (train.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark train",
                            tint = if (train.isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Origin -> Destination with Scheduled & Live Estimated Times + Platforms
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = train.originStation,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Dep: ${train.departureTime}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = originStop?.platformNumber ?: "Platform 1",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Surface(
                            color = delayColor.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = if (train.delayMinutes == 0) "ON TIME" else "DELAYED +${train.delayMinutes}M",
                                style = MaterialTheme.typography.labelSmall,
                                color = delayColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${train.totalDistanceKm} km",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = train.destinationStation,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "ETA: $estimatedFinalArrival",
                            style = MaterialTheme.typography.labelLarge,
                            color = delayColor
                        )
                        Text(
                            text = destStop?.platformNumber ?: "Platform 2",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Live Location Progress & Speed / Next Station Platform
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${train.currentSpeedKmh} km/h • $currentDistKm / ${train.totalDistanceKm} km",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (nextStop != null) {
                        Text(
                            text = "Next: ${nextStop.stationName} ($nextStopEta • ${nextStop.platformNumber})",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { train.currentProgressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50)),
                    color = delayColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Text(
                    text = train.currentStatusNote,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onSelectTrain,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("details_button_${train.trainCode}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Train,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Live Stops & Map")
                }

                Button(
                    onClick = onBookTrain,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("book_button_${train.trainCode}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Book Seat (৳${train.baseFareSChair}+)")
                }
            }
        }
    }
}
