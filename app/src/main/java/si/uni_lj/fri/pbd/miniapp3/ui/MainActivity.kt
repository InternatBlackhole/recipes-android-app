package si.uni_lj.fri.pbd.miniapp3.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import si.uni_lj.fri.pbd.miniapp3.ui.home.MainScreen
import si.uni_lj.fri.pbd.miniapp3.ui.theme.MiniApp3Theme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniApp3Theme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = modifier
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(onRecipeClick = {
                val intent = Intent(context.applicationContext, DetailsActivity::class.java).apply {
                    putExtra(DetailsActivity.RECIPE_ID_KEY, it?.recipeId)
                }
                context.startActivity(intent)
            })
        }
    }
}
