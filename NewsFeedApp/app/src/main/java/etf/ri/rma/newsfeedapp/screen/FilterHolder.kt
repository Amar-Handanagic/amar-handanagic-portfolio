package etf.ri.rma.newsfeedapp.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object FilterHolder {
    var category by mutableStateOf("Sve")
    var fromDate by mutableStateOf<String?>(null)
    var toDate by mutableStateOf<String?>(null)
    var unwantedWords by mutableStateOf(listOf<String>())
}
