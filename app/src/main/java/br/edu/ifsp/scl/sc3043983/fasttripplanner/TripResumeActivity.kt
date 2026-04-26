package br.edu.ifsp.scl.sc3043983.fasttripplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.ifsp.scl.sc3043983.fasttripplanner.model.HostingType
import br.edu.ifsp.scl.sc3043983.fasttripplanner.model.ServiceType
import br.edu.ifsp.scl.sc3043983.fasttripplanner.ui.theme.FastTripPlannerTheme

class TripResumeActivity  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val destiny = intent.getStringExtra("DESTINY") ?: "Não informado"
        val numberOfDays = intent.getIntExtra("DAYS", 0)
        val budget = intent.getDoubleExtra("BUDGET", 0.0)

        // 1. Leitura segura da Hospedagem (com fallback para ECONOMIC caso dê erro)
        val hostingString = intent.getStringExtra("HOSTING") ?: ""
        val selectedHosting = try {
            HostingType.valueOf(hostingString)
        } catch (e: Exception) {
            HostingType.ECONOMIC
        }

        // 2. Leitura segura dos Serviços Extras (ignora se estiver vazio ou inválido)
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
                Surface {
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
    context : ComponentActivity?,
    destiny : String,
    numberOfDays : Int,
    budget : Double,
    selectedHosting: HostingType,
    selectedServices: Set<ServiceType>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Resumo da Viagem", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = destiny, onValueChange = {}, readOnly = true,
            label = { Text("Destino Escolhido") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )
        OutlinedTextField(
            value = numberOfDays.toString(), onValueChange = {}, readOnly = true,
            label = { Text("Numero de Dias") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )
        OutlinedTextField(
            value = "R$ $budget", onValueChange = {}, readOnly = true,
            label = { Text("Orçamento Diário") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )
        OutlinedTextField(
            value = selectedHosting.toString(), onValueChange = {}, readOnly = true,
            label = { Text("Tipo de Hospedagem") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        val servicesText = if (selectedServices.isEmpty()) "Nenhum" else selectedServices.joinToString(", ") { it.toString() }
        OutlinedTextField(
            value = servicesText, onValueChange = {}, readOnly = true,
            label = { Text("Serviços Extras") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        OutlinedTextField(
            value = "R$ ${calculateCost(numberOfDays, budget, selectedHosting, selectedServices)}",
            onValueChange = {}, readOnly = true,
            label = { Text("Custo Total") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        Button(
            onClick = {
                val intent = android.content.Intent(context, TripDataActivity::class.java)
                intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
                context?.startActivity(intent)
            },
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Reiniciar Calculo")
        }
    }
}