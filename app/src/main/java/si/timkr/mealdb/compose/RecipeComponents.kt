package si.timkr.mealdb.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import si.timkr.mealdb.R
import si.timkr.mealdb.models.dto.RecipeDTO
import si.timkr.mealdb.ui.theme.MealDbTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecipeCard(name: String, thumbnail: String?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        GlideImage(
            model = thumbnail,
            contentDescription = stringResource(R.string.thumbnail) + " $name",
            loading = placeholder(R.drawable.meal_placeholder),
            failure = placeholder(R.drawable.error_icon),
            transition = CrossFade
            //modifier = Modifier.padding(2.dp)
        )
        //Glide.with(LocalContext.current).load(thumbnail).in
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
fun RecipeStaggeredGrid(
    items: List<RecipeDTO>,
    key: ((RecipeDTO) -> Any)? = null,
    onRecipeClick: (RecipeDTO) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(items, key) {
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

@Composable
@Preview
private fun RecipeCardPreview() {
    MealDbTheme {
        RecipeCard(
            "Ratata  aajhakjab ajkshja posdfja kajsgd sajdhajshdas ",
            "https://www.themealdb.com/images/media/meals/kos9av1699014767.jpg"
        )
    }
}