package edu.ucne.celainylopez_ap2_p1.presentation.tareas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun TareaScreen(
    viewModel: TareaViewModel = hiltViewModel(),
    tareaId: Int?,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(tareaId) {
        tareaId?.let {
            if (it > 0) {
                viewModel.findTarea(it)
            }
        }
    }

    SistemaBodyScreen(
        uiState = uiState,
        viewModel::onEvent,
        goBack = goBack,
        viewModel = viewModel
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SistemaBodyScreen(
    uiState: TareaUiState,
    onEvent: (TareaEvent) -> Unit,
    goBack: () -> Unit,
    viewModel: TareaViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registro Tarea",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    OutlinedTextField(
                        value = uiState.tareaId.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    OutlinedTextField(
                        value = uiState.descripcion,
                        onValueChange = { onEvent(TareaEvent.DescripcionChange(it)) },
                        label = { Text("Descripcion") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.tiempo.toString(),
                        onValueChange = { onEvent(TareaEvent.TiempoChange(it.toIntOrNull() ?: 0)) },
                        label = { Text("Tiempo en Minutos") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.errorMessage != null
                    )

                    Spacer(modifier = Modifier.padding(2.dp))
                    uiState.errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = {
                                onEvent(TareaEvent.New)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "new button"
                            )
                            Text(text = "Nuevo")
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        val scope = rememberCoroutineScope()

                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    val result = viewModel.saveTarea()
                                    if(result){
                                        goBack()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button"
                            )
                            Text(text = "Guardar")
                        }

                    }
                }
            }
        }
    }
}
