package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DirectionsRailway
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TransferWithinAStation
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.PlannedConnectingTrip
import com.example.ui.PlannedDirectTrip
import com.example.ui.SeatClassOption
import com.example.ui.theme.SignalDelayCrimson
import com.example.ui.theme.SignalOnTimeGreen
import com.example.ui.theme.SignalWarningAmber

@Composable
fun TripPlannerScreen(
    allStations: List<String>,
    fromStation: String,
    toStation: String,
    selectedDay: String,
    directTrips: List<PlannedDirectTrip>,
    connectingTrips: List<PlannedConnectingTrip>,
    onUpdateStations: (String, String) -> Unit,
    onSwapStations: () -> Unit,
    onUpdateDay: (String) -> Unit,
    onInspectTrain: (Int) -> Unit,
    onBookTrip: (Int, SeatClassOption) -> Unit
) {
    val daysOfWeek = listOf("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    val popularCorridors = listOf(
        "Dhaka (Kamalapur)" to "Chattogram",
        "Dhaka (Kamalapur)" to "Cox's Bazar",
        "Dhaka (Kamalapur)" to "Sylhet",
        "Dhaka (Kamalapur)" to "Rajshahi",
        "Dhaka (Kamalapur)" to "Khulna",
        "Dhaka (Kamalapur)" to "Rangpur"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("trip_planner_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Offline-Ready Planner Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            text = "Offline-First Bangladesh Rail Trip Planner",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Computes direct intercity routes, transfer connections, fares, and off-day alerts 100% offline.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Station & Day Selector Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StationDropdownField(
                                label = "ORIGIN STATION",
                                selectedStation = fromStation,
                                allStations = allStations,
                                onSelect = { onUpdateStations(it, toStation) },
                                testTag = "planner_from_station"
                            )
                            StationDropdownField(
                                label = "DESTINATION STATION",
                                selectedStation = toStation,
                                allStations = allStations,
                                onSelect = { onUpdateStations(fromStation, it) },
                                testTag = "planner_to_station"
                            )
                        }

                        FilledTonalIconButton(
                            onClick = onSwapStations,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("swap_stations_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap origin and destination stations"
                            )
                        }
                    }

                    Text(
                        text = "Quick Corridors:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        popularCorridors.forEach { (origin, dest) ->
                            AssistChip(
                                onClick = { onUpdateStations(origin, dest) },
                                label = {
                                    Text(
                                        text = "${origin.substringBefore(" ")} → ${dest.substringBefore(" ")}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            )
                        }
                    }

                    Text(
                        text = "Travel Day (Checks Weekly Train Off-Days):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        daysOfWeek.forEach { day ->
                            FilterChip(
                                selected = selectedDay == day,
                                onClick = { onUpdateDay(day) },
                                label = { Text(day.take(3)) },
                                modifier = Modifier.testTag("planner_day_${day.lowercase()}")
                            )
                        }
                    }
                }
            }
        }

        // Direct Trips Section
        item {
            Text(
                text = "Direct Intercity Trains (${directTrips.size} found)",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (directTrips.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "No single-train direct route from $fromStation to $toStation",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Check the Smart Transfer Connections below or try swapping direction.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(directTrips, key = { "${it.train.trainCode}_${it.fromStop.stationCode}_${it.toStop.stationCode}" }) { trip ->
                DirectPlannedTripCard(
                    trip = trip,
                    selectedDay = selectedDay,
                    onInspectTrain = { onInspectTrain(trip.train.trainCode) },
                    onBookTrip = { seatClass -> onBookTrip(trip.train.trainCode, seatClass) }
                )
            }
        }

        // Connecting Multi-Leg Trips Section
        if (connectingTrips.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Smart Hub Transfer Connections (${connectingTrips.size})",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(
                connectingTrips,
                key = { "${it.firstLeg.train.trainCode}_${it.secondLeg.train.trainCode}_${it.transferStation}" }
            ) { conn ->
                ConnectingPlannedTripCard(
                    connection = conn,
                    onBookFirstLeg = { onBookTrip(conn.firstLeg.train.trainCode, SeatClassOption.S_CHAIR) },
                    onBookSecondLeg = { onBookTrip(conn.secondLeg.train.trainCode, SeatClassOption.S_CHAIR) }
                )
            }
        }
    }
}

@Composable
private fun StationDropdownField(
    label: String,
    selectedStation: String,
    allStations: List<String>,
    onSelect: (String) -> Unit,
    testTag: String
) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = selectedStation,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allStations.forEach { station ->
                    DropdownMenuItem(
                        text = { Text(station) },
                        onClick = {
                            onSelect(station)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DirectPlannedTripCard(
    trip: PlannedDirectTrip,
    selectedDay: String,
    onInspectTrain: () -> Unit,
    onBookTrip: (SeatClassOption) -> Unit
) {
    val train = trip.train
    val delayColor = if (train.delayMinutes == 0) SignalOnTimeGreen else SignalWarningAmber

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRailway,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "${train.name} (#${train.trainCode})",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${train.bengaliName} • ${trip.segmentDistanceKm} km • ${trip.durationText}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = delayColor.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (train.delayMinutes == 0) "ON TIME" else "+${train.delayMinutes}M DELAY",
                        style = MaterialTheme.typography.labelSmall,
                        color = delayColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Off-Day Warning if applicable
            if (trip.isOffDayOnSelectedDay) {
                Surface(
                    color = SignalDelayCrimson.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = SignalDelayCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Weekly Off-Day Warning: ${train.name} does not run on $selectedDay!",
                            style = MaterialTheme.typography.labelMedium,
                            color = SignalDelayCrimson
                        )
                    }
                }
            }

            // Segment Times & Platforms
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = trip.fromStop.stationName, style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = "ETD: ${trip.estimatedDeparture}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = trip.fromStop.platformNumber,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = trip.toStop.stationName, style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = "ETA: ${trip.estimatedArrival}",
                            style = MaterialTheme.typography.titleMedium,
                            color = delayColor
                        )
                        Text(
                            text = trip.toStop.platformNumber,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Class Fare Matrix Buttons
            Text(
                text = "Tap a Class to Book Seats Instantly:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FareClassPill(
                    title = "S_Chair",
                    fare = trip.fareSChair,
                    modifier = Modifier.weight(1f),
                    onClick = { onBookTrip(SeatClassOption.S_CHAIR) }
                )
                FareClassPill(
                    title = "Snigdha AC",
                    fare = trip.fareSnigdha,
                    modifier = Modifier.weight(1f),
                    onClick = { onBookTrip(SeatClassOption.SNIGDHA) }
                )
                FareClassPill(
                    title = "AC Berth",
                    fare = trip.fareAcBerth,
                    modifier = Modifier.weight(1f),
                    onClick = { onBookTrip(SeatClassOption.AC_B) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onInspectTrain,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Live Track & Stops")
                }
                Button(
                    onClick = { onBookTrip(SeatClassOption.SNIGDHA) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Book Seat")
                }
            }
        }
    }
}

@Composable
private fun FareClassPill(
    title: String,
    fare: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "৳$fare",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun ConnectingPlannedTripCard(
    connection: PlannedConnectingTrip,
    onBookFirstLeg: () -> Unit,
    onBookSecondLeg: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                    imageVector = Icons.Default.TransferWithinAStation,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Column {
                    Text(
                        text = "Transfer at ${connection.transferStation}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Total Distance: ${connection.totalDistanceKm} km • Combined S_Chair: ৳${connection.totalFareSChair}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider()

            Text(
                text = "Leg 1: ${connection.firstLeg.train.name} (#${connection.firstLeg.train.trainCode}) • ${connection.firstLeg.fromStop.stationName} (${connection.firstLeg.estimatedDeparture}) → ${connection.transferStation} (${connection.firstLeg.estimatedArrival})",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Layover at ${connection.transferStation}: ${connection.layoverText}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "Leg 2: ${connection.secondLeg.train.name} (#${connection.secondLeg.train.trainCode}) • ${connection.transferStation} (${connection.secondLeg.estimatedDeparture}) → ${connection.secondLeg.toStop.stationName} (${connection.secondLeg.estimatedArrival})",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onBookFirstLeg,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Book Leg 1 (#${connection.firstLeg.train.trainCode})")
                }
                OutlinedButton(
                    onClick = onBookSecondLeg,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Book Leg 2 (#${connection.secondLeg.train.trainCode})")
                }
            }
        }
    }
}
