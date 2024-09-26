package com.belhiba.servicebooking.data.repository

import com.belhiba.servicebooking.data.model.BookingRequest
import com.belhiba.servicebooking.data.model.BookingStatus
import com.belhiba.servicebooking.data.model.ServiceListing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class FirebaseBookingRepository {

    private val _services = MutableStateFlow<List<ServiceListing>>(
        listOf(
            ServiceListing(
                id = "srv-001",
                title = "Diagnostic & Contrôle Technique Embarqué",
                categoryId = "automotive",
                providerId = "prov-101",
                providerName = "TechMobility Services",
                pricePerHour = 65.0,
                rating = 4.9f,
                reviewCount = 42,
                description = "Bilan complet électronique, contrôle des calculateurs et mise à jour logicielle.",
                availableTimeSlots = listOf("09:00 - 10:30", "11:00 - 12:30", "14:00 - 15:30")
            ),
            ServiceListing(
                id = "srv-002",
                title = "Installation Boîtier Télématique & IoT",
                categoryId = "automotive",
                providerId = "prov-102",
                providerName = "Fahd Connect Labs",
                pricePerHour = 80.0,
                rating = 5.0f,
                reviewCount = 19,
                description = "Intégration capteurs connectés, configuration BLE et validation passerelle.",
                availableTimeSlots = listOf("10:00 - 12:00", "15:00 - 17:00")
            )
        )
    )
    val services: Flow<List<ServiceListing>> = _services.asStateFlow()

    private val _userBookings = MutableStateFlow<List<BookingRequest>>(emptyList())
    val userBookings: Flow<List<BookingRequest>> = _userBookings.asStateFlow()

    suspend fun createBooking(booking: BookingRequest): Result<String> {
        return try {
            val bookingId = if (booking.id.isBlank()) "book-" + UUID.randomUUID().toString().take(8) else booking.id
            val confirmed = booking.copy(id = bookingId, status = BookingStatus.CONFIRMED)
            _userBookings.value = _userBookings.value + confirmed
            Result.success(bookingId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelBooking(bookingId: String): Result<Unit> {
        val updated = _userBookings.value.map {
            if (it.id == bookingId) it.copy(status = BookingStatus.CANCELLED) else it
        }
        _userBookings.value = updated
        return Result.success(Unit)
    }
}
