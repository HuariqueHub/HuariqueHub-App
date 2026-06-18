package com.example.huariquehub_mobile.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huariquehub_mobile.data.model.Category
import com.example.huariquehub_mobile.data.model.Huarique
import com.example.huariquehub_mobile.data.remote.toUserMessage
import com.example.huariquehub_mobile.data.repository.HuariqueRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repo = HuariqueRepository()

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var huariques by mutableStateOf<List<Huarique>>(emptyList())
        private set
    var categories by mutableStateOf<List<Category>>(listOf(Category(0, "Todas", "🍽️")))
        private set

    init { load() }

    fun load() {
        viewModelScope.launch {
            isLoading = true
            error = null
            runCatching {
                val huariquesResult = repo.getHuariques()
                val categoriesResult = repo.getCategories()
                huariquesResult to categoriesResult
            }.onSuccess { (h, c) ->
                huariques = h
                categories = listOf(Category(0, "Todas", "🍽️")) + c
            }.onFailure { error = it.toUserMessage() }
            isLoading = false
        }
    }
}
