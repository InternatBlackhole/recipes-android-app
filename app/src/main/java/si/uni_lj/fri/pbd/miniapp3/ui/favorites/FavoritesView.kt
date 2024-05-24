package si.uni_lj.fri.pbd.miniapp3.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.compose.LoadingError
import si.uni_lj.fri.pbd.miniapp3.compose.RecipeStaggeredGrid
import si.uni_lj.fri.pbd.miniapp3.compose.StillLoading
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDTO
import si.uni_lj.fri.pbd.miniapp3.viewmodels.FavoritesLoadingUiState
import si.uni_lj.fri.pbd.miniapp3.viewmodels.FavoritesViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onRecipeClick: (RecipeDTO?) -> Unit,
    snackHost: SnackbarHostState
) {
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val loadingState by favoritesViewModel.favoritesLoadingUiState.collectAsState()
    //val refresh by favoritesViewModel.favoritesRefresh.collectAsState(initial = true)

    //if (refresh) favoritesViewModel.loadFavorites()

    var isRefreshing by remember { mutableStateOf(loadingState is FavoritesLoadingUiState.Loading) }

    val pullRefreshState = rememberPullRefreshState(
        isRefreshing,
        onRefresh = {
            isRefreshing = true
            favoritesViewModel.loadFavorites()
        })

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .pullRefresh(pullRefreshState)
    ) {
        when (loadingState) {
            is FavoritesLoadingUiState.Loading -> {
                StillLoading(
                    text = stringResource(R.string.your_favorite_recipes_are_loading),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is FavoritesLoadingUiState.Error -> {
                LoadingError(
                    text = stringResource(R.string.failed_to_load_your_favorite_recipes),
                    modifier = Modifier.align(Alignment.Center)
                )
                isRefreshing = false
            }

            is FavoritesLoadingUiState.Success -> {
                FavoritesGrid(
                    state = loadingState as FavoritesLoadingUiState.Success,
                    onRecipeClick = onRecipeClick,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp, bottom = 26.dp)
                )
                isRefreshing = false
                Text(
                    text = stringResource(R.string.pull_to_refresh),
                    color = Color.Yellow,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(4.dp)
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

@Composable
private fun FavoritesGrid(
    modifier: Modifier = Modifier,
    onRecipeClick: (RecipeDTO?) -> Unit,
    state: FavoritesLoadingUiState.Success
) {
    if (state.items.isNullOrEmpty()) {
        Text(
            text = stringResource(R.string.you_have_no_favorite_recipes),
            modifier = modifier,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary
        )
    } else {
        RecipeStaggeredGrid(items = state.items, onRecipeClick = onRecipeClick, modifier = modifier)
    }
}