package si.timkr.mealdb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import si.timkr.mealdb.models.RecipeRepository
import si.timkr.mealdb.models.dto.RecipeDTO
import java.io.File

class DetailsViewModel(
    private val cacheLocation: File
) : ViewModel() {
    private val recipeRepository = RecipeRepository.instance

    private val _loadingState = MutableStateFlow<RecipeLoadingUiState>(RecipeLoadingUiState.Loading)
    val recipeLoadingState = _loadingState.asStateFlow()

    private val _favoriteState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.InProgress)
    val favoriteUiState = _favoriteState.asStateFlow()

    fun getFullRecipe(recipeId: String) {
        viewModelScope.launch {
            recipeRepository.getFullRecipe(recipeId)
                .map { RecipeLoadingUiState.Success(it) as RecipeLoadingUiState }
                .catch {
                    emit(RecipeLoadingUiState.Error(it))
                }
                .collect {
                    _loadingState.value = it
                }
        }
    }

    fun markFavorite(recipeDTO: RecipeDTO, isFavorite: Boolean) {
        val recipe = recipeDTO.copy(
            recipeThumbnailLocal = File(
                cacheLocation,
                recipeDTO.recipeId
            ).absolutePath
        )
        viewModelScope.launch {
            recipeRepository.markRecipeFavorite(recipe, isFavorite).collect {
                _favoriteState.value =
                    if (it == null) FavoriteUiState.Success
                    else FavoriteUiState.Error(it)
            }
        }
    }
}

sealed interface FavoriteUiState {
    data object InProgress : FavoriteUiState
    data object Success : FavoriteUiState
    data class Error(
        val error: Throwable
    ) : FavoriteUiState
}

sealed interface RecipeLoadingUiState {
    data object Loading : RecipeLoadingUiState
    data class Success(
        val recipeDTO: RecipeDTO?
    ) : RecipeLoadingUiState

    data class Error(
        val error: Throwable
    ) : RecipeLoadingUiState
}