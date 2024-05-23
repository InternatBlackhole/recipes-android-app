package si.uni_lj.fri.pbd.miniapp3.ui.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.miniapp3.ui.theme.MiniApp3Theme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onRecipeClick: (RecipeDetailsIM) -> Unit = {},
    snackHost: SnackbarHostState? = null,
    loadingState: SearchScreenState,
    ingredients: IngredientsUiState,
    recipesState: RecipesUiState
) {
    Box(
        modifier = modifier
            .fillMaxSize().padding(4.dp)
        //.pullRefresh(pullRefreshState)
    ) {
        if (loadingState.isLoading) {
            IngredientsStillLoading()
        } else {
            when (ingredients) {
                is IngredientsUiState.Success -> {
                    IngredientsLoaded(onIngredientSelected = {}, ingredients = ingredients)
                }

                is IngredientsUiState.Error -> {
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.error_icon),
                            contentDescription = stringResource(R.string.error_occured)
                        )
                        Text(
                            text = "There was an error loading ingredients!",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
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
}

@Composable
private fun BoxScope.IngredientsStillLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.SpaceBetween
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(vertical = 4.dp))
        Text(text = stringResource(R.string.loading_ingredients))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.IngredientsLoaded(
    modifier: Modifier = Modifier,
    onIngredientSelected: (String?) -> Unit,
    ingredients: IngredientsUiState.Success
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
                            onIngredientSelected(ingredientDTO.idIngredient)
                        },
                        //modifier = Modifier.fillMaxWidth(),
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RecipeCard(name: String, thumbnail: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        GlideImage(
            model = thumbnail,
            contentDescription = stringResource(R.string.thumbnail) + " $name",
            loading = placeholder(R.drawable.meal_placeholder),
            //modifier = Modifier.
        )
        Text(text = name, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Preview
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
}

@Composable
@Preview
fun RecipeCardPreview() {
    MiniApp3Theme {
        RecipeCard(
            "Ratata",
            "https://www.themealdb.com/images/media/meals/kos9av1699014767.jpg"
        )
    }
}

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
