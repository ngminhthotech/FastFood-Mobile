package com.example.food_app.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Category
import com.example.food_app.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        // Thêm một vài loại mặc định nếu chưa có
        viewModelScope.launch {
            if (!categoryRepository.hasCategories()) {
                categoryRepository.insert(Category(name = "Món chính"))
                categoryRepository.insert(Category(name = "Món phụ"))
                categoryRepository.insert(Category(name = "Tráng miệng"))
                categoryRepository.insert(Category(name = "Nước uống"))
            }
        }
    }

    fun addCategory(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                categoryRepository.insert(Category(name = name))
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.delete(category)
        }
    }
}
