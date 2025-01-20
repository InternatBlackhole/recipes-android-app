package si.timkr.mealdb.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import si.timkr.mealdb.R
import si.timkr.mealdb.compose.LoadingError
import si.timkr.mealdb.compose.RecipeStaggeredGrid
import si.timkr.mealdb.compose.StillLoading
import si.timkr.mealdb.models.dto.IngredientDTO
import si.timkr.mealdb.models.dto.RecipeDTO
import si.timkr.mealdb.viewmodels.IngredientsUiState
import si.timkr.mealdb.viewmodels.RecipesUiState
import si.timkr.mealdb.viewmodels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onRecipeClick: (RecipeDTO?) -> Unit = {},
    snackHost: SnackbarHostState? = null,
) {
    val searchViewModel: SearchViewModel = viewModel()

//    val loadingState by searchViewModel.ingredientsLoadingState
    val ingredients by searchViewModel.ingredientsState.collectAsState()
    val recipesState by searchViewModel.recipesState.collectAsState()
    var isRefreshing by remember { mutableStateOf(ingredients is IngredientsUiState.Loading) }
    var lastRefresh by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val pullRefreshState = rememberPullRefreshState(
        isRefreshing,
        onRefresh = {
            if (System.currentTimeMillis() - lastRefresh >= 5000) {
                lastRefresh = System.currentTimeMillis()
                isRefreshing = true
                searchViewModel.pullIngredients()
            }
        })

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .pullRefresh(pullRefreshState),
    ) {
        when (ingredients) {
            is IngredientsUiState.Success -> {
//                if (isRefreshing)
//                    StillLoading(
//                        modifier = Modifier.align(Alignment.Center),
//                        text = stringResource(R.string.loading_ingredients)
//                    )
//                else {
                IngredientsLoaded(
                    onIngredientSelected = searchViewModel::findRecipesByIngredient,
                    ingredients = ingredients as IngredientsUiState.Success,
                    recipes = recipesState,
                    onRecipeClick = onRecipeClick
                )
                isRefreshing = false
//                }

            }

            is IngredientsUiState.Error -> {
                LoadingError(text = "ingredients", modifier = Modifier.align(Alignment.Center))
                isRefreshing = false
            }

            is IngredientsUiState.Loading -> {
                StillLoading(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.loading_ingredients)
                )
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

/*@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = viewModel(),
    onRecipeClick: (RecipeDetailsIM) -> Unit = {},
    snackHost: SnackbarHostState? = null
) {
    val ingLoadingState by searchViewModel.ingredientsLoadingState
    val ingredients by searchViewModel.ingredientsState.collectAsState()
    val recipesState by searchViewModel.recipesState.collectAsState()
    SearchScreen(
        loadingState = ingLoadingState,
        ingredients = ingredients,
        recipesState = recipesState,
        modifier = modifier,
        onRecipeClick = onRecipeClick,
        snackHost = snackHost
    )
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IngredientsLoaded(
    modifier: Modifier = Modifier,
    onIngredientSelected: (IngredientDTO?) -> Unit,
    ingredients: IngredientsUiState.Success,
    onRecipeClick: (RecipeDTO?) -> Unit,
    recipes: RecipesUiState,
) {
    var expandedState by remember { mutableStateOf(false) }
    var selectedIngredient by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        ExposedDropdownMenuBox(
            expanded = expandedState,
            onExpandedChange = {
                expandedState = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            TextField(
                value = selectedIngredient,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.choose_ingredient)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expandedState,
                onDismissRequest = { expandedState = false }
            ) {
                ingredients.ingredients!!.ingredients?.forEach { ingredientDTO ->
                    DropdownMenuItem(
                        text = { Text(text = ingredientDTO.strIngredient ?: "Done f up boi") },
                        onClick = {
                            expandedState = false
                            selectedIngredient = ingredientDTO.strIngredient ?: ""
                            onIngredientSelected(ingredientDTO)
                        },
                        //modifier = Modifier.fillMaxWidth(),
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (recipes) {
            is RecipesUiState.Success -> run {
                if (recipes.recipes?.recipes == null || recipes.recipes.recipes.isEmpty()) {
                    Text(
                        text = stringResource(R.string.there_are_no_recipes_for_chosen_ingredient),
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    )
                    return@run
                }

                RecipeStaggeredGrid(
                    items = recipes.recipes.recipes,
                    key = { item -> item.recipeId ?: "" },
                    onRecipeClick = onRecipeClick
                )
            }

            is RecipesUiState.Error -> {
                LoadingError(text = "recipes")
            }

            RecipesUiState.Loading -> {
                StillLoading(
                    text = stringResource(R.string.recipes_loading),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

/*@Preview
@Composable
private fun SearchScreenPreview(
    @PreviewParameter(SearchPreviewProvider::class) data: SearchPreviewData,
) {
    MiniApp3Theme {
        SearchScreen(
            loadingState = SearchScreenState(false),
            ingredients = data.ingredients,
            recipesState = data.recipes
        )
    }
}*/


private data class SearchPreviewData(
    val ingredients: IngredientsUiState,
    val recipes: RecipesUiState,
)

private object SearchPreviewProvider : PreviewParameterProvider<SearchPreviewData> {
    override val values
        get() = sequenceOf(
            SearchPreviewData(
                IngredientsUiState.Error(Error("error test")),
                RecipesUiState.Error(Error("test"))
            ),
            /*SearchPreviewData(
                IngredientsUiState.Success(
                    IngredientsDTO(
                        listOf(
                            IngredientDTO("id", "test", "description")
                        )
                    )
                ),
                RecipesUiState.Success(null)
            )*/
        )
}
