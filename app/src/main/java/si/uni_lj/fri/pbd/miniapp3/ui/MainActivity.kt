package si.uni_lj.fri.pbd.miniapp3.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import si.uni_lj.fri.pbd.miniapp3.ui.home.MainScreen

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun RootView() {
        val navController = rememberNavController()
        AppNavHost(navController = navController)
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            MainScreen(onRecipeClick = {

            })
        }
    }
}
