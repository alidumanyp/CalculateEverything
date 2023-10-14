package com.aliduman.calculateeverything

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerBarScreen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector
) {
    /*
    object IdealWeight: DrawerBarScreen(
        route = "weight",
        title = "Ideal Weight",
        selectedIcon = Icons.Default.Person
    )
    */
}
