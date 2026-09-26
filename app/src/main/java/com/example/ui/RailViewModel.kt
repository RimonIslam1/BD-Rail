package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.BangladeshRailSeedData
import com.example.data.local.BookedTicketEntity
import com.example.data.local.DelayAlertLogEntity
import com.example.data.local.OfflineZoneSyncEntity
import com.example.data.local.TrainEntity
import com.example.data.local.TrainStopEntity
import com.example.data.repository.RailRepository
import com.example.util.RailNotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

class RailViewModel(
    private val repository: RailRepository,
    private val appContext: Context
) : ViewModel() {

    private val prefs = appContext.getSharedPreferences("bd_rail_prefs", Context.MODE_PRIVATE)

    // Dark mode state (defaults to true for high-contrast Night Rail visibility or saved preference)
    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("night_rail_dark_mode", true))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Simulated offline mode toggle so users can test offline banner & zero-data operation
    private val _isOfflineModeActive = MutableStateFlow(prefs.getBoolean("offline_mode_active", false))
    val isOfflineModeActive: StateFlow<Boolean> = _isOfflineModeActive.asStateFlow()

    // Navigation Tab
    private val _currentTab = MutableStateFlow(AppTab.LIVE_TRACK)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Selected train for live detail modal/screen
    private val _selectedTrainCode = MutableStateFlow<Int?>(null)
    val selectedTrainCode: StateFlow<Int?> = _selectedTrainCode.asStateFlow()

    // Search & filter in Live Track
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedZoneFilter = MutableStateFlow("All")
    val selectedZoneFilter: StateFlow<String> = _selectedZoneFilter.asStateFlow()

    // Trip Planner states
    private val _plannerFromStation = MutableStateFlow("Dhaka (Kamalapur)")
    val plannerFromStation: StateFlow<String> = _plannerFromStation.asStateFlow()

    private val _plannerToStation = MutableStateFlow("Chattogram")
    val plannerToStation: StateFlow<String> = _plannerToStation.asStateFlow()

    private val _plannerDayOfWeek = MutableStateFlow("Saturday")
    val plannerDayOfWeek: StateFlow<String> = _plannerDayOfWeek.asStateFlow()

    // Seat Booking states
    private val _bookingTrainCode = MutableStateFlow(701)
    val bookingTrainCode: StateFlow<Int> = _bookingTrainCode.asStateFlow()

    private val _bookingSeatClass = MutableStateFlow(SeatClassOption.SNIGDHA)
    val bookingSeatClass: StateFlow<SeatClassOption> = _bookingSeatClass.asStateFlow()

    private val _bookingCoach = MutableStateFlow("KA")
    val bookingCoach: StateFlow<String> = _bookingCoach.asStateFlow()

    private val _bookingDate = MutableStateFlow("28 Sep 2026")
    val bookingDate: StateFlow<String> = _bookingDate.asStateFlow()

    private val _selectedSeats = MutableStateFlow<Set<String>>(setOf("KA-7W"))
    val selectedSeats: StateFlow<Set<String>> = _selectedSeats.asStateFlow()

    private val _bookingSuccessMessage = MutableStateFlow<String?>(null)
    val bookingSuccessMessage: StateFlow<String?> = _bookingSuccessMessage.asStateFlow()

    // Offline Timetable Station Filter
    private val _timetableStationFilter = MutableStateFlow("All Stations")
    val timetableStationFilter: StateFlow<String> = _timetableStationFilter.asStateFlow()

    // Database streams
    val allTrains: StateFlow<List<TrainEntity>> = repository.allTrains.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allStops: StateFlow<List<TrainStopEntity>> = repository.allTrainStops.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookedTickets: StateFlow<List<BookedTicketEntity>> = repository.allBookedTickets.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val alertLogs: StateFlow<List<DelayAlertLogEntity>> = repository.allAlertLogs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val offlineZones: StateFlow<List<OfflineZoneSyncEntity>> = repository.allOfflineZones.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val filteredTrains: StateFlow<List<TrainEntity>> = combine(
        allTrains,
        allStops,
        _searchQuery,
        _selectedZoneFilter
    ) { trains, stops, query, zoneFilter ->
        trains.filter { train ->
            val matchesZone = when (zoneFilter) {
                "All" -> true
                "East Zone" -> train.zone == "East Zone"
                "West Zone" -> train.zone == "West Zone"
                "On Time" -> train.delayMinutes == 0
                "Delayed" -> train.delayMinutes > 0
                "Favorites" -> train.isFavorite
                else -> true
            }
            if (!matchesZone) return@filter false
            if (query.isBlank()) return@filter true
            val q = query.trim().lowercase()
            val trainStops = stops.filter { it.trainCode == train.trainCode }
            train.name.lowercase().contains(q) ||
                train.bengaliName.contains(q) ||
                train.trainCode.toString().contains(q) ||
                train.originStation.lowercase().contains(q) ||
                train.destinationStation.lowercase().contains(q) ||
                trainStops.any { it.stationName.lowercase().contains(q) || it.stationCode.lowercase().contains(q) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Trip Planner Computed Direct & Connecting Results
    val plannedTrips: StateFlow<Pair<List<PlannedDirectTrip>, List<PlannedConnectingTrip>>> = combine(
        allTrains,
        allStops,
        _plannerFromStation,
        _plannerToStation,
        _plannerDayOfWeek
    ) { trains, stops, fromStation, toStation, dayOfWeek ->
        if (fromStation == toStation) {
            return@combine Pair(emptyList(), emptyList())
        }
        val stopsByTrain = stops.groupBy { it.trainCode }
        val directTrips = findDirectTrips(trains, stopsByTrain, fromStation, toStation, dayOfWeek)

        // Also compute smart 1-transfer connections via Dhaka (Kamalapur) or Chattogram
        val hubs = listOf("Dhaka (Kamalapur)", "Chattogram", "Santahar", "Bhairab Bazar")
        val connectingTrips = mutableListOf<PlannedConnectingTrip>()
        for (hub in hubs) {
            if (hub == fromStation || hub == toStation) continue
            val firstLegs = findDirectTrips(trains, stopsByTrain, fromStation, hub, dayOfWeek)
            val secondLegs = findDirectTrips(trains, stopsByTrain, hub, toStation, dayOfWeek)
            for (leg1 in firstLegs) {
                for (leg2 in secondLegs) {
                    if (leg1.train.trainCode != leg2.train.trainCode) {
                        val layover = TimeCalculator.calculateDurationText(
                            leg1.estimatedArrival,
                            leg2.estimatedDeparture
                        )
                        connectingTrips.add(
                            PlannedConnectingTrip(
                                firstLeg = leg1,
                                secondLeg = leg2,
                                transferStation = hub,
                                layoverText = layover,
                                totalFareSChair = leg1.fareSChair + leg2.fareSChair,
                                totalDistanceKm = leg1.segmentDistanceKm + leg2.segmentDistanceKm
                            )
                        )
                    }
                }
            }
        }
        Pair(directTrips, connectingTrips.take(4))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Pair(emptyList(), emptyList())
    )

    init {
        RailNotificationHelper.createNotificationChannel(appContext)
        viewModelScope.launch {
            repository.ensureDatabaseSeeded()
        }
    }

    private fun findDirectTrips(
        trains: List<TrainEntity>,
        stopsByTrain: Map<Int, List<TrainStopEntity>>,
        fromStation: String,
        toStation: String,
        dayOfWeek: String
    ): List<PlannedDirectTrip> {
        val result = mutableListOf<PlannedDirectTrip>()
        for (train in trains) {
            val trainStops = stopsByTrain[train.trainCode] ?: continue
            val fromStop = trainStops.firstOrNull {
                it.stationName.equals(fromStation, ignoreCase = true)
            } ?: continue
            val toStop = trainStops.firstOrNull {
                it.stationName.equals(toStation, ignoreCase = true)
            } ?: continue

            if (fromStop.stopOrder < toStop.stopOrder) {
                val depScheduled = if (fromStop.scheduledDeparture == "Origin") train.departureTime else fromStop.scheduledDeparture
                val arrScheduled = if (toStop.scheduledArrival == "Terminus") train.arrivalTime else toStop.scheduledArrival
                val estDep = TimeCalculator.addMinutesToTime(depScheduled, train.delayMinutes)
                val estArr = TimeCalculator.addMinutesToTime(arrScheduled, train.delayMinutes)
                val distKm = max(15, toStop.distanceFromOriginKm - fromStop.distanceFromOriginKm)
                val ratio = distKm.toFloat() / max(1, train.totalDistanceKm).toFloat()

                result.add(
                    PlannedDirectTrip(
                        train = train,
                        fromStop = fromStop,
                        toStop = toStop,
                        estimatedDeparture = estDep,
                        estimatedArrival = estArr,
                        segmentDistanceKm = distKm,
                        durationText = TimeCalculator.calculateDurationText(depScheduled, arrScheduled),
                        fareSChair = max(85, (train.baseFareSChair * ratio).roundToInt()),
                        fareSnigdha = max(160, (train.baseFareSnigdha * ratio).roundToInt()),
                        fareAcBerth = max(290, (train.baseFareAcBerth * ratio).roundToInt()),
                        isOffDayOnSelectedDay = train.offDay.equals(dayOfWeek, ignoreCase = true)
                    )
                )
            }
        }
        return result.sortedBy { it.train.delayMinutes }
    }

    fun getStopsWithEstimates(train: TrainEntity, stops: List<TrainStopEntity>): List<StopWithEstimate> {
        val trainStops = stops.filter { it.trainCode == train.trainCode }.sortedBy { it.stopOrder }
        if (trainStops.isEmpty()) return emptyList()
        val currentDist = train.totalDistanceKm * train.currentProgressFraction

        var foundNext = false
        return trainStops.map { stop ->
            val passed = stop.distanceFromOriginKm <= currentDist && stop.stopOrder < trainStops.size
            val isNext = !passed && !foundNext
            if (isNext) foundNext = true

            val rawArr = if (stop.scheduledArrival == "Origin") train.departureTime else stop.scheduledArrival
            val rawDep = if (stop.scheduledDeparture == "Terminus") train.arrivalTime else stop.scheduledDeparture
            StopWithEstimate(
                stop = stop,
                estimatedArrival = if (stop.scheduledArrival == "Origin") "Origin" else TimeCalculator.addMinutesToTime(rawArr, train.delayMinutes),
                estimatedDeparture = if (stop.scheduledDeparture == "Terminus") "Terminus" else TimeCalculator.addMinutesToTime(rawDep, train.delayMinutes),
                isPassed = passed,
                isCurrentOrNext = isNext
            )
        }
    }

    fun toggleDarkMode() {
        val next = !_isDarkMode.value
        _isDarkMode.value = next
        prefs.edit().putBoolean("night_rail_dark_mode", next).apply()
    }

    fun toggleOfflineMode() {
        val next = !_isOfflineModeActive.value
        _isOfflineModeActive.value = next
        prefs.edit().putBoolean("offline_mode_active", next).apply()
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun openTrainDetails(trainCode: Int?) {
        _selectedTrainCode.value = trainCode
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateZoneFilter(filter: String) {
        _selectedZoneFilter.value = filter
    }

    fun updatePlannerStations(from: String, to: String) {
        _plannerFromStation.value = from
        _plannerToStation.value = to
    }

    fun swapPlannerStations() {
        val temp = _plannerFromStation.value
        _plannerFromStation.value = _plannerToStation.value
        _plannerToStation.value = temp
    }

    fun updatePlannerDay(day: String) {
        _plannerDayOfWeek.value = day
    }

    fun startBookingForTrain(trainCode: Int, seatClass: SeatClassOption = SeatClassOption.SNIGDHA) {
        _bookingTrainCode.value = trainCode
        _bookingSeatClass.value = seatClass
        _bookingCoach.value = "KA"
        _selectedSeats.value = setOf("KA-7W")
        _selectedTrainCode.value = null
        _currentTab.value = AppTab.SEAT_BOOKING
    }

    fun selectBookingTrain(trainCode: Int) {
        _bookingTrainCode.value = trainCode
        _selectedSeats.value = emptySet()
    }

    fun selectBookingClass(seatClass: SeatClassOption) {
        _bookingSeatClass.value = seatClass
        _selectedSeats.value = emptySet()
    }

    fun selectBookingCoach(coach: String) {
        _bookingCoach.value = coach
        _selectedSeats.value = emptySet()
    }

    fun updateBookingDate(date: String) {
        _bookingDate.value = date
    }

    fun toggleSeatSelection(seatId: String) {
        val current = _selectedSeats.value.toMutableSet()
        if (current.contains(seatId)) {
            current.remove(seatId)
        } else if (current.size < 4) { // Bangladesh Railway max 4 seats per ticket policy
            current.add(seatId)
        }
        _selectedSeats.value = current
    }

    fun clearBookingSuccessMessage() {
        _bookingSuccessMessage.value = null
    }

    fun confirmSeatBooking(passengerName: String, passengerPhone: String) {
        val train = allTrains.value.firstOrNull { it.trainCode == _bookingTrainCode.value } ?: return
        val seats = _selectedSeats.value.sorted()
        if (seats.isEmpty()) return

        val unitFare = when (_bookingSeatClass.value) {
            SeatClassOption.S_CHAIR -> train.baseFareSChair
            SeatClassOption.SNIGDHA -> train.baseFareSnigdha
            SeatClassOption.AC_B -> train.baseFareAcBerth
        }
        val serviceChargePerSeat = 20
        val totalFare = seats.size * (unitFare + serviceChargePerSeat)
        val pnr = "BR-2026-${(10000..99999).random()}"

        val originStop = allStops.value.firstOrNull {
            it.trainCode == train.trainCode && it.stopOrder == 1
        }
        val platform = originStop?.platformNumber ?: "Platform 1"

        val ticket = BookedTicketEntity(
            pnrNumber = pnr,
            trainCode = train.trainCode,
            trainName = train.name,
            fromStation = train.originStation,
            toStation = train.destinationStation,
            journeyDate = _bookingDate.value,
            departureTime = TimeCalculator.addMinutesToTime(train.departureTime, train.delayMinutes),
            arrivalTime = TimeCalculator.addMinutesToTime(train.arrivalTime, train.delayMinutes),
            platformNumber = platform,
            seatClass = _bookingSeatClass.value.displayName,
            coachName = _bookingCoach.value,
            seatNumbers = seats.joinToString(", "),
            passengerName = passengerName.ifBlank { "BR Traveler" },
            passengerPhone = passengerPhone.ifBlank { "+880 1700-000000" },
            totalFareBdt = totalFare,
            status = "CONFIRMED"
        )

        viewModelScope.launch {
            repository.bookSeatTicket(ticket)
            _selectedSeats.value = emptySet()
            _bookingSuccessMessage.value = "Ticket Confirmed! PNR: $pnr (${ticket.seatNumbers})"
        }
    }

    fun cancelBookedTicket(ticketId: Int) {
        viewModelScope.launch {
            repository.cancelSeatTicket(ticketId)
        }
    }

    fun toggleFavorite(train: TrainEntity) {
        viewModelScope.launch {
            repository.toggleTrainFavorite(train.trainCode, !train.isFavorite)
        }
    }

    fun toggleAlertSubscription(train: TrainEntity) {
        viewModelScope.launch {
            val next = !train.isSubscribedAlert
            repository.toggleTrainAlertSubscription(train.trainCode, next)
        }
    }

    fun triggerLiveDelayUpdate(
        train: TrainEntity,
        addedDelayMinutes: Int,
        customPlatformNote: String? = null
    ) {
        viewModelScope.launch {
            val newDelay = max(0, train.delayMinutes + addedDelayMinutes)
            val newSpeed = if (newDelay > 20) 62 else 82
            val updatedArrival = TimeCalculator.addMinutesToTime(train.arrivalTime, newDelay)
            val statusNote = if (newDelay == 0) {
                "Cleared signal block • Running On Time (ETA $updatedArrival)"
            } else {
                "Live Control Update: +${newDelay}m delay • Revised ETA $updatedArrival"
            }

            repository.updateLiveTrainTelemetry(
                trainCode = train.trainCode,
                delayMinutes = newDelay,
                statusNote = statusNote,
                speedKmh = newSpeed
            )

            val platformText = customPlatformNote ?: "${train.destinationStation} Platform ${(1..4).random()}"
            val title = if (newDelay == 0) {
                "Schedule Restored • ${train.name} (${train.trainCode})"
            } else {
                "Delay Alert (+${newDelay} min) • ${train.name} (${train.trainCode})"
            }
            val message = if (newDelay == 0) {
                "${train.name} is back on schedule! Expected arrival at ${train.destinationStation}: $updatedArrival."
            } else {
                "${train.name} is delayed by $newDelay minutes due to signal clearance. Updated arrival at ${train.destinationStation}: $updatedArrival."
            }

            repository.logDelayAlert(
                DelayAlertLogEntity(
                    trainCode = train.trainCode,
                    trainName = train.name,
                    alertTitle = title,
                    alertMessage = message,
                    delayMinutes = newDelay,
                    platformInfo = platformText,
                    isRead = false
                )
            )

            RailNotificationHelper.sendDelayPushNotification(
                context = appContext,
                trainCode = train.trainCode,
                trainName = train.name,
                title = title,
                message = message,
                platformInfo = platformText
            )
        }
    }

    fun markAllAlertsRead() {
        viewModelScope.launch {
            repository.markAllAlertsRead()
        }
    }

    fun deleteAlert(alertId: Int) {
        viewModelScope.launch {
            repository.deleteAlertLog(alertId)
        }
    }

    fun refreshOfflineZonePack(zone: OfflineZoneSyncEntity) {
        viewModelScope.launch {
            repository.syncOfflineZone(zone.zoneId, true)
        }
    }

    fun updateTimetableStationFilter(station: String) {
        _timetableStationFilter.value = station
    }

    val allStationNames: List<String> = BangladeshRailSeedData.allStations

    class Factory(
        private val repository: RailRepository,
        private val appContext: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RailViewModel(repository, appContext) as T
        }
    }
}
