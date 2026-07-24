package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.theme.LightGray

@Composable
fun TagScreen(book: Book) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.SpaceXLarge,
                vertical = Dimens.SpaceXLarge)
    ) {

        Text(
            text = stringResource(R.string.tags),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        TagSearchBar()

        Spacer(modifier = Modifier.height(Dimens.SpaceXXLarge))

        Text(
            text = stringResource(R.string.tag_search_history),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        FlowRow (
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
        ) {

            listOf(
                R.string.tag_bestseller,
                R.string.tag_classic,
                R.string.tag_intriguing,
                R.string.tag_tag,
                R.string.tag_tag,
                R.string.tag_classic,
                R.string.tag_bestseller
            ).forEach {

                OutlinedTag(it)

            }

            Text(
                text = "#" + stringResource(R.string.show_more),
                modifier = Modifier.padding(top = Dimens.SpaceXSmall),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceXXLarge))

        Text(
            text = stringResource(R.string.books_with_tag_romantic),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        BookTagCard(book)

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))
    }
}

@Composable
private fun TagSearchBar() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.SearchBarHeight)
            .clip(RoundedCornerShape(Dimens.CornerXSmall))
            .background(LightGray)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(Dimens.SpaceMedium))

            //TextField
            Text(
                text = stringResource(R.string.tag_romantic),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
private fun OutlinedTag(text: Int) {

    Surface(
        shape = RoundedCornerShape(Dimens.CornerPill),
        color = LightGray,
        border = BorderStroke(Dimens.BorderThin, MaterialTheme.colorScheme.outline)
    ) {

        Text(
            text = stringResource(text),
            modifier = Modifier.padding(horizontal = Dimens.SpaceSmall, vertical = Dimens.SpaceXSmall),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BookTagCard(book: Book) {  //is only used inside TagScreen.kt, so it should be private.

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerXLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CornerSmall) //corner radius dimension being reused for elevation. Confusing, and not recommended. Should create a new dimension for elevation
    ) {

        Row(modifier = Modifier.padding(Dimens.SpaceSmall)) {

            Image(
                painter = painterResource(book.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .width(Dimens.BookCoverWidthSmall)
                    .height(Dimens.BookCoverHeightSmall),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(Dimens.SpaceSmall))

            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = stringResource(book.title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceXXSmall))

                Text(
                    text = stringResource(book.author),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpaceXSmall)
                ) {
                    //The number 3 appears twice independently. If someone changes one, the other won't match. Extract it to const something like MAX_TAGS_TO_SHOW = 3 and use it in both places. This will make it easier to change in the future.
                    book.tags.take(3).forEach {
                        OutlinedTag(it)
                    }

                    if (book.tags.size > 3) {
                        Text(
                            text = stringResource(R.string.more),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    }
}