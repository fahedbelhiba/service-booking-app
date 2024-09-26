package com.belhiba.servicebooking.data.model

import java.util.Date

enum class BookingStatus {
    PENDING, CONFIRMED, COMPLETED, CANCELLED
}

data class ServiceCategory(
    val id: String = "",
    val name: String = "",
    val iconUrl: String = "",
    val description: String = ""
)

data class ServiceListing(
    val id: String = "",
    val title: String = "",
    val categoryId: String = "",
    val providerId: String = "",
    val providerName: String = "",
    val pricePerHour: Double = 0.0,
    val rating: Float = 4.8f,
    val reviewCount: Int = 24,
    val description: String = "",
    val availableTimeSlots: List<String> = emptyList()
)

data class BookingRequest(
    val id: String = "",
    val serviceId: String = "",
    val userId: String = "",
    val providerId: String = "",
    val scheduledDate: Date = Date(),
    val timeSlot: String = "",
    val totalAmount: Double = 0.0,
    val status: BookingStatus = BookingStatus.PENDING,
    val clientNotes: String = ""
)

data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val avatarUrl: String = "",
    val isServiceProvider: Boolean = false
)
