package com.naufalmaulanaartocarpussavero607062300078.asesment1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.naufalmaulanaartocarpussavero607062300078.asesment1.R
import com.naufalmaulanaartocarpussavero607062300078.asesment1.ui.theme.Asesment1Theme


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var distance by remember { mutableStateOf("") }
    var speed by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val unitOptions = listOf(
        stringResource(id = R.string.km),
        stringResource(id = R.string.mil),
        stringResource(id = R.string.meter)
    )
    var unit by rememberSaveable { mutableStateOf(unitOptions[0]) }

    Column(
        modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = distance,
            onValueChange = { distance = it },
            label = { Text(text = stringResource(R.string.label_jarak)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.pilih_satuan_jarak))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            unitOptions.forEach { option ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    RadioButton(
                        selected = unit == option,
                        onClick = { unit = option }
                    )
                    Text(option)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = speed,
            onValueChange = { speed = it },
            label = { Text(text = stringResource(R.string.label_masukkan_kecepatan)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            trailingIcon = { Text(text = stringResource(R.string.satuan_km_perjam)) },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                result = try {
                    val dist = distance.toDouble()
                    val spd = speed.toDouble()
                    if (spd > 0) calculateTravelTime(convertDistance(dist, unit), spd) else "Kecepatan harus lebih dari 0!"
                } catch (e: NumberFormatException) {
                    "Masukkan angka yang valid!"
                }
            }) {
                Text(text = stringResource(R.string.hitung))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = result, style = MaterialTheme.typography.bodyLarge)
    }
}


fun convertDistance(distance: Double, unit: String): Double {
    return when (unit) {
        "mil" -> distance * 1.60934
        "meter" -> distance / 1000
        else -> distance
    }
}

fun calculateTravelTime(distance: Double, speed: Double): String {
    val timeInHours = distance / speed
    val hours = timeInHours.toInt()
    val minutes = ((timeInHours - hours) * 60).toInt()
    val seconds = (((timeInHours - hours) * 60 - minutes) * 60).toInt()
    return "Hasil: $hours jam $minutes menit $seconds detik"
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asesment1Theme {
        MainScreen()
    }
}