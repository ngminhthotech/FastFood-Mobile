package com.example.food_app.ui.menu

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.FoodItem
import com.example.food_app.repository.FoodRepository
import com.example.food_app.ui.navigation.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

enum class SortOrder {
    BY_NAME,
    BY_PRICE_ASC,
    BY_PRICE_DESC
}

@HiltViewModel
class FoodMenuViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _sortOrder = MutableStateFlow(SortOrder.BY_NAME)
    val sortOrder = _sortOrder.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null) // null = Tất cả
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val menuItems: StateFlow<List<FoodItem>> = 
        combine(_sortOrder, _searchQuery, _selectedCategoryId) { order, query, categoryId ->
            Triple(order, query, categoryId)
        }.flatMapLatest { (order, query, categoryId) ->
            when (order) {
                SortOrder.BY_NAME -> foodRepository.getItemsSortedByName(query, categoryId)
                SortOrder.BY_PRICE_ASC -> foodRepository.getItemsSortedByPriceAsc(query, categoryId)
                SortOrder.BY_PRICE_DESC -> foodRepository.getItemsSortedByPriceDesc(query, categoryId)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun changeSortOrder(newOrder: SortOrder) {
        _sortOrder.value = newOrder
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentEditItem: StateFlow<FoodItem?> =
        savedStateHandle.getStateFlow(AppDestinations.ITEM_ID_ARG, -1)
            .flatMapLatest { itemId ->
                if (itemId != -1) {
                    foodRepository.getItemById(itemId)
                } else {
                    flowOf(null)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = null
            )

    fun addNewItem(name: String, description: String, price: Double, imageUri: Uri?, categoryId: Int) {
        viewModelScope.launch {
            val imagePath = imageUri?.let { saveImageToInternalStorage(it) }
            if (imagePath != null) {
                val newItem = FoodItem(
                    name = name,
                    description = description,
                    price = price,
                    imagePath = imagePath,
                    categoryId = categoryId
                )
                foodRepository.insert(newItem)
            }
        }
    }

    fun updateItem(item: FoodItem, newImageUri: Uri?, newCategoryId: Int) {
        viewModelScope.launch {
            val imagePath = newImageUri?.let { saveImageToInternalStorage(it) } ?: item.imagePath
            foodRepository.update(item.copy(imagePath = imagePath, categoryId = newCategoryId))
        }
    }

    fun deleteItem(item: FoodItem) {
        viewModelScope.launch {
            foodRepository.delete(item)
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
