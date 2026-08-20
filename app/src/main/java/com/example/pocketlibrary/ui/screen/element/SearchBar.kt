package com.example.pocketlibrary.ui.screen.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.SearchBarHeight)
            .clip(RoundedCornerShape(Dimens.CornerXSmall))
            .background(MaterialTheme.colorScheme.surface)
    ) {

        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxSize()
                .height(Dimens.SearchBarHeight),
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
    }
}

