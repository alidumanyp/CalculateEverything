package com.aliduman.calculateeverything

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerBarScreen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector
) {

    object Finance: DrawerBarScreen(
        route = "weight",
        title = "Finance",
        selectedIcon = Icons.Default.Person
    )
    object Fitness: DrawerBarScreen(
        route = "weight",
        title = "Fitness & Health",
        selectedIcon = Icons.Default.Person
    )
    object Math: DrawerBarScreen(
        route = "weight",
        title = "Math",
        selectedIcon = Icons.Default.Person
    )
    object Other: DrawerBarScreen(
        route = "weight",
        title = "Other",
        selectedIcon = Icons.Default.Person
    )
}
