package si.uni_lj.fri.pbd.miniapp3.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.compose.LoadingError
import si.uni_lj.fri.pbd.miniapp3.compose.MainAppBar
import si.uni_lj.fri.pbd.miniapp3.compose.StillLoading
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDTO
import si.uni_lj.fri.pbd.miniapp3.ui.theme.MiniApp3Theme
import si.uni_lj.fri.pbd.miniapp3.viewmodels.DetailsViewModel
import si.uni_lj.fri.pbd.miniapp3.viewmodels.FavoriteUiState
import si.uni_lj.fri.pbd.miniapp3.viewmodels.FavoritesViewModel
import si.uni_lj.fri.pbd.miniapp3.viewmodels.RecipeLoadingUiState

class DetailsActivity : AppCompatActivity() {
    private val favoritesViewModel by viewModels<FavoritesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val recipeId =
            intent.getStringExtra(RECIPE_ID_KEY) ?: throw Error("recipeId is null, can't be")
        setContent {
            MiniApp3Theme {
                val detailsViewModel: DetailsViewModel = viewModel {
                    DetailsViewModel(this@DetailsActivity.cacheDir)
                }
                val recipeLoadingState by detailsViewModel.recipeLoadingState.collectAsState()
                val favoriteDone by detailsViewModel.favoriteUiState.collectAsState()
                detailsViewModel.getFullRecipe(recipeId)

                DetailsScreen(
                    modifier = Modifier.fillMaxSize(),
                    recipeLoadingUiState = recipeLoadingState,
                    onRecipeFavoriteChanged = { recipe, favoriteCheck ->
                        recipe.isFavorite = favoriteCheck
                        detailsViewModel.markFavorite(recipe, favoriteCheck)
                    },
                    favoriteState = favoriteDone
                )
            }
        }
    }

    override fun onStop() {
        favoritesViewModel.refreshFavorites()
        super.onStop()
    }

    companion object {
        const val RECIPE_ID_KEY = "RECIPE_ID_KEY"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    recipeLoadingUiState: RecipeLoadingUiState,
    onRecipeFavoriteChanged: (RecipeDTO, Boolean) -> Unit = { _, _ -> },
    favoriteState: FavoriteUiState = FavoriteUiState.InProgress,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    when (favoriteState) {
        is FavoriteUiState.InProgress -> {

        }

        is FavoriteUiState.Error -> {
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.couldn_t_favorite_recipe),
                Toast.LENGTH_SHORT
            ).show()
        }

        is FavoriteUiState.Success -> {
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.recipe_favorited),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            MainAppBar(scrollBehavior = scrollBehavior)
        },
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackHost)
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .fillMaxSize(),
        ) {
            val generalModifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
            when (recipeLoadingUiState) {
                is RecipeLoadingUiState.Loading ->
                    StillLoading(
                        text = stringResource(R.string.recipe_loading),
                        modifier = generalModifier
                    )

                is RecipeLoadingUiState.Error -> LoadingError(
                    text = stringResource(R.string.couldn_t_load_recipe), modifier = generalModifier
                )

                is RecipeLoadingUiState.Success -> {
                    val recipe = recipeLoadingUiState.recipeDTO
                    if (recipe == null)
                        LoadingError(
                            text = stringResource(R.string.this_recipe_doesn_t_exist),
                            modifier = generalModifier
                        )
                    else {
                        RecipeScreen(
                            recipeDTO = recipe,
                            modifier = generalModifier
                                .align(Alignment.TopCenter),
                            onRecipeFavoriteChanged = onRecipeFavoriteChanged
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RecipeScreen(
    modifier: Modifier = Modifier,
    recipeDTO: RecipeDTO,
    onRecipeFavoriteChanged: (RecipeDTO, Boolean) -> Unit
) {
    var isFavorite by remember { mutableStateOf(recipeDTO.isFavorite ?: false) }
    val default = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        GlideImage(
            model = recipeDTO.recipeThumbnail,
            contentDescription = stringResource(id = R.string.thumbnail),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(MaterialTheme.colorScheme.secondary),
            loading = placeholder(R.drawable.meal_placeholder),
            failure = placeholder(R.drawable.error_icon)
        )
        Text(
            text = recipeDTO.recipeName ?: stringResource(id = R.string.no_name_given),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = default
                .padding(top = 8.dp),
        )
        Box(modifier = default) {
            Text(
                text = stringResource(R.string.area) + recipeDTO.strArea,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
            )
            IconToggleButton(
                checked = isFavorite,
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onCheckedChange = {
                    isFavorite = it
                    onRecipeFavoriteChanged(recipeDTO, it)
                }) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite)
                            R.drawable.star_full else R.drawable.star_thin
                    ),
                    contentDescription = stringResource(R.string.favorite_button)
                )
            }
        }
        HorizontalDivider(thickness = 2.dp)
        LabelLazyList(
            list = recipeDTO.ingredients,
            label = stringResource(R.string.ingredients_list),
            modifier = default
        )

        HorizontalDivider(thickness = 2.dp)
        LabelLazyList(
            list = recipeDTO.measurements,
            label = stringResource(R.string.measurements_list),
            modifier = default
        )

        HorizontalDivider(thickness = 3.dp, modifier = Modifier.padding(top = 4.dp, bottom = 4.dp))
        LabelText(
            text = stringResource(R.string.instructions),
            modifier = default.padding(bottom = 6.dp)
        )
        Text(
            text = recipeDTO.strInstructions
                ?: stringResource(R.string.no_instructions_for_this_recipe_were_given),
            modifier = default.padding(bottom = 12.dp)
        )
    }
}

@Composable
private fun LabelText(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        modifier = modifier
    )
}

@Composable
private fun LabelLazyList(modifier: Modifier = Modifier, list: List<String?>, label: String) {
    LabelText(
        text = label,
        modifier = modifier.padding(top = 4.dp),
    )
    LazyRow(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        val list1: List<String> = list.filterNotNull().filter { it.isNotBlank() }
        items(
            list1.subList(0, list1.size - 1),
            //{ i -> i.hashCode() }
        ) {
            Text(text = it)
            VerticalDivider(
                thickness = 2.dp,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 8.dp, end = 8.dp)
                    .height(16.dp)
            )
        }
        item(key = list1.last()) {
            Text(
                text = list1.last()
            )
        }
    }
}

@Preview
@Composable
private fun DetailsPreviewLoading() {
    MiniApp3Theme {
        DetailsScreen(
            recipeLoadingUiState = RecipeLoadingUiState.Loading,
            //modifier = Modifier.fillMaxSize(),
        )

    }
}

@Preview
@Composable
private fun DetailsPreviewError() {
    MiniApp3Theme {
        DetailsScreen(
            recipeLoadingUiState = RecipeLoadingUiState.Error(Error("test")),
//            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun DetailsPreviewSuccess() {
    MiniApp3Theme {
        DetailsScreen(
            recipeLoadingUiState = RecipeLoadingUiState.Success(
                RecipeDTO(
                    "random",
                    strArea = "Japan",
                    recipeName = "Ratatatatat",
                    isFavorite = false,
                    strIngredient1 = "hola",
                    strIngredient2 = "dola",
                    strIngredient3 = "fola",
                    strIngredient4 = "chicken",
                    strIngredient5 = "bacon bacon bacon",
                    strIngredient6 = "kola",
                    strMeasure1 = "12 cups",
                    strMeasure2 = "0 cups",
                    strMeasure3 = "123 cups",
                    strMeasure4 = "whole",
                    strMeasure5 = "3 strips",
                    strMeasure6 = "1 l",
                    strInstructions = "Preheat oven to 350° F. Spray a 9x13-inch baking pan with " +
                            "non-stick spray.\r\nCombine soy sauce, ½ cup water, brown sugar, " +
                            "ginger and garlic in a small saucepan and cover. Bring to a boil " +
                            "over medium heat. Remove lid and cook for one minute once boiling." +
                            "\r\nMeanwhile, stir together the corn starch and 2 tablespoons of " +
                            "water in a separate dish until smooth. Once sauce is boiling, add " +
                            "mixture to the saucepan and stir to combine. Cook until the sauce " +
                            "starts to thicken then remove from heat.\r\nPlace the chicken breasts " +
                            "in the prepared pan. Pour one cup of the sauce over top of chicken. " +
                            "Place chicken in oven and bake 35 minutes or until cooked through. " +
                            "Remove from oven and shred chicken in the dish using two forks.\r\n" +
                            "*Meanwhile, steam or cook the vegetables according to package " +
                            "directions.\r\nAdd the cooked vegetables and rice to the casserole " +
                            "dish with the chicken. Add most of the remaining sauce, reserving a " +
                            "bit to drizzle over the top when serving. Gently toss everything " +
                            "together in the casserole dish until combined. Return to oven and " +
                            "cook 15 minutes. Remove from oven and let stand 5 minutes before " +
                            "serving. Drizzle each serving with remaining sauce. Enjoy!"
                )
            ),
//            modifier = Modifier.fillMaxSize()
        )
    }
}