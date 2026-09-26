package com.example.ui

import com.example.data.local.TrainEntity
import com.example.data.local.TrainStopEntity

enum class AppTab(val route: String) {
    LIVE_TRACK("live_track"),
    TRIP_PLANNER("trip_planner"),
    SEAT_BOOKING("seat_booking"),
    OFFLINE_TIMETABLE("offline_timetable"),
    DELAY_ALERTS("delay_alerts")
}

enum class SeatClassOption(
    val code: String,
    val displayName: String,
    val bengaliSubtitle: String,
    val seatsPerRowLeft: Int,
    val seatsPerRowRight: Int
) {
    S_CHAIR("S_CHAIR", "Shovan Chair", "শোভন চেয়ার (Non-AC 2×3)", 2, 3),
    SNIGDHA("SNIGDHA", "Snigdha (AC Chair)", "স্নিগ্ধা এসি চেয়ার (2×2)", 2, 2),
    AC_B("AC_B", "AC Berth / Cabin", "এসি বার্থ স্লিপার (Berth)", 1, 2)
}

data class StopWithEstimate(
    val stop: TrainStopEntity,
    val estimatedArrival: String,
    val estimatedDeparture: String,
    val isPassed: Boolean,
    val isCurrentOrNext: Boolean
)

data class PlannedDirectTrip(
    val train: TrainEntity,
    val fromStop: TrainStopEntity,
    val toStop: TrainStopEntity,
    val estimatedDeparture: String,
    val estimatedArrival: String,
    val segmentDistanceKm: Int,
    val durationText: String,
    val fareSChair: Int,
    val fareSnigdha: Int,
    val fareAcBerth: Int,
    val isOffDayOnSelectedDay: Boolean
)

data class PlannedConnectingTrip(
    val firstLeg: PlannedDirectTrip,
    val secondLeg: PlannedDirectTrip,
    val transferStation: String,
    val layoverText: String,
    val totalFareSChair: Int,
    val totalDistanceKm: Int
)

object TimeCalculator {
    fun addMinutesToTime(timeStr: String, minutesToAdd: Int): String {
        if (timeStr == "Origin" || timeStr == "Terminus" || !timeStr.contains(":")) {
            return timeStr
        }
        val parts = timeStr.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: return timeStr
        val m = parts.getOrNull(1)?.toIntOrNull() ?: return timeStr
        val totalMinutes = (h * 60 + m + minutesToAdd).mod(24 * 60)
        val newH = totalMinutes / 60
        val newM = totalMinutes % 60
        return "%02d:%02d".format(newH, newM)
    }

    fun calculateDurationText(depTime: String, arrTime: String): String {
        val depMinutes = parseMinutes(depTime) ?: return "5h 15m"
        val arrMinutes = parseMinutes(arrTime) ?: return "5h 15m"
        val diff = if (arrMinutes >= depMinutes) {
            arrMinutes - depMinutes
        } else {
            (24 * 60 - depMinutes) + arrMinutes
        }
        val h = diff / 60
        val m = diff % 60
        return "${h}h ${m}m"
    }

    fun parseMinutes(timeStr: String): Int? {
        if (!timeStr.contains(":")) return null
        val parts = timeStr.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: return null
        val m = parts.getOrNull(1)?.toIntOrNull() ?: return null
        return h * 60 + m
    }
}
