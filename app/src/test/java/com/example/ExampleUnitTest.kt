package com.example

import com.example.data.local.BangladeshRailSeedData
import com.example.ui.TimeCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun timeCalculator_addsDelayMinutesCorrectly() {
    assertEquals("12:30", TimeCalculator.addMinutesToTime("12:15", 15))
    assertEquals("00:10", TimeCalculator.addMinutesToTime("23:50", 20))
    assertEquals("Origin", TimeCalculator.addMinutesToTime("Origin", 15))
  }

  @Test
  fun seedData_containsIntercityTrainsAndStops() {
    val trains = BangladeshRailSeedData.getInitialTrains()
    val stops = BangladeshRailSeedData.getInitialTrainStops()
    assertTrue(trains.isNotEmpty())
    assertTrue(stops.any { it.trainCode == 701 && it.stationName == "Chattogram" })
  }
}
