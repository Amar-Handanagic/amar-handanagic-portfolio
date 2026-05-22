// DateRangePicker.kt
package etf.ri.rma.newsfeedapp.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateRangePicker(
    onDismissRequest: () -> Unit,
    onDateRangeSelected: (String, String) -> Unit
) {
    val context = LocalContext.current
    var start by remember { mutableStateOf<String?>(null) }
    var end by remember { mutableStateOf<String?>(null) }
    val today = Calendar.getInstance()
    val fmt = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 8.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Izaberite početni datum")
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    DatePickerDialog(
                        context,
                        { _: DatePicker, y, m, d -> start = String.format("%02d-%02d-%04d", d, m+1, y) },
                        today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) { Text(start ?: "Odaberi...") }

                Spacer(Modifier.height(16.dp))
                Text("Izaberite završni datum")
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    DatePickerDialog(
                        context,
                        { _: DatePicker, y, m, d -> end = String.format("%02d-%02d-%04d", d, m+1, y) },
                        today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) { Text(end ?: "Odaberi...") }

                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onDateRangeSelected(start!!, end!!); onDismissRequest() },
                        enabled = start != null && end != null
                    ) { Text("Potvrdi") }
                    OutlinedButton(onClick = onDismissRequest) { Text("Otkaži") }
                }
            }
        }
    }
}
