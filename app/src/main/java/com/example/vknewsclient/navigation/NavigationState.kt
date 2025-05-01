package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vknewsclient.domain.entity.FeedPost

class NavigationState(
    val navHostController: NavHostController,
) {

    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            launchSingleTop =
                true // Теперь объекты Вью падают в бэкстэк только уникальные

            popUpTo(navHostController.graph.findStartDestination().id) { // Установили так, потому что сейчас у нас стартовый экран = граф, а не экран
                saveState = true // при удалении экранов из бэкстэка их стейт сохраняется
            }
            // Удаляет все элементы из бэкстэка до текущего. Текущий экран находится всегда наверху бэкстэка

            restoreState = true // Позволяет восстановить стейт элементов, которые мы сохранили
        }
    }

    fun navigateToComments(feedPost: FeedPost) {
        navHostController.navigate(Screen.Comments.getRouteWithArgs(feedPost))
    }

}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState = remember { // Чтобы во время рекомпозиции данная функция не пересоздавалась
    NavigationState(navHostController)
}
