package com.example.pocketlibrary.ui.screen.element

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pocketlibrary.ui.viewmodel.BookFilters
import com.example.pocketlibrary.ui.viewmodel.BookSort
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun BookFilterMenu(
    filters : BookFilters ,
    availableTags : List<String> ,
    onToggleTag : (String) -> Unit ,
    onOnlyWithFileChange : (Boolean) -> Unit ,
    onSortChange : (BookSort) -> Unit ,
    onClear : () -> Unit ,
    modifier : Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            BadgedBox(
                badge = {
                    if (filters.activeCount > 0) {
                        Badge { Text(filters.activeCount.toString()) }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter) ,
                    contentDescription = stringResource(R.string.filters) ,
                    tint = Color.Unspecified
                )
            }
        }
    }

    DropdownMenu(
        expanded = expanded ,
        onDismissRequest = { expanded = false }
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = Dimens.SpaceLarge)
        ) {


            FilterSection(title = stringResource(R.string.sort_by)) {
                BookSort.entries.forEach { sort ->
                    FilterChip(
                        selected = filters.sort == sort,
                        onClick = { onSortChange(sort) },
                        label = { Text(stringResource(sort.labelRes())) }
                    )
                }
            }


                FilterSection(title = stringResource(R.string.reading)) {
                    FilterChip(
                        selected = filters.onlyWithFile,
                        onClick = { onOnlyWithFileChange(!filters.onlyWithFile) },
                        label = { Text(stringResource(R.string.with_attached_file)) }
                    )
                }

                if (availableTags.isNotEmpty()) {
                    FilterSection(title = stringResource(R.string.tags)) {
                        availableTags.forEach { tag ->
                            FilterChip(
                                selected = tag in filters.selectedTags,
                                onClick = { onToggleTag(tag) },
                                label = { Text("#$tag") }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.SpaceXSmall),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (filters.activeCount > 0) {
                        Button(modifier = Modifier.padding(Dimens.SpaceXSmall),
                            onClick = onClear
                        ) {
                            Text(stringResource(R.string.clear_filters))
                        }

                    }
                    Button (onClick = { expanded = false }) {
                        Text(stringResource(R.string.search))
                    }

                }
            }
        }
    }


@Composable
private fun FilterSection(
    title : String ,
    content : @Composable () -> Unit
) {
    Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))
    Text(
        text = title ,
        style = MaterialTheme.typography.labelLarge ,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall) ,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        content()
    }
}

private fun BookSort.labelRes() : Int = when (this) {
    BookSort.RECENT -> R.string.sort_recent
    BookSort.TITLE -> R.string.sort_title
    BookSort.AUTHOR -> R.string.sort_author
}