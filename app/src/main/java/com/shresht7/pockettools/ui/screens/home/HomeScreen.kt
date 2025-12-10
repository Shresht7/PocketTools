package com.shresht7.pockettools.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shresht7.pockettools.R
import com.shresht7.pockettools.navigation.Screen
import com.shresht7.pockettools.navigation.screens

/**
 * The home screen of the application, which displays a searchable grid of available tools.
 *
 * This screen serves as the main entry point for users to access the various utility tools.
 * It features a search bar to filter the tools and a grid layout that displays the tools.
 *
 * @param screens The list of [Screen] objects representing the available tools.
 * @param onNavigateToTool A callback function that is invoked when a tool is selected,
 *                         triggering navigation to that tool's screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    screens: List<Screen> = emptyList(),
    onNavigateToTool: (Screen) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val filtered = screens.filter { it.title.contains(query, true) }

    var started by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        started = true
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SearchField(query, onQueryChange = { query = it })

            Spacer(modifier = Modifier.height(16.dp))

            ToolBox(filtered, screens, onNavigateToTool, started)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(screens = screens)
}