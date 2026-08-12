package com.example.pocketlibrary.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pocketlibrary.R
import androidx.compose.runtime.getValue
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun BottomBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface (
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row ( modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpaceXSmall),
            horizontalArrangement = Arrangement.SpaceEvenly,
            ) {

            IconButton(onClick = {
                //Tapping the same tab twice pushes a new copy of the screen onto the back stack.
                // The idiomatic fix is to use launchSingleTop = true and popUpTo the start destination:
                navController.navigate(Screen.Search.route) {
                    popUpTo(Screen.Library.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_tag),
                    contentDescription = "Tags",
                    tint = if (currentRoute == Screen.Search.route)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = { navController.navigate(Screen.Library.route) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = "Library",
                    tint = if (currentRoute == Screen.Search.route)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = { navController.navigate(Screen.AddBook.route) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = "Add book",
                    tint = if (currentRoute == Screen.Search.route)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }
    }
}