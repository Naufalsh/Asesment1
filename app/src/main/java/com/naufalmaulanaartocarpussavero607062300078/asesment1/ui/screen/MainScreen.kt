package com.naufalmaulanaartocarpussavero607062300078.asesment1.ui.screen

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naufalmaulanaartocarpussavero607062300078.asesment1.R
import com.naufalmaulanaartocarpussavero607062300078.asesment1.navigation.Screen
import com.naufalmaulanaartocarpussavero607062300078.asesment1.ui.theme.Asesment1Theme


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(navController : NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ), actions = {
                    IconButton(onClick = { navController.navigate(Screen.History.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.info_aplikasi)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var distance by rememberSaveable { mutableStateOf("") }
    var distanceError by rememberSaveable { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.km),
        stringResource(id = R.string.mil),
        stringResource(id = R.string.meter)
    )
    var distanceType by rememberSaveable { mutableStateOf(radioOptions[0]) }

    var speed by rememberSaveable { mutableStateOf("") }
    var speedError by rememberSaveable { mutableStateOf(false) }

    var result by rememberSaveable { mutableStateOf(Triple(0,0,0)) }


    Column(
        modifier = modifier.fillMaxSize().
        verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.deskripsi_aplikasi),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = distance,
            onValueChange = { distance = it },
            label = { Text(text = stringResource(R.string.label_jarak)) },
            trailingIcon = { IconPicker(distanceError, "") },
            supportingText = { ErrorHintInputInvalid(distanceError) },
            isError = distanceError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = stringResource(R.string.pilih_satuan_jarak))

        Row (
            modifier = Modifier.padding(top = 6.dp)
        ){
            radioOptions.forEach{text ->
                DistanceTypeOption(
                    label = text,
                    isSelected = distanceType == text,
                    modifier =Modifier.selectable(
                        selected = distanceType == text,
                        onClick ={distanceType = text},
                        role = Role.RadioButton
                    )
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }

        OutlinedTextField(
            value = speed,
            onValueChange = { speed = it },
            label = { Text(text = stringResource(R.string.label_masukkan_kecepatan)) },
            trailingIcon = { IconPicker(speedError, stringResource(R.string.satuan_km_perjam)) },
            supportingText = { ErrorHintInputInvalid(speedError) },
            isError = speedError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val numberRegex = Regex("^\\d+(\\.\\d+)?$")

            distanceError = distance.isEmpty() || !distance.matches(numberRegex) || distance.toDouble() <= 0
            speedError = speed.isEmpty() || !speed.matches(numberRegex) || speed.toDouble() <= 0

            if (!distanceError && !speedError) {
                val convertedDistance = convertDistance(distance.toDouble(), distanceType)
                result = calculateTravelTime(convertedDistance, speed.toDouble())
            } else {
                result = Triple(0, 0, 0)
            }
        }) {
            Text(text = stringResource(R.string.hitung))
        }

        if (result != Triple(0, 0, 0)) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(
                stringResource(R.string.keterangan_hasil,
                    distance,
                    distanceType,
                    speed),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                stringResource(R.string.hasil_waktu_tempuh,
                    result.first,
                    result.second,
                    result.third),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge
            )

            ShareResult(result)

        }
    }
}

fun convertDistance(distance: Double, unit: String): Double {
    return when (unit.lowercase()) {
        "mil" -> distance * 1.60934
        "meter" -> distance / 1000
        "km" -> distance
        else -> {
            println("Warning: Unit tidak dikenali ($unit), menggunakan default (km).")
            distance
        }
    }
}

fun calculateTravelTime(distance: Double, speed: Double): Triple<Int, Int, Int> {
    val timeInHours = distance / speed
    val hours = timeInHours.toInt()
    val minutes = ((timeInHours - hours) * 60).toInt()
    val seconds = (((timeInHours - hours) * 60 - minutes) * 60).toInt()
    return Triple(hours, minutes, seconds)
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
    } else {
        Text (text = unit)
    }
}

@Composable
fun ErrorHintInputInvalid(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(id = R.string.input_invalid))
    }
}


@Composable
fun DistanceTypeOption(label:String, isSelected:Boolean, modifier:Modifier) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected =  isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun ShareResult(result: Triple<Int, Int, Int>) {
    val context = LocalContext.current

    IconButton(onClick = {
        val shareText = context.getString(
            R.string.text_bagikan,
            result.first, result.second, result.third
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
    }) {
        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = stringResource(id = R.string.share)
        )
    }
}




@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asesment1Theme {
        MainScreen(rememberNavController())
    }
}