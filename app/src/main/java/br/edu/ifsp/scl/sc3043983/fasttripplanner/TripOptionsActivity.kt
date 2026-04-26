package br.edu.ifsp.scl.sc3043983.fasttripplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifsp.scl.sc3043983.fasttripplanner.model.HostingType
import br.edu.ifsp.scl.sc3043983.fasttripplanner.model.ServiceType
import br.edu.ifsp.scl.sc3043983.fasttripplanner.ui.theme.FastTripPlannerTheme
import kotlin.collections.plus

class TripOptionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FastTripPlannerTheme {
                Surface {
                    OptionsSection()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun OptionsSection() {
    val context = LocalActivity.current as? ComponentActivity

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropdownMenu()
        CheckServices()
        BottomButtons(context)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
fun DropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedHosting by remember { mutableStateOf(HostingType.ECONOMIC) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedHosting.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo de Hospedagem") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            HostingType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.toString()) },
                    onClick = {
                        selectedHosting = type
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CheckServices() {
    var selectedServices by remember { mutableStateOf(setOf<ServiceType>()) }

    Column(
        Modifier.fillMaxWidth(),

    ) {
        Text("Serviços Extras:", modifier = Modifier.padding(bottom = 8.dp))
        ServiceType.entries.forEach { service ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedServices.contains(service),
                    onCheckedChange = { isChecked ->
                        selectedServices =
                            if (isChecked) selectedServices + service
                            else selectedServices - service
                    }
                )
                Text(text = service.toString())
            }
        }
    }
}

@Composable
fun BottomButtons(context : ComponentActivity?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        Button(onClick = {
            context?.finish()
        }) {
            Text("Voltar")
        }

        Button(onClick = {
        }) {
            Text("Calcular")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewOptions() {
    FastTripPlannerTheme {
        OptionsSection()
    }
}