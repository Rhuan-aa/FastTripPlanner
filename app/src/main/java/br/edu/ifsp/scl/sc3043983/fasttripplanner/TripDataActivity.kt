package br.edu.ifsp.scl.sc3043983.fasttripplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.ifsp.scl.sc3043983.fasttripplanner.ui.theme.FastTripPlannerTheme

class TripDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FastTripPlannerTheme {
                Surface {
                    DataFields()
                }
            }
        }
    }
}

fun validateInformation(destiny: String, numberOfDays: String, budget: String): Boolean {
    return destiny.isNotEmpty() && numberOfDays.isNotEmpty() && budget.isNotEmpty()
}

@Composable
fun DataFields() {
    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var destiny by remember { mutableStateOf("") }
        var numberOfDays by remember { mutableStateOf("") }
        var budget by remember { mutableStateOf("") }

        Column(
            Modifier
                .padding(16.dp),
            Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            OutlinedTextField(
                value = destiny,
                onValueChange = { destiny = it },
                label = { Text("Destino") },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(
                value = numberOfDays,
                onValueChange = { numberOfDays = it },
                label = { Text("Numero de Dias") },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("Orçamento Diário") },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
        Button(
            onClick = {
                if (!validateInformation(destiny, numberOfDays, budget)){
                    Toast.makeText(
                        context,
                        "Preencha todos os campos!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(
                        context,
                        TripOptionsActivity::class.java
                    ).apply {
                        putExtra("DESTINY", destiny)
                        putExtra("DAYS", numberOfDays.toIntOrNull() ?: 0)
                        putExtra("BUDGET", budget.toDoubleOrNull() ?: 0.0)
                    }
                    context.startActivity(intent)
                }
            },
            shape = RoundedCornerShape(32.dp),
        ) {
            Text("Avançar")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    FastTripPlannerTheme {
        DataFields()
    }
}