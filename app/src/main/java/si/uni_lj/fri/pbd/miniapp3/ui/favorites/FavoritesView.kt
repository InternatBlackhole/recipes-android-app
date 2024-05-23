package si.uni_lj.fri.pbd.miniapp3.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDTO

@Preview(showBackground = true)
@Composable
fun FavoritesScreen(
    onRecipeClick: (RecipeDTO?) -> Unit,
    snackHost: SnackbarHostState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Text(text = "hello")
    }
}