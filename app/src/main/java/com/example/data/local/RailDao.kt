package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RailDao {

    @Query("SELECT * FROM trains ORDER BY isFavorite DESC, trainCode ASC")
    fun getAllTrains(): Flow<List<TrainEntity>>

    @Query("SELECT * FROM trains WHERE trainCode = :trainCode LIMIT 1")
    fun getTrainByCode(trainCode: Int): Flow<TrainEntity?>

    @Query("SELECT * FROM trains ORDER BY trainCode ASC")
    suspend fun getAllTrainsSnapshot(): List<TrainEntity>

    @Query("SELECT * FROM train_stops WHERE trainCode = :trainCode ORDER BY stopOrder ASC")
    fun getStopsForTrain(trainCode: Int): Flow<List<TrainStopEntity>>

    @Query("SELECT * FROM train_stops ORDER BY trainCode ASC, stopOrder ASC")
    fun getAllTrainStops(): Flow<List<TrainStopEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrains(trains: List<TrainEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainStops(stops: List<TrainStopEntity>)

    @Update
    suspend fun updateTrain(train: TrainEntity)

    @Query("UPDATE trains SET delayMinutes = :delayMinutes, currentStatusNote = :statusNote, currentSpeedKmh = :speedKmh WHERE trainCode = :trainCode")
    suspend fun updateTrainLiveTelemetry(trainCode: Int, delayMinutes: Int, statusNote: String, speedKmh: Int)

    @Query("UPDATE trains SET isSubscribedAlert = :subscribed WHERE trainCode = :trainCode")
    suspend fun setTrainAlertSubscription(trainCode: Int, subscribed: Boolean)

    @Query("UPDATE trains SET isFavorite = :favorite WHERE trainCode = :trainCode")
    suspend fun setTrainFavorite(trainCode: Int, favorite: Boolean)

    // Booked Tickets
    @Query("SELECT * FROM booked_tickets ORDER BY bookedAtTimestamp DESC")
    fun getAllBookedTickets(): Flow<List<BookedTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookedTicket(ticket: BookedTicketEntity)

    @Query("UPDATE booked_tickets SET status = 'CANCELLED' WHERE id = :ticketId")
    suspend fun cancelTicket(ticketId: Int)

    @Query("DELETE FROM booked_tickets WHERE id = :ticketId")
    suspend fun deleteTicket(ticketId: Int)

    // Delay & Schedule Alert Logs
    @Query("SELECT * FROM delay_alert_logs ORDER BY timestamp DESC")
    fun getAllAlertLogs(): Flow<List<DelayAlertLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlertLog(alert: DelayAlertLogEntity)

    @Query("UPDATE delay_alert_logs SET isRead = 1")
    suspend fun markAllAlertsRead()

    @Query("DELETE FROM delay_alert_logs WHERE id = :alertId")
    suspend fun deleteAlertLog(alertId: Int)

    // Offline Timetable Sync Packs
    @Query("SELECT * FROM offline_zones ORDER BY zoneName ASC")
    fun getAllOfflineZones(): Flow<List<OfflineZoneSyncEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflineZones(zones: List<OfflineZoneSyncEntity>)

    @Query("UPDATE offline_zones SET isDownloaded = :downloaded, lastSyncedTimestamp = :timestamp WHERE zoneId = :zoneId")
    suspend fun updateOfflineZoneSync(zoneId: String, downloaded: Boolean, timestamp: Long)
}
