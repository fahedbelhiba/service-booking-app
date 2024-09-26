package com.belhiba.servicebooking.ui

import com.belhiba.servicebooking.data.model.BookingRequest
import com.belhiba.servicebooking.data.model.ServiceListing
import com.belhiba.servicebooking.data.repository.FirebaseBookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class BookingViewModel(
    private val repository: FirebaseBookingRepository = FirebaseBookingRepository()
) {
    private val _servicesState = MutableStateFlow<UiState<List<ServiceListing>>>(UiState.Loading)
    val servicesState: StateFlow<UiState<List<ServiceListing>>> = _servicesState.asStateFlow()

    suspend fun loadServices() {
        _servicesState.value = UiState.Loading
        try {
            repository.services.collect { list ->
                _servicesState.value = UiState.Success(list)
            }
        } catch (e: Exception) {
            _servicesState.value = UiState.Error(e.localizedMessage ?: "Erreur de chargement")
        }
    }

    suspend fun requestBooking(service: ServiceListing, slot: String, notes: String): Result<String> {
        val request = BookingRequest(
            serviceId = service.id,
            userId = "current-user-uid",
            providerId = service.providerId,
            timeSlot = slot,
            totalAmount = service.pricePerHour,
            clientNotes = notes
        )
        return repository.createBooking(request)
    }
}
