package etf.ri.rma.newsfeedapp.screen

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterScreen(navController: NavController, selectedCategoryStart: String?) {
    var unwantedInput by remember { mutableStateOf("") }
    var unwantedWords by remember { mutableStateOf(listOf<String>()) }
    var selectedCategory by rememberSaveable { mutableStateOf(selectedCategoryStart ?: "Sve") }

    var startDate by remember { mutableStateOf<String?>(null) }
    var endDate by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Filtriranje vijesti", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Sve", "Politika", "Sport", "Nauka/tehnologija", "Fantazija").forEach { category ->
                val tag = when (category) {
                    "Sve" -> "filter_chip_all"
                    "Politika" -> "filter_chip_pol"
                    "Sport" -> "filter_chip_spo"
                    "Nauka/tehnologija" -> "filter_chip_sci"
                    "Fantazija" -> "filter_chip_fan"
                    else -> ""
                }
                FilterChip(
                    modifier = Modifier
                        .testTag(tag)
                        .widthIn(min = 80.dp),
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = {
                        Text(
                            text = category,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val rangeText = if (startDate != null && endDate != null)
            "$startDate;$endDate"
        else
            "Odaberite opseg datuma"

        var showPicker by remember { mutableStateOf(false) }

        Text(text = rangeText, modifier = Modifier.testTag("filter_daterange_display"))
        Button(
            onClick = { showPicker = true },
            modifier = Modifier.testTag("filter_daterange_button")
        ) { Text("Izaberi opseg datuma") }

        if (showPicker) {
            DateRangePicker(
                onDismissRequest = { showPicker = false },
                onDateRangeSelected = { s, e ->
                    startDate = s
                    endDate = e
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = unwantedInput,
                onValueChange = { unwantedInput = it },
                label = { Text("Nepoželjna riječ") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("filter_unwanted_input")
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    val word = unwantedInput.trim()
                    if (word.isNotEmpty() && unwantedWords.none { it.equals(word, ignoreCase = true) }) {
                        unwantedWords = unwantedWords + word
                    }
                    unwantedInput = ""
                },
                modifier = Modifier.testTag("filter_unwanted_add_button")
            ) {
                Text("Dodaj")
            }
        }

        Button(
            onClick = {
                val encodedCategory = Uri.encode(selectedCategory)
                val start = startDate ?: ""
                val end = endDate ?: ""
                val unwanted = unwantedWords.joinToString(",")

                val route = "newsFeedFiltered/$encodedCategory/$start/$end/$unwanted"
                val options = NavOptions.Builder()
                    .setPopUpTo("filters", inclusive = true)
                    .setLaunchSingleTop(true)
                    .build()

                navController.navigate(route, options)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("filter_apply_button")
        ) {
            Text("Primijeni filtere")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .testTag("filter_unwanted_list")
        ) {
            unwantedWords.forEach {
                Text(text = it)
            }
        }

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.testTag("back_button")
        ) {
            Text("Nazad")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}