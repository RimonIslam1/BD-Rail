package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.local.BookedTicketEntity
import com.example.data.local.TrainEntity
import com.example.ui.SeatClassOption
import com.example.ui.theme.SeatAvailableColor
import com.example.ui.theme.SeatBookedColor
import com.example.ui.theme.SeatSelectedColor
import com.example.ui.theme.SignalDelayCrimson
import com.example.ui.theme.SignalOnTimeGreen

@Composable
fun SeatBookingScreen(
    trains: List<TrainEntity>,
    selectedTrainCode: Int,
    selectedSeatClass: SeatClassOption,
    selectedCoach: String,
    selectedDate: String,
    selectedSeats: Set<String>,
    bookedTickets: List<BookedTicketEntity>,
    bookingSuccessMessage: String?,
    onSelectTrain: (Int) -> Unit,
    onSelectSeatClass: (SeatClassOption) -> Unit,
    onSelectCoach: (String) -> Unit,
    onSelectDate: (String) -> Unit,
    onToggleSeat: (String) -> Unit,
    onConfirmBooking: (String, String) -> Unit,
    onCancelTicket: (Int) -> Unit,
    onDismissSuccess: () -> Unit
) {
    val activeTrain = trains.firstOrNull { it.trainCode == selectedTrainCode } ?: trains.firstOrNull()
    var passengerName by remember { mutableStateOf("Rimon Islam") }
    var passengerPhone by remember { mutableStateOf("+880 1711-248900") }
    val coaches = listOf("KA", "KHA", "GA", "GHA", "UMA", "CHA")
    val dates = listOf("26 Sep 2026", "27 Sep 2026", "28 Sep 2026", "29 Sep 2026", "30 Sep 2026")

    val unitFare = if (activeTrain != null) {
        when (selectedSeatClass) {
            SeatClassOption.S_CHAIR -> activeTrain.baseFareSChair
            SeatClassOption.SNIGDHA -> activeTrain.baseFareSnigdha
            SeatClassOption.AC_B -> activeTrain.baseFareAcBerth
        }
    } else 405

    val serviceCharge = 20
    val totalFare = selectedSeats.size * (unitFare + serviceCharge)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("seat_booking_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (bookingSuccessMessage != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SignalOnTimeGreen.copy(alpha = 0.18f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SignalOnTimeGreen
                            )
                            Text(
                                text = bookingSuccessMessage,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        OutlinedButton(onClick = onDismissSuccess) {
                            Text("OK")
                        }
                    }
                }
            }
        }

        // Train, Date & Class Selection Card
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
                    Text(
                        text = "1. Select Intercity Train & Travel Date",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        trains.forEach { train ->
                            FilterChip(
                                selected = train.trainCode == activeTrain?.trainCode,
                                onClick = { onSelectTrain(train.trainCode) },
                                label = { Text("${train.trainCode} ${train.name}") },
                                modifier = Modifier.testTag("book_select_train_${train.trainCode}")
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dates.forEach { date ->
                            FilterChip(
                                selected = selectedDate == date,
                                onClick = { onSelectDate(date) },
                                label = { Text(date) }
                            )
                        }
                    }

                    HorizontalDivider()

                    Text(
                        text = "2. Choose Class & Coach Bogie",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SeatClassOption.entries.forEach { option ->
                            val selected = selectedSeatClass == option
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSelectSeatClass(option) }
                                    .testTag("class_option_${option.code}")
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = option.displayName.substringBefore(" "),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Text(
                                        text = option.code,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        coaches.forEach { coach ->
                            FilterChip(
                                selected = selectedCoach == coach,
                                onClick = { onSelectCoach(coach) },
                                label = { Text("Coach $coach") },
                                modifier = Modifier.testTag("coach_chip_$coach")
                            )
                        }
                    }
                }
            }
        }

        // Interactive Visual Coach Seat Map
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
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
                        Column {
                            Text(
                                text = "Coach $selectedCoach • ${selectedSeatClass.displayName}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "${selectedSeatClass.bengaliSubtitle} • Max 4 seats per PNR",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "৳$unitFare / seat",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SeatLegendDot("Available", SeatAvailableColor)
                        SeatLegendDot("Selected (${selectedSeats.size}/4)", SeatSelectedColor)
                        SeatLegendDot("Booked", SeatBookedColor)
                    }

                    // Locomotive Direction Banner
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "▲ LOCOMOTIVE / ENGINE DIRECTION (${activeTrain?.destinationStation ?: "Terminus"}) ▲",
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    // 6 Rows of Interactive Seats (Left side + Aisle + Right side)
                    val leftCount = selectedSeatClass.seatsPerRowLeft
                    val rightCount = selectedSeatClass.seatsPerRowRight
                    val seatsPerRow = leftCount + rightCount

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (rowIdx in 0 until 6) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Left Block
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    for (c in 0 until leftCount) {
                                        val seatNum = rowIdx * seatsPerRow + c + 1
                                        val posTag = if (c == 0) "W" else "A"
                                        val seatId = "$selectedCoach-$seatNum$posTag"
                                        val isPreBooked = (seatNum % 5 == 0 || seatNum == 3)
                                        SeatButtonCell(
                                            seatId = seatId,
                                            label = "$seatNum$posTag",
                                            isBooked = isPreBooked,
                                            isSelected = selectedSeats.contains(seatId),
                                            onClick = { if (!isPreBooked) onToggleSeat(seatId) }
                                        )
                                    }
                                }

                                // Central Aisle
                                Text(
                                    text = "R${rowIdx + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                // Right Block
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    for (c in 0 until rightCount) {
                                        val seatNum = rowIdx * seatsPerRow + leftCount + c + 1
                                        val posTag = when {
                                            c == rightCount - 1 -> "W"
                                            c == 0 -> "A"
                                            else -> "M"
                                        }
                                        val seatId = "$selectedCoach-$seatNum$posTag"
                                        val isPreBooked = (seatNum % 7 == 0 || seatNum == 9)
                                        SeatButtonCell(
                                            seatId = seatId,
                                            label = "$seatNum$posTag",
                                            isBooked = isPreBooked,
                                            isSelected = selectedSeats.contains(seatId),
                                            onClick = { if (!isPreBooked) onToggleSeat(seatId) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Passenger & Checkout Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "3. Passenger Details & Instant PNR Issue",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = passengerName,
                        onValueChange = { passengerName = it },
                        label = { Text("Passenger Full Name (as on NID)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("passenger_name_input")
                    )

                    OutlinedTextField(
                        value = passengerPhone,
                        onValueChange = { passengerPhone = it },
                        label = { Text("Mobile Number for SMS Ticket") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("passenger_phone_input")
                    )

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
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
                                Text(
                                    text = if (selectedSeats.isEmpty()) "No seats selected" else "Seats: ${selectedSeats.sorted().joinToString(", ")}",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = "Includes ৳20 BR Service Charge / seat",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "৳$totalFare",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Button(
                        onClick = { onConfirmBooking(passengerName, passengerPhone) },
                        enabled = selectedSeats.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("confirm_booking_button")
                    ) {
                        Icon(imageVector = Icons.Default.ConfirmationNumber, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (selectedSeats.isEmpty()) "Select at least 1 Seat on Coach Map"
                            else "Confirm & Save Offline Boarding Pass (৳$totalFare)"
                        )
                    }
                }
            }
        }

        // Saved Offline Tickets & Boarding Passes
        item {
            Text(
                text = "My Offline Boarding Passes (${bookedTickets.size})",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(bookedTickets, key = { it.id }) { ticket ->
            BookedTicketBoardingPassCard(
                ticket = ticket,
                onCancel = { onCancelTicket(ticket.id) }
            )
        }
    }
}

@Composable
private fun SeatLegendDot(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun SeatButtonCell(
    seatId: String,
    label: String,
    isBooked: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isBooked -> SeatBookedColor.copy(alpha = 0.28f)
        isSelected -> SeatSelectedColor
        else -> SeatAvailableColor.copy(alpha = 0.18f)
    }
    val borderColor = when {
        isBooked -> SeatBookedColor.copy(alpha = 0.4f)
        isSelected -> SeatSelectedColor
        else -> SeatAvailableColor
    }
    val textColor = when {
        isBooked -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        isSelected -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .size(width = 50.dp, height = 48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(enabled = !isBooked, onClick = onClick)
            .testTag("seat_cell_$seatId"),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.EventSeat,
                contentDescription = "Seat $label",
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BookedTicketBoardingPassCard(
    ticket: BookedTicketEntity,
    onCancel: () -> Unit
) {
    val isConfirmed = ticket.status == "CONFIRMED"
    val statusColor = if (isConfirmed) SignalOnTimeGreen else SignalDelayCrimson
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ticket_card_${ticket.pnrNumber}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                Column {
                    Text(
                        text = "PNR: ${ticket.pnrNumber}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${ticket.trainName} (#${ticket.trainCode})",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Surface(
                    color = statusColor.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = ticket.status,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${ticket.fromStation} → ${ticket.toStation}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "${ticket.journeyDate} • Dep ${ticket.departureTime} • Arr ${ticket.arrivalTime}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${ticket.platformNumber} • Coach ${ticket.coachName} (${ticket.seatClass})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Seats: ${ticket.seatNumbers} • Paid: ৳${ticket.totalFareBdt}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Passenger: ${ticket.passengerName} (${ticket.passengerPhone})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Stylized Offline Verification Matrix Pattern
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val grid = 5
                        val cellW = size.width / grid
                        val cellH = size.height / grid
                        val seed = ticket.pnrNumber.hashCode()
                        for (r in 0 until grid) {
                            for (c in 0 until grid) {
                                if (((seed shr (r + c)) and 1) == 1 || (r == 0 && c == 0) || (r == 0 && c == grid - 1) || (r == grid - 1 && c == 0)) {
                                    drawRect(
                                        color = onSurfaceColor,
                                        topLeft = Offset(c * cellW, r * cellH),
                                        size = Size(cellW * 0.82f, cellH * 0.82f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isConfirmed) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.testTag("cancel_ticket_${ticket.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cancel Reservation")
                    }
                }
            }
        }
    }
}
