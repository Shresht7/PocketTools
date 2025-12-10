package com.shresht7.pockettools.ui.screens.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shresht7.pockettools.R

/**
 * A composable function that displays a search input field.
 *
 * This component allows users to input a search query to filter content.
 * It includes a leading search icon and a placeholder text.
 *
 * @param query The current search query string.
 * @param onQueryChange A callback function that is invoked when the search query changes.
 */
@Composable
fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_icon_content_description)) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun SearchFieldPreview() {
    SearchField(query = "", onQueryChange = {})
}
