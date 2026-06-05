package com.example.pocketguidance.ui.theme.screens.report

import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pocketguidance.viewmodel.ReportViewModel
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: ReportViewModel, userId: Int, onBack: () -> Unit = {}) {

    val data = viewModel.chartData

    LaunchedEffect(Unit) {
        viewModel.load(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            AndroidView(factory = { context ->

            PieChart(context).apply {

                val entries = data.map {
                    PieEntry(it.second.toFloat(), it.first)
                }

                val dataSet = PieDataSet(entries, "Spending")
                dataSet.valueTextSize = 14f

                this.data = PieData(dataSet)
                invalidate()
            }

        })
    }
}
}
