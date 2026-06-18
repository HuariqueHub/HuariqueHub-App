package com.example.huariquehub_mobile.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huariquehub_mobile.data.model.Huarique
import com.example.huariquehub_mobile.data.model.Review
import com.example.huariquehub_mobile.data.remote.SessionManager
import com.example.huariquehub_mobile.data.remote.toUserMessage
import com.example.huariquehub_mobile.data.repository.HuariqueRepository
import kotlinx.coroutines.launch

class HuariqueDetailViewModel : ViewModel() {

    private val repo = HuariqueRepository()

    var huarique by mutableStateOf<Huarique?>(null)
        private set
    var reviews by mutableStateOf<List<Review>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var isSubmitting by mutableStateOf(false)
        private set

    fun load(id: Int) {
        viewModelScope.launch {
            isLoading = true
            error = null
            runCatching {
                val h = repo.getHuarique(id)
                val r = runCatching { repo.getReviews(id) }.getOrDefault(emptyList())
                h to r
            }.onSuccess { (h, r) ->
                huarique = h
                reviews = r
            }.onFailure { error = it.toUserMessage() }
            isLoading = false
        }
    }

    fun submitReview(huariqueId: Int, rating: Int, comment: String, onDone: () -> Unit) {
        val userId = SessionManager.userId ?: return
        viewModelScope.launch {
            isSubmitting = true
            runCatching { repo.addReview(huariqueId, userId, rating, comment) }
                .onSuccess {
                    reviews = runCatching { repo.getReviews(huariqueId) }.getOrDefault(reviews + it)
                    onDone()
                }
                .onFailure { error = it.toUserMessage() }
            isSubmitting = false
        }
    }
}
