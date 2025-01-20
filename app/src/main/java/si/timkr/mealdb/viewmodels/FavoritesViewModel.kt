package si.timkr.mealdb.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import si.timkr.mealdb.models.RecipeRepository
import si.timkr.mealdb.models.dto.RecipeDTO

class FavoritesViewModel : ViewModel() {
    private val recipeRepository = RecipeRepository.instance

    private val _favoritesLoadingState: MutableStateFlow<FavoritesLoadingUiState> =
        MutableStateFlow(FavoritesLoadingUiState.Loading)
    val favoritesLoadingUiState = _favoritesLoadingState.asStateFlow()

    private val _favoritesRefresh = MutableSharedFlow<Boolean>(1)
    val favoritesRefresh = _favoritesRefresh.asSharedFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            recipeRepository.favoriteRecipes
                .map { FavoritesLoadingUiState.Success(it?.recipes) as FavoritesLoadingUiState }
                .catch {
                    emit(FavoritesLoadingUiState.Error(it))
                    Log.e(FavoritesViewModel::class.simpleName, "LoadFavorites", it)
                }.collect {
                    _favoritesLoadingState.value = it
                }
        }
    }

    fun refreshFavorites() {
        viewModelScope.launch {
            _favoritesRefresh.emit(true)
        }
    }

}

sealed class FavoritesLoadingUiState {
    data object Loading : FavoritesLoadingUiState()

    data class Success(
        val items: List<RecipeDTO>?
    ) : FavoritesLoadingUiState()

    data class Error(
        val error: Throwable
    ) : FavoritesLoadingUiState()
}