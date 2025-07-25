package com.example.recipelist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipelist.data.repository.FavoritesRepository
import com.example.recipelist.data.repository.RecipeRepository

class DetailViewModelFactory(private val repository: RecipeRepository, private val favoritesRepository: FavoritesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository, favoritesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}