package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trains")
data class TrainEntity(
    @PrimaryKey val trainCode: Int,
    val name: String,
    val bengaliName: String,
    val originStation: String,
    val destinationStation: String,
    val zone: String, // "East Zone" or "West Zone"
    val offDay: String, // e.g., "Monday", "Tuesday", "None"
    val departureTime: String, // "HH:mm"
    val arrivalTime: String, // "HH:mm"
    val totalDistanceKm: Int,
    val currentSpeedKmh: Int,
    val delayMinutes: Int, // 0 = On Time, >0 = Delayed
    val currentProgressFraction: Float, // 0.0f .. 1.0f along the route
    val currentStatusNote: String,
    val coachesCsv: String,
    val availableClassesCsv: String,
    val baseFareSChair: Int,
    val baseFareSnigdha: Int,
    val baseFareAcBerth: Int,
    val isSubscribedAlert: Boolean = false,
    val isFavorite: Boolean = false
)

@Entity(tableName = "train_stops")
data class TrainStopEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trainCode: Int,
    val stopOrder: Int,
    val stationName: String,
    val stationCode: String,
    val scheduledArrival: String, // "Origin" or "HH:mm"
    val scheduledDeparture: String, // "Terminus" or "HH:mm"
    val haltMinutes: Int,
    val platformNumber: String,
    val distanceFromOriginKm: Int,
    val latitude: Double,
    val longitude: Double
)

@Entity(tableName = "booked_tickets")
data class BookedTicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pnrNumber: String,
    val trainCode: Int,
    val trainName: String,
    val fromStation: String,
    val toStation: String,
    val journeyDate: String,
    val departureTime: String,
    val arrivalTime: String,
    val platformNumber: String,
    val seatClass: String,
    val coachName: String,
    val seatNumbers: String,
    val passengerName: String,
    val passengerPhone: String,
    val totalFareBdt: Int,
    val bookedAtTimestamp: Long = System.currentTimeMillis(),
    val status: String = "CONFIRMED"
)

@Entity(tableName = "delay_alert_logs")
data class DelayAlertLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trainCode: Int,
    val trainName: String,
    val alertTitle: String,
    val alertMessage: String,
    val delayMinutes: Int,
    val platformInfo: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "offline_zones")
data class OfflineZoneSyncEntity(
    @PrimaryKey val zoneId: String,
    val zoneName: String,
    val corridorSummary: String,
    val routesCount: Int,
    val stationsCount: Int,
    val sizeKb: Int,
    val lastSyncedTimestamp: Long,
    val isDownloaded: Boolean = true
)
