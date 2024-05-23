package si.uni_lj.fri.pbd.miniapp3.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.miniapp3.ui.MainScreenViewModel

@Preview(showBackground = true)
@Composable
fun FavoritesScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(),
    onRecipeClick: (RecipeDetailsIM) -> Unit,
    snackHost: SnackbarHostState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "hello")
    }
}