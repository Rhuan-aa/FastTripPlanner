package br.edu.ifsp.scl.sc3043983.fasttripplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifsp.scl.sc3043983.fasttripplanner.model.HostingType
import br.edu.ifsp.scl.sc3043983.fasttripplanner.model.ServiceType
import br.edu.ifsp.scl.sc3043983.fasttripplanner.ui.theme.FastTripPlannerTheme

class TripResumeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val destiny = intent.getStringExtra("DESTINY") ?: "Não informado"
        val numberOfDays = intent.getIntExtra("DAYS", 0)
        val budget = intent.getDoubleExtra("BUDGET", 0.0)

        val hostingString = intent.getStringExtra("HOSTING") ?: ""
        val selectedHosting = try {
            HostingType.valueOf(hostingString)
        } catch (e: Exception) {
            HostingType.ECONOMIC
        }

        val servicesString = intent.getStringExtra("SERVICES") ?: ""
        val selectedServices = if (servicesString.isNotBlank() && servicesString != "Não informado") {
            try {
                servicesString.split(",").map { ServiceType.valueOf(it.trim()) }.toSet()
            } catch (e: Exception) {
                emptySet()
            }
        } else {
            emptySet()
        }

        setContent {
            FastTripPlannerTheme {
                Surface(color = Color.Transparent) {
                    TripReport(
                        context = this,
                        destiny = destiny,
                        numberOfDays = numberOfDays,
                        budget = budget,
                        selectedHosting = selectedHosting,
                        selectedServices = selectedServices
                    )
                }
            }
        }
    }
}

fun calculateCost(
    numberOfDays: Int,
    budget: Double,
    selectedHosting: HostingType,
    selectedServices: Set<ServiceType>
): Double {
    var totalCost = budget * numberOfDays
    totalCost *= selectedHosting.modifier
    totalCost += selectedServices.sumOf { it.price }
    return totalCost
}

@Composable
fun TripReport(
    context: ComponentActivity?,
    destiny: String,
    numberOfDays: Int,
    budget: Double,
    selectedHosting: HostingType,
    selectedServices: Set<ServiceType>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Concluído",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Resumo da Viagem",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
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
                OutlinedTextField(
                    value = destiny, onValueChange = {}, readOnly = true,
                    label = { Text("Destino Escolhido") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = numberOfDays.toString(), onValueChange = {}, readOnly = true,
                    label = { Text("Número de Dias") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = "R$ $budget", onValueChange = {}, readOnly = true,
                    label = { Text("Orçamento Diário") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = selectedHosting.toString(), onValueChange = {}, readOnly = true,
                    label = { Text("Tipo de Hospedagem") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
                )

                val servicesText =
                    if (selectedServices.isEmpty()) "Nenhum"
                    else selectedServices.joinToString(", ") { it.toString() }
                OutlinedTextField(
                    value = servicesText, onValueChange = {}, readOnly = true,
                    label = { Text("Serviços Extras") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                val finalCost = calculateCost(numberOfDays, budget, selectedHosting, selectedServices)
                OutlinedTextField(
                    value = "R$ " + "%.2f".format(finalCost), onValueChange = {}, readOnly = true,
                    label = { Text("Custo Total Estimado", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val intent = Intent(context, TripDataActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                context?.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Nova Simulação", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTripReport() {
    FastTripPlannerTheme {
        Surface(color = Color.Transparent) {
            TripReport(
                context = null,
                destiny = "Fernando de Noronha",
                numberOfDays = 7,
                budget = 450.0,
                selectedHosting = HostingType.ECONOMIC,
                selectedServices = emptySet()
            )
        }
    }
}