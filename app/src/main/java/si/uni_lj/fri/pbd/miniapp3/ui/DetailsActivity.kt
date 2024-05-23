package si.uni_lj.fri.pbd.miniapp3.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import si.uni_lj.fri.pbd.miniapp3.ui.home.MainAppBar
import si.uni_lj.fri.pbd.miniapp3.ui.theme.MiniApp3Theme

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val recipeId = intent.getStringExtra(RECIPE_ID_KEY)
        setContent {
            MiniApp3Theme {
                DetailsScreen(recipeId = recipeId, modifier = Modifier.fillMaxSize())
            }
        }
    }

    companion object {
        const val RECIPE_ID_KEY = "RECIPE_ID_KEY"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreen(recipeId: String?, modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackHost = remember { SnackbarHostState() }

    Scaffold(
        topBar = { MainAppBar(scrollBehavior = scrollBehavior) },
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackHost)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "RecipeId:")
            Text(text = recipeId ?: "null")

        }
    }
}