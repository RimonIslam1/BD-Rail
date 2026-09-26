package com.example.data.repository

import com.example.data.local.BangladeshRailSeedData
import com.example.data.local.BookedTicketEntity
import com.example.data.local.DelayAlertLogEntity
import com.example.data.local.OfflineZoneSyncEntity
import com.example.data.local.RailDao
import com.example.data.local.TrainEntity
import com.example.data.local.TrainStopEntity
import kotlinx.coroutines.flow.Flow

class RailRepository(private val railDao: RailDao) {

    val allTrains: Flow<List<TrainEntity>> = railDao.getAllTrains()
    val allTrainStops: Flow<List<TrainStopEntity>> = railDao.getAllTrainStops()
    val allBookedTickets: Flow<List<BookedTicketEntity>> = railDao.getAllBookedTickets()
    val allAlertLogs: Flow<List<DelayAlertLogEntity>> = railDao.getAllAlertLogs()
    val allOfflineZones: Flow<List<OfflineZoneSyncEntity>> = railDao.getAllOfflineZones()

    fun getStopsForTrain(trainCode: Int): Flow<List<TrainStopEntity>> =
        railDao.getStopsForTrain(trainCode)

    suspend fun ensureDatabaseSeeded() {
        val currentTrains = railDao.getAllTrainsSnapshot()
        if (currentTrains.isEmpty()) {
            railDao.insertTrains(BangladeshRailSeedData.getInitialTrains())
            railDao.insertTrainStops(BangladeshRailSeedData.getInitialTrainStops())
            railDao.insertOfflineZones(BangladeshRailSeedData.getInitialOfflineZones())
            BangladeshRailSeedData.getInitialAlerts().forEach { alert ->
                railDao.insertAlertLog(alert)
            }
            railDao.insertBookedTicket(BangladeshRailSeedData.getInitialSampleTicket())
        }
    }

    suspend fun toggleTrainFavorite(trainCode: Int, favorite: Boolean) {
        railDao.setTrainFavorite(trainCode, favorite)
    }

    suspend fun toggleTrainAlertSubscription(trainCode: Int, subscribed: Boolean) {
        railDao.setTrainAlertSubscription(trainCode, subscribed)
    }

    suspend fun updateLiveTrainTelemetry(
        trainCode: Int,
        delayMinutes: Int,
        statusNote: String,
        speedKmh: Int
    ) {
        railDao.updateTrainLiveTelemetry(trainCode, delayMinutes, statusNote, speedKmh)
    }

    suspend fun bookSeatTicket(ticket: BookedTicketEntity) {
        railDao.insertBookedTicket(ticket)
    }

    suspend fun cancelSeatTicket(ticketId: Int) {
        railDao.cancelTicket(ticketId)
    }

    suspend fun logDelayAlert(alert: DelayAlertLogEntity) {
        railDao.insertAlertLog(alert)
    }

    suspend fun markAllAlertsRead() {
        railDao.markAllAlertsRead()
    }

    suspend fun deleteAlertLog(alertId: Int) {
        railDao.deleteAlertLog(alertId)
    }

    suspend fun syncOfflineZone(zoneId: String, isDownloaded: Boolean) {
        railDao.updateOfflineZoneSync(zoneId, isDownloaded, System.currentTimeMillis())
    }
}
