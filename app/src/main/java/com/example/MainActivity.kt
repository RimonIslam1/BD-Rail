package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.RailDatabase
import com.example.data.repository.RailRepository
import com.example.ui.AppTab
import com.example.ui.RailViewModel
import com.example.ui.screens.DelayAlertsScreen
import com.example.ui.screens.LiveTrackScreen
import com.example.ui.screens.OfflineTimetableScreen
import com.example.ui.screens.SeatBookingScreen
import com.example.ui.screens.TrainDetailScreen
import com.example.ui.screens.TripPlannerScreen
import com.example.ui.theme.BDRailTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = RailDatabase.getInstance(applicationContext)
        val repository = RailRepository(database.railDao())

        setContent {
            val railViewModel: RailViewModel = viewModel(
                factory = RailViewModel.Factory(repository, applicationContext)
            )
            val isDarkMode by railViewModel.isDarkMode.collectAsStateWithLifecycle()

            BDRailTrackerTheme(darkTheme = isDarkMode) {
                BDRailTrackerApp(viewModel = railViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BDRailTrackerApp(viewModel: RailViewModel) {
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val isOfflineModeActive by viewModel.isOfflineModeActive.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedTrainCode by viewModel.selectedTrainCode.collectAsStateWithLifecycle()

    val allTrains by viewModel.allTrains.collectAsStateWithLifecycle()
    val filteredTrains by viewModel.filteredTrains.collectAsStateWithLifecycle()
    val allStops by viewModel.allStops.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedZoneFilter by viewModel.selectedZoneFilter.collectAsStateWithLifecycle()

    val plannerFrom by viewModel.plannerFromStation.collectAsStateWithLifecycle()
    val plannerTo by viewModel.plannerToStation.collectAsStateWithLifecycle()
    val plannerDay by viewModel.plannerDayOfWeek.collectAsStateWithLifecycle()
    val plannedTrips by viewModel.plannedTrips.collectAsStateWithLifecycle()

    val bookingTrainCode by viewModel.bookingTrainCode.collectAsStateWithLifecycle()
    val bookingSeatClass by viewModel.bookingSeatClass.collectAsStateWithLifecycle()
    val bookingCoach by viewModel.bookingCoach.collectAsStateWithLifecycle()
    val bookingDate by viewModel.bookingDate.collectAsStateWithLifecycle()
    val selectedSeats by viewModel.selectedSeats.collectAsStateWithLifecycle()
    val bookedTickets by viewModel.bookedTickets.collectAsStateWithLifecycle()
    val bookingSuccessMessage by viewModel.bookingSuccessMessage.collectAsStateWithLifecycle()

    val offlineZones by viewModel.offlineZones.collectAsStateWithLifecycle()
    val timetableStationFilter by viewModel.timetableStationFilter.collectAsStateWithLifecycle()
    val alertLogs by viewModel.alertLogs.collectAsStateWithLifecycle()

    val unreadAlertsCount = alertLogs.count { !it.isRead }
    val activeDetailTrain = allTrains.firstOrNull { it.trainCode == selectedTrainCode }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Train,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "BD Rail Tracker",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isDarkMode) "Night Rail Mode • Live & Offline"
                                else "Daylight Mode • Live & Offline",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    if (isOfflineModeActive) {
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudOff,
                                    contentDescription = "Offline mode active",
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = "Offline",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }

                    FilledTonalIconButton(
                        onClick = { viewModel.toggleDarkMode() },
                        modifier = Modifier.testTag("dark_mode_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Night Rail Dark Mode"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == AppTab.LIVE_TRACK && activeDetailTrain == null,
                    onClick = {
                        viewModel.openTrainDetails(null)
                        viewModel.selectTab(AppTab.LIVE_TRACK)
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.LIVE_TRACK) Icons.Filled.Train else Icons.Outlined.Train,
                            contentDescription = "Live Track"
                        )
                    },
                    label = { Text("Live Track") },
                    modifier = Modifier.testTag("nav_live_track")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.TRIP_PLANNER && activeDetailTrain == null,
                    onClick = {
                        viewModel.openTrainDetails(null)
                        viewModel.selectTab(AppTab.TRIP_PLANNER)
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.TRIP_PLANNER) Icons.Filled.Route else Icons.Outlined.Route,
                            contentDescription = "Trip Planner"
                        )
                    },
                    label = { Text("Planner") },
                    modifier = Modifier.testTag("nav_trip_planner")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.SEAT_BOOKING && activeDetailTrain == null,
                    onClick = {
                        viewModel.openTrainDetails(null)
                        viewModel.selectTab(AppTab.SEAT_BOOKING)
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.SEAT_BOOKING) Icons.Filled.ConfirmationNumber else Icons.Outlined.ConfirmationNumber,
                            contentDescription = "Book Seats"
                        )
                    },
                    label = { Text("Seats") },
                    modifier = Modifier.testTag("nav_seat_booking")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.OFFLINE_TIMETABLE && activeDetailTrain == null,
                    onClick = {
                        viewModel.openTrainDetails(null)
                        viewModel.selectTab(AppTab.OFFLINE_TIMETABLE)
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.OFFLINE_TIMETABLE) Icons.Filled.Schedule else Icons.Outlined.Schedule,
                            contentDescription = "Offline Timetables"
                        )
                    },
                    label = { Text("Offline") },
                    modifier = Modifier.testTag("nav_offline_timetable")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.DELAY_ALERTS && activeDetailTrain == null,
                    onClick = {
                        viewModel.openTrainDetails(null)
                        viewModel.selectTab(AppTab.DELAY_ALERTS)
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (unreadAlertsCount > 0) {
                                    Badge { Text("$unreadAlertsCount") }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppTab.DELAY_ALERTS) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                                contentDescription = "Delay Alerts"
                            )
                        }
                    },
                    label = { Text("Alerts") },
                    modifier = Modifier.testTag("nav_delay_alerts")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (activeDetailTrain != null) {
                val stopsWithEstimates = viewModel.getStopsWithEstimates(activeDetailTrain, allStops)
                TrainDetailScreen(
                    train = activeDetailTrain,
                    stopsWithEstimates = stopsWithEstimates,
                    onBack = { viewModel.openTrainDetails(null) },
                    onBookSeats = { viewModel.startBookingForTrain(activeDetailTrain.trainCode) },
                    onSimulateDelayUpdate = { deltaMinutes ->
                        viewModel.triggerLiveDelayUpdate(activeDetailTrain, deltaMinutes)
                    },
                    onToggleAlertSubscription = {
                        viewModel.toggleAlertSubscription(activeDetailTrain)
                    }
                )
            } else {
                when (currentTab) {
                    AppTab.LIVE_TRACK -> {
                        LiveTrackScreen(
                            trains = filteredTrains,
                            allStops = allStops,
                            searchQuery = searchQuery,
                            selectedZoneFilter = selectedZoneFilter,
                            isOfflineModeActive = isOfflineModeActive,
                            onSearchQueryChange = viewModel::updateSearchQuery,
                            onZoneFilterChange = viewModel::updateZoneFilter,
                            onSelectTrain = viewModel::openTrainDetails,
                            onBookTrain = { trainCode -> viewModel.startBookingForTrain(trainCode) },
                            onToggleFavorite = viewModel::toggleFavorite,
                            onToggleAlert = viewModel::toggleAlertSubscription,
                            onToggleOfflineMode = viewModel::toggleOfflineMode
                        )
                    }

                    AppTab.TRIP_PLANNER -> {
                        TripPlannerScreen(
                            allStations = viewModel.allStationNames,
                            fromStation = plannerFrom,
                            toStation = plannerTo,
                            selectedDay = plannerDay,
                            directTrips = plannedTrips.first,
                            connectingTrips = plannedTrips.second,
                            onUpdateStations = viewModel::updatePlannerStations,
                            onSwapStations = viewModel::swapPlannerStations,
                            onUpdateDay = viewModel::updatePlannerDay,
                            onInspectTrain = viewModel::openTrainDetails,
                            onBookTrip = { trainCode, seatClass ->
                                viewModel.startBookingForTrain(trainCode, seatClass)
                            }
                        )
                    }

                    AppTab.SEAT_BOOKING -> {
                        SeatBookingScreen(
                            trains = allTrains,
                            selectedTrainCode = bookingTrainCode,
                            selectedSeatClass = bookingSeatClass,
                            selectedCoach = bookingCoach,
                            selectedDate = bookingDate,
                            selectedSeats = selectedSeats,
                            bookedTickets = bookedTickets,
                            bookingSuccessMessage = bookingSuccessMessage,
                            onSelectTrain = viewModel::selectBookingTrain,
                            onSelectSeatClass = viewModel::selectBookingClass,
                            onSelectCoach = viewModel::selectBookingCoach,
                            onSelectDate = viewModel::updateBookingDate,
                            onToggleSeat = viewModel::toggleSeatSelection,
                            onConfirmBooking = viewModel::confirmSeatBooking,
                            onCancelTicket = viewModel::cancelBookedTicket,
                            onDismissSuccess = viewModel::clearBookingSuccessMessage
                        )
                    }

                    AppTab.OFFLINE_TIMETABLE -> {
                        OfflineTimetableScreen(
                            trains = allTrains,
                            allStops = allStops,
                            offlineZones = offlineZones,
                            selectedStationFilter = timetableStationFilter,
                            isOfflineModeActive = isOfflineModeActive,
                            onStationFilterChange = viewModel::updateTimetableStationFilter,
                            onToggleOfflineMode = viewModel::toggleOfflineMode,
                            onRefreshZonePack = viewModel::refreshOfflineZonePack,
                            onSelectTrain = viewModel::openTrainDetails
                        )
                    }

                    AppTab.DELAY_ALERTS -> {
                        DelayAlertsScreen(
                            trains = allTrains,
                            alertLogs = alertLogs,
                            onToggleTrainSubscription = viewModel::toggleAlertSubscription,
                            onTriggerLiveDelayNotification = { train, mins ->
                                viewModel.triggerLiveDelayUpdate(train, mins)
                            },
                            onMarkAllRead = viewModel::markAllAlertsRead,
                            onDeleteAlert = viewModel::deleteAlert
                        )
                    }
                }
            }
        }
    }
}
