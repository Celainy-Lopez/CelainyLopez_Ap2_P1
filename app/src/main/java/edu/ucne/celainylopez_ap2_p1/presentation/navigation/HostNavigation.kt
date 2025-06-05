package edu.ucne.celainylopez_ap2_p1.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.celainylopez_ap2_p1.presentation.tareas.TareaScreen
import edu.ucne.registrosistema.presentation.sistemas.TareaListScreen

@Composable
fun HomeNavHost(
    navHostController: NavHostController
){
    NavHost(
        navController = navHostController,
        startDestination = Screen.TareaList
    ) {
        //pantalla lista de prioridades
        composable <Screen.TareaList> {

            TareaListScreen(
                goToTarea = { id ->
                    navHostController.navigate(Screen.Tarea(id ?: 0))
                },
                createTarea = {
                    navHostController.navigate(Screen.Tarea(0))
                }
            )
        }

        composable <Screen.Tarea> { backStack ->
            val tareaId = backStack.toRoute<Screen.Tarea>().tareaId
            TareaScreen (
                tareaId = tareaId,
                goBack = { navHostController.popBackStack() }
            )
        }
    }
}