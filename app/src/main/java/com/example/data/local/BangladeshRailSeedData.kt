package com.example.data.local

object BangladeshRailSeedData {

    val allStations = listOf(
        "Dhaka (Kamalapur)",
        "Dhaka Bimanbandar",
        "Joydebpur",
        "Tangail",
        "Ishwardi Bypass",
        "Natore",
        "Rajshahi",
        "Santahar",
        "Bogura",
        "Rangpur",
        "Panchagarh",
        "Bhairab Bazar",
        "Brahmanbaria",
        "Cumilla",
        "Feni",
        "Chattogram",
        "Cox's Bazar",
        "Shaistaganj",
        "Srimangal",
        "Kulaura",
        "Sylhet",
        "Bhanga Junction",
        "Jashore",
        "Khulna",
        "Mymensingh"
    )

    fun getInitialTrains(): List<TrainEntity> = listOf(
        TrainEntity(
            trainCode = 701,
            name = "Suborno Express",
            bengaliName = "সুবর্ণ এক্সপ্রেস",
            originStation = "Chattogram",
            destinationStation = "Dhaka (Kamalapur)",
            zone = "East Zone",
            offDay = "Monday",
            departureTime = "07:00",
            arrivalTime = "12:15",
            totalDistanceKm = 321,
            currentSpeedKmh = 84,
            delayMinutes = 0,
            currentProgressFraction = 0.68f,
            currentStatusNote = "Cruising past Bhairab Meghna Bridge • On schedule",
            coachesCsv = "KA (Snigdha),KHA (Snigdha),GA (Snigdha),GHA (AC Chair),UMA (S_Chair),CHA (S_Chair),CHHA (S_Chair),JA (Dining)",
            availableClassesCsv = "SNIGDHA,S_CHAIR",
            baseFareSChair = 405,
            baseFareSnigdha = 777,
            baseFareAcBerth = 1150,
            isSubscribedAlert = true,
            isFavorite = true
        ),
        TrainEntity(
            trainCode = 787,
            name = "Sonar Bangla Express",
            bengaliName = "সোনার বাংলা এক্সপ্রেস",
            originStation = "Dhaka (Kamalapur)",
            destinationStation = "Chattogram",
            zone = "East Zone",
            offDay = "Wednesday",
            departureTime = "07:00",
            arrivalTime = "12:15",
            totalDistanceKm = 321,
            currentSpeedKmh = 79,
            delayMinutes = 8,
            currentProgressFraction = 0.54f,
            currentStatusNote = "Passing Cumilla Outer Signal • Minor signal caution (+8m)",
            coachesCsv = "KA (AC Berth),KHA (Snigdha),GA (Snigdha),GHA (Snigdha),UMA (S_Chair),CHA (S_Chair),CHHA (Dining)",
            availableClassesCsv = "AC_B,SNIGDHA,S_CHAIR",
            baseFareSChair = 405,
            baseFareSnigdha = 777,
            baseFareAcBerth = 1245,
            isSubscribedAlert = true,
            isFavorite = true
        ),
        TrainEntity(
            trainCode = 814,
            name = "Cox's Bazar Express",
            bengaliName = "কক্সবাজার এক্সপ্রেস",
            originStation = "Dhaka (Kamalapur)",
            destinationStation = "Cox's Bazar",
            zone = "East Zone",
            offDay = "Monday",
            departureTime = "22:30",
            arrivalTime = "06:40",
            totalDistanceKm = 472,
            currentSpeedKmh = 86,
            delayMinutes = 0,
            currentProgressFraction = 0.76f,
            currentStatusNote = "Departed Chattogram Junction • En route to Cox's Bazar Iconic Station",
            coachesCsv = "KA (AC Berth),KHA (AC Berth),GA (Snigdha),GHA (Snigdha),UMA (S_Chair),CHA (S_Chair),CHHA (S_Chair),JA (Dining)",
            availableClassesCsv = "AC_B,SNIGDHA,S_CHAIR",
            baseFareSChair = 695,
            baseFareSnigdha = 1325,
            baseFareAcBerth = 2380,
            isSubscribedAlert = true,
            isFavorite = true
        ),
        TrainEntity(
            trainCode = 759,
            name = "Padma Express",
            bengaliName = "পদ্মা এক্সপ্রেস",
            originStation = "Dhaka (Kamalapur)",
            destinationStation = "Rajshahi",
            zone = "West Zone",
            offDay = "Tuesday",
            departureTime = "22:45",
            arrivalTime = "04:25",
            totalDistanceKm = 343,
            currentSpeedKmh = 72,
            delayMinutes = 18,
            currentProgressFraction = 0.61f,
            currentStatusNote = "Crossing Jamuna Multipurpose Bridge • Single-line crossing wait (+18m)",
            coachesCsv = "KA (AC Berth),KHA (Snigdha),GA (Snigdha),GHA (S_Chair),UMA (S_Chair),CHA (S_Chair),CHHA (Dining)",
            availableClassesCsv = "AC_B,SNIGDHA,S_CHAIR",
            baseFareSChair = 405,
            baseFareSnigdha = 777,
            baseFareAcBerth = 1168,
            isSubscribedAlert = true,
            isFavorite = false
        ),
        TrainEntity(
            trainCode = 709,
            name = "Parabat Express",
            bengaliName = "পারাবত এক্সপ্রেস",
            originStation = "Dhaka (Kamalapur)",
            destinationStation = "Sylhet",
            zone = "East Zone",
            offDay = "Tuesday",
            departureTime = "06:30",
            arrivalTime = "13:00",
            totalDistanceKm = 319,
            currentSpeedKmh = 68,
            delayMinutes = 14,
            currentProgressFraction = 0.72f,
            currentStatusNote = "Approaching Srimangal Tea Valley • Platform 1 assigned (+14m)",
            coachesCsv = "KA (AC Berth),KHA (Snigdha),GA (Snigdha),GHA (S_Chair),UMA (S_Chair),CHA (S_Chair),CHHA (Shovon)",
            availableClassesCsv = "AC_B,SNIGDHA,S_CHAIR,SHOVON",
            baseFareSChair = 375,
            baseFareSnigdha = 719,
            baseFareAcBerth = 1098,
            isSubscribedAlert = false,
            isFavorite = false
        ),
        TrainEntity(
            trainCode = 726,
            name = "Sundarban Express",
            bengaliName = "সুন্দরবন এক্সপ্রেস",
            originStation = "Dhaka (Kamalapur)",
            destinationStation = "Khulna",
            zone = "West Zone",
            offDay = "Wednesday",
            departureTime = "08:00",
            arrivalTime = "15:40",
            totalDistanceKm = 376,
            currentSpeedKmh = 88,
            delayMinutes = 0,
            currentProgressFraction = 0.42f,
            currentStatusNote = "Cruising on Padma Bridge Rail Link towards Bhanga Junction",
            coachesCsv = "KA (AC Berth),KHA (Snigdha),GA (Snigdha),GHA (S_Chair),UMA (S_Chair),CHA (S_Chair)",
            availableClassesCsv = "AC_B,SNIGDHA,S_CHAIR",
            baseFareSChair = 500,
            baseFareSnigdha = 955,
            baseFareAcBerth = 1435,
            isSubscribedAlert = false,
            isFavorite = false
        ),
        TrainEntity(
            trainCode = 771,
            name = "Rangpur Express",
            bengaliName = "রংপুর এক্সপ্রেস",
            originStation = "Dhaka (Kamalapur)",
            destinationStation = "Rangpur",
            zone = "West Zone",
            offDay = "Monday",
            departureTime = "09:10",
            arrivalTime = "19:00",
            totalDistanceKm = 468,
            currentSpeedKmh = 64,
            delayMinutes = 25,
            currentProgressFraction = 0.58f,
            currentStatusNote = "Departed Santahar Junction • Track maintenance speed restriction (+25m)",
            coachesCsv = "KA (AC Berth),KHA (Snigdha),GA (S_Chair),GHA (S_Chair),UMA (S_Chair),CHA (Dining)",
            availableClassesCsv = "AC_B,SNIGDHA,S_CHAIR",
            baseFareSChair = 585,
            baseFareSnigdha = 1122,
            baseFareAcBerth = 1680,
            isSubscribedAlert = true,
            isFavorite = false
        ),
        TrainEntity(
            trainCode = 793,
            name = "Panchagarh Express",
            bengaliName = "পঞ্চগড় এক্সপ্রেস",
            originStation = "Dhaka (Kamalapur)",
            destinationStation = "Panchagarh",
            zone = "West Zone",
            offDay = "None",
            departureTime = "23:30",
            arrivalTime = "09:50",
            totalDistanceKm = 593,
            currentSpeedKmh = 76,
            delayMinutes = 5,
            currentProgressFraction = 0.65f,
            currentStatusNote = "Crossing Natore Bypass • Smooth overnight run (+5m)",
            coachesCsv = "KA (AC Berth),KHA (Snigdha),GA (Snigdha),GHA (S_Chair),UMA (S_Chair),CHA (S_Chair)",
            availableClassesCsv = "AC_B,SNIGDHA,S_CHAIR",
            baseFareSChair = 695,
            baseFareSnigdha = 1334,
            baseFareAcBerth = 1995,
            isSubscribedAlert = false,
            isFavorite = false
        )
    )

    fun getInitialTrainStops(): List<TrainStopEntity> = listOf(
        // 701 Suborno Express (Chattogram -> Dhaka)
        TrainStopEntity(trainCode = 701, stopOrder = 1, stationName = "Chattogram", stationCode = "CTG", scheduledArrival = "Origin", scheduledDeparture = "07:00", haltMinutes = 0, platformNumber = "Platform 1", distanceFromOriginKm = 0, latitude = 22.3396, longitude = 91.8315),
        TrainStopEntity(trainCode = 701, stopOrder = 2, stationName = "Feni", stationCode = "FNI", scheduledArrival = "08:12", scheduledDeparture = "08:12", haltMinutes = 0, platformNumber = "Through Track 1", distanceFromOriginKm = 91, latitude = 23.0186, longitude = 91.4021),
        TrainStopEntity(trainCode = 701, stopOrder = 3, stationName = "Cumilla", stationCode = "CML", scheduledArrival = "09:00", scheduledDeparture = "09:00", haltMinutes = 0, platformNumber = "Through Track 2", distanceFromOriginKm = 148, latitude = 23.4539, longitude = 91.1818),
        TrainStopEntity(trainCode = 701, stopOrder = 4, stationName = "Bhairab Bazar", stationCode = "BHAB", scheduledArrival = "10:25", scheduledDeparture = "10:25", haltMinutes = 0, platformNumber = "Main Line 1", distanceFromOriginKm = 236, latitude = 24.0533, longitude = 90.9859),
        TrainStopEntity(trainCode = 701, stopOrder = 5, stationName = "Dhaka Bimanbandar", stationCode = "BMBD", scheduledArrival = "11:35", scheduledDeparture = "11:40", haltMinutes = 5, platformNumber = "Platform 2", distanceFromOriginKm = 302, latitude = 23.8519, longitude = 90.4086),
        TrainStopEntity(trainCode = 701, stopOrder = 6, stationName = "Dhaka (Kamalapur)", stationCode = "DAKA", scheduledArrival = "12:15", scheduledDeparture = "Terminus", haltMinutes = 0, platformNumber = "Platform 3", distanceFromOriginKm = 321, latitude = 23.7316, longitude = 90.4261),

        // 787 Sonar Bangla Express (Dhaka -> Chattogram)
        TrainStopEntity(trainCode = 787, stopOrder = 1, stationName = "Dhaka (Kamalapur)", stationCode = "DAKA", scheduledArrival = "Origin", scheduledDeparture = "07:00", haltMinutes = 0, platformNumber = "Platform 2", distanceFromOriginKm = 0, latitude = 23.7316, longitude = 90.4261),
        TrainStopEntity(trainCode = 787, stopOrder = 2, stationName = "Dhaka Bimanbandar", stationCode = "BMBD", scheduledArrival = "07:22", scheduledDeparture = "07:27", haltMinutes = 5, platformNumber = "Platform 1", distanceFromOriginKm = 19, latitude = 23.8519, longitude = 90.4086),
        TrainStopEntity(trainCode = 787, stopOrder = 3, stationName = "Brahmanbaria", stationCode = "BMBA", scheduledArrival = "09:05", scheduledDeparture = "09:05", haltMinutes = 0, platformNumber = "Through Track 1", distanceFromOriginKm = 112, latitude = 23.9686, longitude = 91.1112),
        TrainStopEntity(trainCode = 787, stopOrder = 4, stationName = "Cumilla", stationCode = "CML", scheduledArrival = "10:02", scheduledDeparture = "10:02", haltMinutes = 0, platformNumber = "Through Track 1", distanceFromOriginKm = 173, latitude = 23.4539, longitude = 91.1818),
        TrainStopEntity(trainCode = 787, stopOrder = 5, stationName = "Chattogram", stationCode = "CTG", scheduledArrival = "12:15", scheduledDeparture = "Terminus", haltMinutes = 0, platformNumber = "Platform 1", distanceFromOriginKm = 321, latitude = 22.3396, longitude = 91.8315),

        // 814 Cox's Bazar Express (Dhaka -> Cox's Bazar)
        TrainStopEntity(trainCode = 814, stopOrder = 1, stationName = "Dhaka (Kamalapur)", stationCode = "DAKA", scheduledArrival = "Origin", scheduledDeparture = "22:30", haltMinutes = 0, platformNumber = "Platform 1", distanceFromOriginKm = 0, latitude = 23.7316, longitude = 90.4261),
        TrainStopEntity(trainCode = 814, stopOrder = 2, stationName = "Dhaka Bimanbandar", stationCode = "BMBD", scheduledArrival = "22:53", scheduledDeparture = "22:58", haltMinutes = 5, platformNumber = "Platform 1", distanceFromOriginKm = 19, latitude = 23.8519, longitude = 90.4086),
        TrainStopEntity(trainCode = 814, stopOrder = 3, stationName = "Chattogram", stationCode = "CTG", scheduledArrival = "03:40", scheduledDeparture = "04:00", haltMinutes = 20, platformNumber = "Platform 4", distanceFromOriginKm = 321, latitude = 22.3396, longitude = 91.8315),
        TrainStopEntity(trainCode = 814, stopOrder = 4, stationName = "Cox's Bazar", stationCode = "CXB", scheduledArrival = "06:40", scheduledDeparture = "Terminus", haltMinutes = 0, platformNumber = "Iconic Platform 1", distanceFromOriginKm = 472, latitude = 21.4395, longitude = 92.0077),

        // 759 Padma Express (Dhaka -> Rajshahi)
        TrainStopEntity(trainCode = 759, stopOrder = 1, stationName = "Dhaka (Kamalapur)", stationCode = "DAKA", scheduledArrival = "Origin", scheduledDeparture = "22:45", haltMinutes = 0, platformNumber = "Platform 5", distanceFromOriginKm = 0, latitude = 23.7316, longitude = 90.4261),
        TrainStopEntity(trainCode = 759, stopOrder = 2, stationName = "Dhaka Bimanbandar", stationCode = "BMBD", scheduledArrival = "23:08", scheduledDeparture = "23:13", haltMinutes = 5, platformNumber = "Platform 2", distanceFromOriginKm = 19, latitude = 23.8519, longitude = 90.4086),
        TrainStopEntity(trainCode = 759, stopOrder = 3, stationName = "Joydebpur", stationCode = "JDP", scheduledArrival = "23:38", scheduledDeparture = "23:41", haltMinutes = 3, platformNumber = "Platform 1", distanceFromOriginKm = 34, latitude = 23.9999, longitude = 90.4203),
        TrainStopEntity(trainCode = 759, stopOrder = 4, stationName = "Tangail", stationCode = "TGL", scheduledArrival = "00:48", scheduledDeparture = "00:50", haltMinutes = 2, platformNumber = "Platform 1", distanceFromOriginKm = 98, latitude = 24.2513, longitude = 89.9167),
        TrainStopEntity(trainCode = 759, stopOrder = 5, stationName = "Ishwardi Bypass", stationCode = "ISDB", scheduledArrival = "02:45", scheduledDeparture = "02:48", haltMinutes = 3, platformNumber = "Platform 1", distanceFromOriginKm = 248, latitude = 24.1481, longitude = 89.0762),
        TrainStopEntity(trainCode = 759, stopOrder = 6, stationName = "Rajshahi", stationCode = "RJS", scheduledArrival = "04:25", scheduledDeparture = "Terminus", haltMinutes = 0, platformNumber = "Platform 2", distanceFromOriginKm = 343, latitude = 24.3745, longitude = 88.6042),

        // 709 Parabat Express (Dhaka -> Sylhet)
        TrainStopEntity(trainCode = 709, stopOrder = 1, stationName = "Dhaka (Kamalapur)", stationCode = "DAKA", scheduledArrival = "Origin", scheduledDeparture = "06:30", haltMinutes = 0, platformNumber = "Platform 4", distanceFromOriginKm = 0, latitude = 23.7316, longitude = 90.4261),
        TrainStopEntity(trainCode = 709, stopOrder = 2, stationName = "Dhaka Bimanbandar", stationCode = "BMBD", scheduledArrival = "06:53", scheduledDeparture = "06:58", haltMinutes = 5, platformNumber = "Platform 1", distanceFromOriginKm = 19, latitude = 23.8519, longitude = 90.4086),
        TrainStopEntity(trainCode = 709, stopOrder = 3, stationName = "Bhairab Bazar", stationCode = "BHAB", scheduledArrival = "08:08", scheduledDeparture = "08:11", haltMinutes = 3, platformNumber = "Platform 2", distanceFromOriginKm = 85, latitude = 24.0533, longitude = 90.9859),
        TrainStopEntity(trainCode = 709, stopOrder = 4, stationName = "Brahmanbaria", stationCode = "BMBA", scheduledArrival = "08:31", scheduledDeparture = "08:35", haltMinutes = 4, platformNumber = "Platform 1", distanceFromOriginKm = 104, latitude = 23.9686, longitude = 91.1112),
        TrainStopEntity(trainCode = 709, stopOrder = 5, stationName = "Shaistaganj", stationCode = "SHG", scheduledArrival = "09:50", scheduledDeparture = "09:53", haltMinutes = 3, platformNumber = "Platform 1", distanceFromOriginKm = 179, latitude = 24.2736, longitude = 91.4542),
        TrainStopEntity(trainCode = 709, stopOrder = 6, stationName = "Srimangal", stationCode = "SRM", scheduledArrival = "10:36", scheduledDeparture = "10:41", haltMinutes = 5, platformNumber = "Platform 1", distanceFromOriginKm = 219, latitude = 24.3065, longitude = 91.7296),
        TrainStopEntity(trainCode = 709, stopOrder = 7, stationName = "Kulaura", stationCode = "KUA", scheduledArrival = "11:28", scheduledDeparture = "11:32", haltMinutes = 4, platformNumber = "Platform 2", distanceFromOriginKm = 262, latitude = 24.5197, longitude = 92.0335),
        TrainStopEntity(trainCode = 709, stopOrder = 8, stationName = "Sylhet", stationCode = "SYT", scheduledArrival = "13:00", scheduledDeparture = "Terminus", haltMinutes = 0, platformNumber = "Platform 1", distanceFromOriginKm = 319, latitude = 24.8825, longitude = 91.8693),

        // 726 Sundarban Express (Dhaka -> Khulna via Padma Bridge)
        TrainStopEntity(trainCode = 726, stopOrder = 1, stationName = "Dhaka (Kamalapur)", stationCode = "DAKA", scheduledArrival = "Origin", scheduledDeparture = "08:00", haltMinutes = 0, platformNumber = "Platform 6", distanceFromOriginKm = 0, latitude = 23.7316, longitude = 90.4261),
        TrainStopEntity(trainCode = 726, stopOrder = 2, stationName = "Bhanga Junction", stationCode = "BHG", scheduledArrival = "09:10", scheduledDeparture = "09:13", haltMinutes = 3, platformNumber = "Platform 1", distanceFromOriginKm = 78, latitude = 23.3889, longitude = 89.9850),
        TrainStopEntity(trainCode = 726, stopOrder = 3, stationName = "Jashore", stationCode = "JSR", scheduledArrival = "14:15", scheduledDeparture = "14:20", haltMinutes = 5, platformNumber = "Platform 2", distanceFromOriginKm = 319, latitude = 23.1634, longitude = 89.2182),
        TrainStopEntity(trainCode = 726, stopOrder = 4, stationName = "Khulna", stationCode = "KLN", scheduledArrival = "15:40", scheduledDeparture = "Terminus", haltMinutes = 0, platformNumber = "Platform 1", distanceFromOriginKm = 376, latitude = 22.8156, longitude = 89.5632),

        // 771 Rangpur Express (Dhaka -> Rangpur)
        TrainStopEntity(trainCode = 771, stopOrder = 1, stationName = "Dhaka (Kamalapur)", stationCode = "DAKA", scheduledArrival = "Origin", scheduledDeparture = "09:10", haltMinutes = 0, platformNumber = "Platform 7", distanceFromOriginKm = 0, latitude = 23.7316, longitude = 90.4261),
        TrainStopEntity(trainCode = 771, stopOrder = 2, stationName = "Dhaka Bimanbandar", stationCode = "BMBD", scheduledArrival = "09:33", scheduledDeparture = "09:38", haltMinutes = 5, platformNumber = "Platform 2", distanceFromOriginKm = 19, latitude = 23.8519, longitude = 90.4086),
        TrainStopEntity(trainCode = 771, stopOrder = 3, stationName = "Natore", stationCode = "NTR", scheduledArrival = "13:55", scheduledDeparture = "13:58", haltMinutes = 3, platformNumber = "Platform 1", distanceFromOriginKm = 264, latitude = 24.4139, longitude = 88.9869),
        TrainStopEntity(trainCode = 771, stopOrder = 4, stationName = "Santahar", stationCode = "STH", scheduledArrival = "14:55", scheduledDeparture = "15:00", haltMinutes = 5, platformNumber = "Platform 2", distanceFromOriginKm = 307, latitude = 24.8021, longitude = 88.9936),
        TrainStopEntity(trainCode = 771, stopOrder = 5, stationName = "Bogura", stationCode = "BGR", scheduledArrival = "15:45", scheduledDeparture = "15:50", haltMinutes = 5, platformNumber = "Platform 1", distanceFromOriginKm = 349, latitude = 24.8481, longitude = 89.3730),
        TrainStopEntity(trainCode = 771, stopOrder = 6, stationName = "Rangpur", stationCode = "RGP", scheduledArrival = "19:00", scheduledDeparture = "Terminus", haltMinutes = 0, platformNumber = "Platform 1", distanceFromOriginKm = 468, latitude = 25.7361, longitude = 89.2519),

        // 793 Panchagarh Express (Dhaka -> Panchagarh)
        TrainStopEntity(trainCode = 793, stopOrder = 1, stationName = "Dhaka (Kamalapur)", stationCode = "DAKA", scheduledArrival = "Origin", scheduledDeparture = "23:30", haltMinutes = 0, platformNumber = "Platform 3", distanceFromOriginKm = 0, latitude = 23.7316, longitude = 90.4261),
        TrainStopEntity(trainCode = 793, stopOrder = 2, stationName = "Dhaka Bimanbandar", stationCode = "BMBD", scheduledArrival = "23:53", scheduledDeparture = "23:58", haltMinutes = 5, platformNumber = "Platform 2", distanceFromOriginKm = 19, latitude = 23.8519, longitude = 90.4086),
        TrainStopEntity(trainCode = 793, stopOrder = 3, stationName = "Natore", stationCode = "NTR", scheduledArrival = "03:45", scheduledDeparture = "03:48", haltMinutes = 3, platformNumber = "Platform 1", distanceFromOriginKm = 264, latitude = 24.4139, longitude = 88.9869),
        TrainStopEntity(trainCode = 793, stopOrder = 4, stationName = "Santahar", stationCode = "STH", scheduledArrival = "04:40", scheduledDeparture = "04:45", haltMinutes = 5, platformNumber = "Platform 1", distanceFromOriginKm = 307, latitude = 24.8021, longitude = 88.9936),
        TrainStopEntity(trainCode = 793, stopOrder = 5, stationName = "Panchagarh", stationCode = "PCG", scheduledArrival = "09:50", scheduledDeparture = "Terminus", haltMinutes = 0, platformNumber = "Platform 1", distanceFromOriginKm = 593, latitude = 26.3354, longitude = 88.5517)
    )

    fun getInitialOfflineZones(): List<OfflineZoneSyncEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            OfflineZoneSyncEntity(
                zoneId = "EAST_ZONE",
                zoneName = "BR East Zone (Dhaka • Chattogram • Sylhet)",
                corridorSummary = "Kamalapur, Bimanbandar, Bhairab, Cumilla, Feni, Chattogram, Srimangal, Sylhet",
                routesCount = 24,
                stationsCount = 86,
                sizeKb = 1420,
                lastSyncedTimestamp = now - 3600_000L,
                isDownloaded = true
            ),
            OfflineZoneSyncEntity(
                zoneId = "WEST_ZONE",
                zoneName = "BR West Zone (Rajshahi • Rangpur • Panchagarh)",
                corridorSummary = "Joydebpur, Tangail, Jamuna Bridge, Ishwardi, Natore, Santahar, Bogura, Rangpur",
                routesCount = 21,
                stationsCount = 78,
                sizeKb = 1280,
                lastSyncedTimestamp = now - 7200_000L,
                isDownloaded = true
            ),
            OfflineZoneSyncEntity(
                zoneId = "PADMA_LINK",
                zoneName = "Padma Bridge Corridor (Dhaka • Bhanga • Khulna)",
                corridorSummary = "Mawa, Padma Bridge Viaduct, Bhanga Junction, Jashore, Khulna",
                routesCount = 8,
                stationsCount = 22,
                sizeKb = 540,
                lastSyncedTimestamp = now - 1800_000L,
                isDownloaded = true
            ),
            OfflineZoneSyncEntity(
                zoneId = "COXS_BAZAR_LINK",
                zoneName = "Dohazari – Cox's Bazar Iconic Coastal Line",
                corridorSummary = "Chattogram, Dohazari, Chakaria, Ramu, Cox's Bazar Iconic Terminal",
                routesCount = 6,
                stationsCount = 11,
                sizeKb = 390,
                lastSyncedTimestamp = now - 900_000L,
                isDownloaded = true
            )
        )
    }

    fun getInitialAlerts(): List<DelayAlertLogEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            DelayAlertLogEntity(
                trainCode = 771,
                trainName = "Rangpur Express",
                alertTitle = "Delay Alert • +25 min on Rangpur Express (771)",
                alertMessage = "Track maintenance caution between Santahar and Bogura. Estimated arrival at Rangpur revised to 19:25.",
                delayMinutes = 25,
                platformInfo = "Bogura Platform 1 • Rangpur Platform 1",
                timestamp = now - 15 * 60_000L,
                isRead = false
            ),
            DelayAlertLogEntity(
                trainCode = 759,
                trainName = "Padma Express",
                alertTitle = "Bridge Crossing Wait • +18 min on Padma Express (759)",
                alertMessage = "Single-line crossing clearance at Jamuna Multipurpose Bridge East Bank. Updated Rajshahi ETA: 04:43.",
                delayMinutes = 18,
                platformInfo = "Ishwardi Bypass Platform 1 • Rajshahi Platform 2",
                timestamp = now - 42 * 60_000L,
                isRead = false
            ),
            DelayAlertLogEntity(
                trainCode = 701,
                trainName = "Suborno Express",
                alertTitle = "Platform Assigned • Suborno Express (701) On Time",
                alertMessage = "Suborno Express is running on schedule at 84 km/h. Assigned Platform 2 at Dhaka Bimanbandar and Platform 3 at Kamalapur.",
                delayMinutes = 0,
                platformInfo = "Bimanbandar Platform 2 → Kamalapur Platform 3",
                timestamp = now - 90 * 60_000L,
                isRead = true
            )
        )
    }

    fun getInitialSampleTicket(): BookedTicketEntity = BookedTicketEntity(
        pnrNumber = "BR-2026-70189",
        trainCode = 701,
        trainName = "Suborno Express",
        fromStation = "Chattogram",
        toStation = "Dhaka (Kamalapur)",
        journeyDate = "27 Sep 2026",
        departureTime = "07:00",
        arrivalTime = "12:15",
        platformNumber = "Platform 1",
        seatClass = "SNIGDHA",
        coachName = "KA",
        seatNumbers = "KA-14W, KA-15A",
        passengerName = "Rimon Islam",
        passengerPhone = "+880 1711-000000",
        totalFareBdt = 1594,
        status = "CONFIRMED"
    )
}
