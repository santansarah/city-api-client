package com.example.cityapiclient.presentation

import com.example.cityapiclient.presentation.AppScreens.HOME_SCREEN
import com.example.cityapiclient.presentation.AppScreens.ONBOARDING_SCREEN

object AppScreens {
    const val ONBOARDING_SCREEN = "onboarding"
    const val HOME_SCREEN = "home"
}

object AppDestinationsArgs {
    const val LAST_SCREEN_ARG = "lastScreenViewed"
}

object AppDestinations {
    const val ONBOARDING_ROUTE = ONBOARDING_SCREEN
    const val HOME_ROUTE = HOME_SCREEN
}

/**
 * Models the navigation actions in the app.
 */
/*
class AppNavigationActions(private val navController: NavHostController) {

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager

    fun navigateToOnboarding() {
        val navigatesFromDrawer = userMessage == 0
        navController.navigate(
            TASKS_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = !navigatesFromDrawer
                saveState = navigatesFromDrawer
            }
            launchSingleTop = true
            restoreState = navigatesFromDrawer
        }
    }

    fun navigateToStatistics() {
        navController.navigate(TodoDestinations.STATISTICS_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToTaskDetail(taskId: String) {
        navController.navigate("$TASK_DETAIL_SCREEN/$taskId")
    }

    fun navigateToAddEditTask(title: Int, taskId: String?) {
        navController.navigate(
            "$ADD_EDIT_TASK_SCREEN/$title".let {
                if (taskId != null) "$it?$TASK_ID_ARG=$taskId" else it
            }
        )
    }
}



*/
