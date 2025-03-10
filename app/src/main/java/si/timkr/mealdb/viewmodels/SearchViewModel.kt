package si.timkr.mealdb.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import si.timkr.mealdb.models.RecipeRepository
import si.timkr.mealdb.models.dto.IngredientDTO
import si.timkr.mealdb.models.dto.IngredientsDTO
import si.timkr.mealdb.models.dto.RecipesDTO

class SearchViewModel : ViewModel() {
    private val recipeRepository = RecipeRepository.instance

    private val _recipesState =
        MutableStateFlow<RecipesUiState>(RecipesUiState.Success(null))
    val recipesState = _recipesState.asStateFlow()

    private val _ingredientsState =
        MutableStateFlow<IngredientsUiState>(IngredientsUiState.Success(null))
    val ingredientsState = _ingredientsState.asStateFlow()

    init {
        pullIngredients()
    }

    fun pullIngredients() {
        viewModelScope.launch {
            //load all ingredients
            _ingredientsState.value = IngredientsUiState.Loading
            recipeRepository.allIngredients
                //.flowOn(Dispatchers.IO)
                .map { IngredientsUiState.Success(it) as IngredientsUiState }
                .catch {
                    emit(IngredientsUiState.Error(it))
                    Log.e(SearchViewModel::class.simpleName, "PullIngredients", it)
                }
                .collect {
//                    _state.value = SearchScreenState(false)
                    _ingredientsState.value = it
                }
        }
    }

    fun findRecipesByIngredient(ingredient: IngredientDTO?) {
        ingredient?.strIngredient ?: return
        viewModelScope.launch {
            _recipesState.value = RecipesUiState.Loading
            recipeRepository.getRecipesByIngredient(ingredient.strIngredient)
                .map { RecipesUiState.Success(it) as RecipesUiState }
                .catch {
                    emit(RecipesUiState.Error(it))
                    Log.e(SearchViewModel::class.simpleName, "findRecipesByIngredient", it)
                }
                .collect {
                    _recipesState.value = it
                }
        }
    }
}

/*data class SearchScreenState(
    val isLoading: Boolean = true,
)*/

sealed class RecipesUiState {
    data class Success(
        val recipes: RecipesDTO?
    ) : RecipesUiState()

    data class Error(
        val exception: Throwable
    ) : RecipesUiState()

    data object Loading : RecipesUiState()
}

sealed class IngredientsUiState {
    data class Success(
        val ingredients: IngredientsDTO?
    ) : IngredientsUiState()

    data class Error(
        val exception: Throwable
    ) : IngredientsUiState()

    data object Loading : IngredientsUiState()
}



