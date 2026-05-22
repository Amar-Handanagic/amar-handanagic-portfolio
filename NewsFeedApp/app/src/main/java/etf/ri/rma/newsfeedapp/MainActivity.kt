package etf.ri.rma.newsfeedapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.screen.FilterScreen
import etf.ri.rma.newsfeedapp.screen.NewsDetailsScreen
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.ui.theme.NewsFeedAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsFeedAppTheme {
                Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val newsList = remember{NewsData.getAllNews()}
    var currentCategory by remember { mutableStateOf("Sve") }
    var startDateFilter by remember { mutableStateOf<String?>(null) }
    var endDateFilter by remember { mutableStateOf<String?>(null) }
    val unwantedWordsFilter = remember { mutableStateListOf<String>() }
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            NewsFeedScreen(
                navController = navController,
                news = newsList,
                currentCategory = currentCategory,
                changeCategory = { newCategory -> currentCategory = newCategory },
                startDate = startDateFilter,
                endDate = endDateFilter,
                unwantedWords = unwantedWordsFilter // Pass the reactive list
            )
        }

        composable(
            route = "filters/{category}",
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = "Sve"
                }
            )
        ) { backStackEntry ->
            val category = Uri.decode(backStackEntry.arguments?.getString("category") ?: "Sve")
            FilterScreen(navController, category)
        }


        composable(
            route = "newsFeedFiltered/{category}/{start}/{end}/{unwanted}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("start")    { type = NavType.StringType },
                navArgument("end")      { type = NavType.StringType },
                navArgument("unwanted"){ type = NavType.StringType }
            )
        ) { backStackEntry ->
            val start    = backStackEntry.arguments?.getString("start") ?: ""
            val end      = backStackEntry.arguments?.getString("end") ?: ""
            val unwanted = backStackEntry.arguments?.getString("unwanted") ?: ""
            val unwantedWords = unwanted.split(",").filter { it.isNotBlank() }

            val category = backStackEntry.arguments?.getString("category") ?: "Sve"
            NewsFeedScreen(
                navController = navController,
                news = newsList,
                currentCategory = currentCategory,
                changeCategory = { newCategory -> currentCategory = newCategory },
                startDate = start.takeIf { it.isNotEmpty() },
                endDate = end.takeIf { it.isNotEmpty() },
                unwantedWords = unwantedWords
            )
        }


        composable(
            route = "newsDetails/{newsId}",
            arguments = listOf(navArgument("newsId"){ type = NavType.StringType })
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            NewsDetailsScreen(navController = navController, newsId = newsId)
        }
    }
}