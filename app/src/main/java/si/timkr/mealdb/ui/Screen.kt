package si.timkr.mealdb.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Main : Screen("home")

    //if i won't use an activity
    data object RecipeDetail : Screen(
        route = "recipe/{recipeId}",
        navArguments = listOf(navArgument("recipeId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(recipeId: String) = "recipe/$recipeId"
    }
}