package si.uni_lj.fri.pbd.miniapp3.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDTO
import si.uni_lj.fri.pbd.miniapp3.ui.theme.MiniApp3Theme
import si.uni_lj.fri.pbd.miniapp3.viewmodels.IngredientsUiState
import si.uni_lj.fri.pbd.miniapp3.viewmodels.RecipesUiState
import si.uni_lj.fri.pbd.miniapp3.viewmodels.SearchViewModel

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

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        //.pullRefresh(pullRefreshState)
    ) {
//        if (loadingState.isLoading) {
//            StillLoading(
//                modifier = Modifier.align(Alignment.Center),
//                text = stringResource(R.string.loading_ingredients)
//            )
//        } else {
        when (ingredients) {
            is IngredientsUiState.Success -> {
                IngredientsLoaded(
                    onIngredientSelected = searchViewModel::findRecipesByIngredient,
                    ingredients = ingredients as IngredientsUiState.Success,
                    recipes = recipesState,
                    onRecipeClick = onRecipeClick
                )
            }

            is IngredientsUiState.Error -> {
                LoadingError(text = "ingredients", modifier = Modifier.align(Alignment.Center))
            }

            is IngredientsUiState.Loading -> {
                StillLoading(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.loading_ingredients)
                )
            }
        }
//        }
    }
}

@Composable
private fun LoadingError(modifier: Modifier = Modifier, text: String) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.error_icon),
            contentDescription = stringResource(R.string.error_occured)
        )
        Text(
            text = "There was an error loading $text!",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
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

@Composable
private fun StillLoading(modifier: Modifier = Modifier, text: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(vertical = 4.dp))
        Text(text = text)
    }
}

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
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    )
                    return@run
                }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalItemSpacing = 8.dp
                ) {
                    items(recipes.recipes.recipes, { item -> item.recipeId ?: "" }) {
                        RecipeCard(
                            name = it.recipeName ?: stringResource(R.string.no_name_given),
                            thumbnail = it.recipeThumbnail,
                            modifier = Modifier.clickable {
                                onRecipeClick(it)
                            }
                        )
                    }
                }
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RecipeCard(name: String, thumbnail: String?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        GlideImage(
            model = thumbnail,
            contentDescription = stringResource(R.string.thumbnail) + " $name",
            loading = placeholder(R.drawable.meal_placeholder),
            failure = placeholder(R.drawable.error_icon),
            //modifier = Modifier.padding(2.dp)
        )
        Text(
            text = name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(4.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview
fun RecipeCardPreview() {
    MiniApp3Theme {
        RecipeCard(
            "Ratata  aajhakjab ajkshja posdfja kajsgd sajdhajshdas ",
            "https://www.themealdb.com/images/media/meals/kos9av1699014767.jpg"
        )
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
