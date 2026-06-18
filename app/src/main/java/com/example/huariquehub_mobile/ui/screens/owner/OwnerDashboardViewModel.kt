package com.example.huariquehub_mobile.ui.screens.owner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huariquehub_mobile.data.model.Huarique
import com.example.huariquehub_mobile.data.remote.toUserMessage
import com.example.huariquehub_mobile.data.repository.HuariqueRepository
import kotlinx.coroutines.launch

class OwnerDashboardViewModel : ViewModel() {

    private val repo = HuariqueRepository()

    var huariques by mutableStateOf<List<Huarique>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun load(ownerId: Int) {
        viewModelScope.launch {
            isLoading = true
            error = null
            runCatching { repo.getHuariquesByOwner(ownerId) }
                .onSuccess { huariques = it }
                .onFailure { error = it.toUserMessage() }
            isLoading = false
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            runCatching { repo.deleteHuarique(id) }
                .onSuccess { huariques = huariques.filter { it.id != id } }
                .onFailure { error = it.toUserMessage() }
        }
    }
}
