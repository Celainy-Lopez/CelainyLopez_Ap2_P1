package edu.ucne.registrosistema.presentation.sistemas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.celainylopez_ap2_p1.data.local.entities.TareaEntity
import edu.ucne.celainylopez_ap2_p1.presentation.tareas.TareaEvent
import edu.ucne.celainylopez_ap2_p1.presentation.tareas.TareaUiState
import edu.ucne.celainylopez_ap2_p1.presentation.tareas.TareaViewModel

@Composable
fun TareaListScreen(
    viewModel: TareaViewModel = hiltViewModel(),
    goToTarea: (Int) -> Unit,
    createTarea: () -> Unit,
    deleteTarea: ((TareaEntity) -> Unit)? = null
){
    val  uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TareaListBodyScreen(
        uiState = uiState,
        goToTarea = goToTarea,
        createTarea = createTarea,
        deleteTarea = { tarea ->
            viewModel.onEvent(TareaEvent.TareaChange(tarea.tareaId?:0))
            viewModel.onEvent(TareaEvent.Delete)

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaListBodyScreen(
    uiState: TareaUiState,
    goToTarea: (Int) -> Unit,
    createTarea: () -> Unit,
    deleteTarea: (TareaEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de tareas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = createTarea) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(1f), text= "ID")
                Text(modifier = Modifier.weight(1f), text = "Descripcion")
                Text(modifier = Modifier.weight(1f), text = "Acciones")

            }

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.tareas) { tarea ->
                    TareaRow(
                        it = tarea,
                        goToTarea = {goToTarea(tarea.tareaId?: 0)},
                        deleteTarea = deleteTarea
                    )
                }
            }
        }
    }
}

@Composable
private fun TareaRow(
    it: TareaEntity,
    goToTarea: () -> Unit,
    deleteTarea: (TareaEntity) ->Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = cardElevation(defaultElevation = 6.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(modifier = Modifier.weight(1f), text = it.tareaId.toString())
            Text(modifier = Modifier.weight(1f), text = it.descripcion)
            Text(modifier = Modifier.weight(1f), text = it.tiempo.toString())

            Row(modifier = Modifier.weight(1f)){
                IconButton(onClick = goToTarea) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { deleteTarea(it) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
        HorizontalDivider()
    }

}