package si.uni_lj.fri.pbd.miniapp3.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.miniapp3.R
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.miniapp3.ui.favorites.FavoritesScreen
import si.uni_lj.fri.pbd.miniapp3.ui.search.SearchScreen
import si.uni_lj.fri.pbd.miniapp3.ui.theme.MiniApp3Theme

enum class Pages(
    @StringRes val title: Int,
) {
    SEARCH(R.string.search),
    FAVORITES(R.string.favorites)
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
)
@Composable
fun MainScreen(
    onRecipeClick: (RecipeDetailsIM) -> Unit = {},
    modifier: Modifier = Modifier,
    pages: Array<Pages> = Pages.entries.toTypedArray()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MainAppBar(pagerState = pagerState, scrollBehavior = scrollBehavior)
        }, snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        MainPager(
            modifier = Modifier.padding(paddingValues),
            pagerState = pagerState,
            pages = pages,
            snackbarHostState = snackbarHostState,
            onRecipeClick = {
                onRecipeClick(it)
                scope.launch {
                    snackbarHostState.showSnackbar(it.strMeal ?: "unknown")
                }
            }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreenPreview() {
    MiniApp3Theme {
        MainScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    pages: Array<Pages>,
    onRecipeClick: (RecipeDetailsIM) -> Unit = {},
    snackbarHostState: SnackbarHostState
) {
    Column(modifier) {
        val scope = rememberCoroutineScope()
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            divider = {
                Spacer(modifier = Modifier.height(5.dp))
            },
            /*indicator = {
                TabRowDefaults.Indicator(
                    modifier =
                    Modifier.tabIndicatorOffset(it[pagerState.currentPage]),
                    height = 2.dp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }*/
        ) {
            pages.forEachIndexed { index, s ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(text = stringResource(id = s.title))
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.Top
            //pageNestedScrollConnection = scrollBehavior.nestedScrollConnection
        ) {
            when (pages[it]) {
                Pages.SEARCH -> SearchScreen(
                    onRecipeClick = onRecipeClick,
                    snackHost = snackbarHostState
                )

                Pages.FAVORITES -> FavoritesScreen(
                    onRecipeClick = onRecipeClick,
                    snackHost = snackbarHostState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainAppBar(
    pagerState: PagerState,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = Modifier
            //.wrapContentHeight()
            .fillMaxWidth(),
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = scrollBehavior
    )
}