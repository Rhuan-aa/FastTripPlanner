package br.edu.ifsp.scl.sc3043983.fasttripplanner

import android.content.Intent
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
import androidx.compose.material3.MaterialTheme
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
import kotlin.text.toDoubleOrNull
import kotlin.text.toIntOrNull

class TripOptionsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val destiny = intent.getStringExtra("DESTINY") ?: "Não informado"
        val numberOfDays = intent.getIntExtra("DAYS", 0)
        val budget = intent.getDoubleExtra("BUDGET", 0.0)
        setContent {
            FastTripPlannerTheme {
                Surface {
                    OptionsSection(
                        destiny,
                        numberOfDays,
                        budget
                    )
                }
            }
        }
    }
}

@Composable
fun OptionsSection(destiny: String, numberOfDays: Int, budget: Double) {
    val context = LocalActivity.current as? ComponentActivity
    var selectedHosting by remember { mutableStateOf(HostingType.ECONOMIC) }
    var selectedServices by remember { mutableStateOf(setOf<ServiceType>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Viagem para: $destiny",
            style = MaterialTheme.typography.headlineSmall
        )

        DropdownMenuComponent(
            selectedHosting = selectedHosting,
            onHostingSelected = { selectedHosting = it }
        )

        CheckServicesComponent(
            selectedServices = selectedServices,
            onServicesChanged = { selectedServices = it }
        )

        BottomButtons(
            context = context,
            destiny = destiny,
            numberOfDays = numberOfDays,
            budget = budget,
            selectedHosting = selectedHosting,
            selectedServices = selectedServices
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuComponent(
    selectedHosting: HostingType,
    onHostingSelected: (HostingType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(24.dp)
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            HostingType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.toString()) },
                    onClick = {
                        onHostingSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CheckServicesComponent(
    selectedServices: Set<ServiceType>,
    onServicesChanged: (Set<ServiceType>) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text("Serviços Extras:", modifier = Modifier.padding(bottom = 8.dp))
        ServiceType.entries.forEach { service ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedServices.contains(service),
                    onCheckedChange = { isChecked ->
                        val newSet = if (isChecked) selectedServices + service
                        else selectedServices - service
                        onServicesChanged(newSet)
                    }
                )
                Text(text = service.toString())
            }
        }
    }
}

@Composable
fun BottomButtons(
    context : ComponentActivity?,
    destiny : String,
    numberOfDays : Int,
    budget : Double,
    selectedHosting: HostingType,
    selectedServices: Set<ServiceType>
) {
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
            val intent = Intent(
                context,
                TripResumeActivity::class.java
            ).apply {
                putExtra("DESTINY", destiny)
                putExtra("DAYS", numberOfDays)
                putExtra("BUDGET", budget)
                putExtra("HOSTING", selectedHosting.toString())
                putExtra("SERVICES", selectedServices.toString())
            }
            context?.startActivity(intent)
        }) {
            Text("Calcular")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewOptions() {
    FastTripPlannerTheme {
        OptionsSection(
            destiny = "",
            numberOfDays = 0,
            budget = 0.00
        )
    }
}