package edu.ucne.celainylopez_ap2_p1.presentation.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.celainylopez_ap2_p1.data.local.entities.TareaEntity
import edu.ucne.celainylopez_ap2_p1.data.repository.TareasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TareaViewModel @Inject constructor(
    private val tareasRepository: TareasRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TareaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTareas()
    }


    fun onEvent(event: TareaEvent) {
        when (event) {
            is TareaEvent.TareaChange -> onTareaIdChange(event.tareaId)
            is TareaEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            is TareaEvent.TiempoChange -> onTiempoChange(event.tiempo)

            TareaEvent.Save -> viewModelScope.launch { saveTarea() }
            TareaEvent.Delete -> deleteTarea()
            TareaEvent.New -> nuevo()
        }
    }


    suspend fun saveTarea(): Boolean {
        return if (_uiState.value.descripcion.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Campos vacios")
            }
            false
        } else {
            tareasRepository.save(_uiState.value.toEntity())
            true
        }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                tareaId = null,
                descripcion = "",
                tiempo = 0,
                errorMessage = null
            )
        }
    }

    fun findTarea(tareaId: Int) {
        viewModelScope.launch {
            if (tareaId > 0) {
                val tarea = tareasRepository.find(tareaId)
                _uiState.update {
                    it.copy(
                        tareaId = tarea?.tareaId,
                        descripcion = tarea?.descripcion ?: "",
                        tiempo = tarea?.tiempo?: 0
                    )
                }
            }
        }
    }

    fun deleteTarea() {
        viewModelScope.launch {
            tareasRepository.delete(_uiState.value.toEntity())
        }
    }

    val tareas: StateFlow<List<TareaEntity>> = tareasRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = emptyList()
        )

    private fun getTareas() {
        viewModelScope.launch {
            tareasRepository.getAll().collect { tareas ->
                _uiState.update {
                    it.copy(
                        tareas = tareas
                    )
                }
            }
        }
    }


    private fun onTareaIdChange(tareaId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tareaId = tareaId
                )
            }
        }
    }

    private fun onDescripcionChange(descripcion: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    descripcion = descripcion
                )
            }
        }
    }

    private fun onTiempoChange(tiempo: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tiempo = tiempo
                )
            }
        }
    }



    fun TareaUiState.toEntity() = TareaEntity(
        tareaId = tareaId,
        descripcion = descripcion ?: "",
        tiempo = tiempo?: 0
    )
}