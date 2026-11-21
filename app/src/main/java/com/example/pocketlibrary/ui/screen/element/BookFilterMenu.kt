package com.example.pocketlibrary.ui.screen.element

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pocketlibrary.ui.viewmodel.BookFilters
import com.example.pocketlibrary.ui.viewmodel.BookSort
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds

private data class MenuPlacement(
    val maxHeightPx : Int,
    val wightPx : Int,
    val anchorHeightPx : Int
)

val KEYBOARD_HIDE_TIMEOUT = 1000.milliseconds

@Composable
fun BookFilterMenu(
    filters : BookFilters,
    availableTags : List<String>,
    onToggleTag : (String) -> Unit,
    onOnlyWithFileChange : (Boolean) -> Unit,
    onSortChange : (BookSort) -> Unit,
    onClear : () -> Unit,
    modifier : Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var placement by remember { mutableStateOf<MenuPlacement?>(null) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val imeInsets = WindowInsets.ime
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val navBarInsets = WindowInsets.navigationBars
    val windowSize = LocalWindowInfo.current.containerSize
    val screenMarginPx = with(density) { Dimens.SpaceMedium.roundToPx() }

    val anchorHolder = remember { arrayOfNulls<LayoutCoordinates>(1) }

    Box(
        modifier = modifier.onGloballyPositioned { anchorHolder[0] = it }
    ) {
        IconButton(
            onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                scope.launch {
                    withTimeoutOrNull(KEYBOARD_HIDE_TIMEOUT) {
                        snapshotFlow { imeInsets.getBottom(density) }.first { it == 0 }
                    }
                }
                val bounds = anchorHolder[0]
                    ?.takeIf { it.isAttached }
                    ?.boundsInWindow()

                if (bounds != null) {
                    val bottomLimit =
                        windowSize.height - navBarInsets.getBottom(density) - screenMarginPx

                    placement = MenuPlacement(
                        maxHeightPx = (bottomLimit - bounds.bottom).toInt().coerceAtLeast(0),
                        wightPx = minOf(
                            windowSize.width * 0.85f,
                            windowSize.height * 0.6f
                        ).toInt(),
                        anchorHeightPx = (bounds.bottom - bounds.top).toInt()
                    )
                }

                expanded = true
            }
        ) {
            BadgedBox(
                badge = {
                    if (filters.activeCount > 0) {
                        Badge { Text(filters.activeCount.toString()) }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = stringResource(R.string.filters),
                    tint = Color.Unspecified
                )
            }
        }
    }

    val current = placement
    if (expanded && current != null) {
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset(x = 0, y = current.anchorHeightPx),
            onDismissRequest = { expanded = false }
        ) {
            BackHandler { expanded = false }

            val menuWidth = with(density) { current.wightPx.toDp() }
            val menuMaxHeight = with(density) { current.maxHeightPx.toDp() }

            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + scaleIn(
                    transformOrigin = TransformOrigin(pivotFractionX = 1f, pivotFractionY = 0f),
                    initialScale = 0.85f
                )
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .width(menuWidth)
                        .heightIn(max = menuMaxHeight)
                ) {
                    Column(
                        modifier = modifier
                            .padding(horizontal = Dimens.SpaceLarge)
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .verticalScroll(rememberScrollState())
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
                                    onClick = { onOnlyWithFileChange(! filters.onlyWithFile) },
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
                                    Button(
                                        modifier = Modifier.padding(Dimens.SpaceXSmall),
                                        onClick = onClear
                                    ) {
                                        Text(stringResource(R.string.clear_filters))
                                    }

                                }
                                Button(onClick = { expanded = false }) {
                                    Text(stringResource(R.string.search))
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun FilterSection(
    title : String,
    content : @Composable () -> Unit
) {
    Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall),
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