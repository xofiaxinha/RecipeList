package com.example.recipelist.data.repository

import com.example.recipelist.data.local.RecipeDao
import com.example.recipelist.data.local.RecipeEntity
import com.example.recipelist.data.model.Recipe
import com.example.recipelist.data.remote.RecipeApiService
import com.example.recipelist.data.util.toDomainModel
import com.example.recipelist.data.util.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstRecipeRepository(
    private val recipeDao: RecipeDao,
    private val apiService: RecipeApiService
) : RecipeRepository {

    override fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun refreshRecipes() {
        try {
            val remoteRecipes = apiService.getAllRecipes()
            recipeDao.insertAll(remoteRecipes.map { it.toEntity() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun getRecipeById(id: Int): Recipe? {

        return recipeDao.getRecipeById(id)?.toDomainModel()
    }

    override suspend fun getRecipeByName(name: String): Recipe? {

        return null
    }

    override suspend fun getFavoriteRecipes(): List<Recipe> {

        return emptyList()
    }
}