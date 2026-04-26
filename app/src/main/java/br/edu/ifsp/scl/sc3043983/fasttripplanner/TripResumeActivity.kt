package br.edu.ifsp.scl.sc3043983.fasttripplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import br.edu.ifsp.scl.sc3043983.fasttripplanner.ui.theme.FastTripPlannerTheme

class TripResumeActivity  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val destiny = intent.getStringExtra("DESTINY") ?: "Não informado"
        val numberOfDays = intent.getIntExtra("DAYS", 0)
        val budget = intent.getDoubleExtra("BUDGET", 0.0)
        val selectedHosting = intent.getStringExtra("HOSTING") ?: "Não informado"
        val selectedServices = intent.getStringExtra("SERVICES") ?: "Não informado"
        setContent {
            FastTripPlannerTheme {
                Surface {}
            }
        }
    }
}

@Composable
fun tripReport() {

}