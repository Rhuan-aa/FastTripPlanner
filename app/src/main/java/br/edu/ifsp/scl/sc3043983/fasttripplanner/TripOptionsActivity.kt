package br.edu.ifsp.scl.sc3043983.fasttripplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Luggage // Ícone de mala
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifsp.scl.sc3043983.fasttripplanner.model.HostingType
import br.edu.ifsp.scl.sc3043983.fasttripplanner.model.ServiceType
import br.edu.ifsp.scl.sc3043983.fasttripplanner.ui.theme.FastTripPlannerTheme

class TripOptionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val destiny = intent.getStringExtra("DESTINY") ?: "Não informado"
        val numberOfDays = intent.getIntExtra("DAYS", 0)
        val budget = intent.getDoubleExtra("BUDGET", 0.0)
        setContent {
            FastTripPlannerTheme {
                Surface(color = Color.Transparent) {
                    OptionsSection(destiny, numberOfDays, budget)
                }
            }
        }
    }
}

@Composable
fun OptionsSection(destiny: String, numberOfDays: Int, budget: Double) {
    val context = LocalActivity.current as? ComponentActivity
    var selectedHosting by rememberSaveable { mutableStateOf(HostingType.ECONOMIC) }
    var selectedServices by rememberSaveable { mutableStateOf(setOf<ServiceType>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            imageVector = Icons.Default.Luggage,
            contentDescription = "Ícone de bagagem",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Personalizar",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Destino: $destiny",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DropdownMenuComponent(
                    selectedHosting = selectedHosting,
                    onHostingSelected = { selectedHosting = it }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                CheckServicesComponent(
                    selectedServices = selectedServices,
                    onServicesChanged = { selectedServices = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

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
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(16.dp)
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
        Text(
            text = "Serviços Extras:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ServiceType.entries.forEach { service ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val newSet = if (selectedServices.contains(service)) {
                            selectedServices - service
                        } else {
                            selectedServices + service
                        }
                        onServicesChanged(newSet)
                    }
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = selectedServices.contains(service),
                    onCheckedChange = null,
                )
                Text(
                    text = service.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun BottomButtons(
    context: ComponentActivity?,
    destiny: String,
    numberOfDays: Int,
    budget: Double,
    selectedHosting: HostingType,
    selectedServices: Set<ServiceType>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = { context?.finish() },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Voltar")
        }

        Button(
            onClick = {
                val intent = Intent(context, TripResumeActivity::class.java).apply {
                    putExtra("DESTINY", destiny)
                    putExtra("DAYS", numberOfDays)
                    putExtra("BUDGET", budget)
                    putExtra("HOSTING", selectedHosting.name)

                    val servicesText = selectedServices.joinToString(",") { it.name }
                    putExtra("SERVICES", servicesText)
                }
                context?.startActivity(intent)
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
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