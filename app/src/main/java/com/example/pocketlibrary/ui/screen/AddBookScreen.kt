package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun AddBookScreenVisual() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.SpaceLarge, vertical = Dimens.SpaceLarge)
    ) {

        Text(
            text = stringResource(R.string.save),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        Text(
            text = stringResource(R.string.add_a_book),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXXLarge))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(Dimens.ImagePlaceholderWidth)
                .height(Dimens.ImagePlaceholderHeight)
                .clip(RoundedCornerShape(Dimens.CornerLarge))
                .background(MaterialTheme.colorScheme.background)
                .border(Dimens.BorderThin, MaterialTheme.colorScheme.surface, RoundedCornerShape(Dimens.CornerLarge)),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(R.drawable.ic_image_placeholder),
                contentDescription = null,
            )

        }

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        LabeledField(
            label = R.string.title,
            value = R.string.the_star
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        LabeledField(
            label = R.string.author,
            value = R.string.enter_the_author
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        Row (
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium)
        ) {

            Box(modifier = Modifier.weight(1f)) {
                LabeledField(
                    label = R.string.total_pages,
                    value = R.string.enter_the_page_number
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                LabeledField(
                    label = R.string.language,
                    value = R.string.enter_the_language
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        LabeledMultilineField(
            label = R.string.description,
            value = R.string.enter_the_description,
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.ic_tag),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(Dimens.SpaceSmall))

            Text(
                text = stringResource(R.string.add_tags),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        FlowRow (
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
        ) {

            listOf(R.string.tag_romantic).forEach { tag ->
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(Dimens.CornerPill)
                ) {
                    Text(
                        text = "#" + stringResource(tag),
                        modifier = Modifier.padding(horizontal = Dimens.SpaceSmall, vertical = Dimens.SpaceXSmall),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        LabeledMultilineField(
            label = R.string.notes,
            value = R.string.enter_the_notes,
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXXXLarge))

        Button (
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(Dimens.CornerXSmall)
        ) {
            Text(
                text = stringResource(R.string.save),
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
    }
}


@Composable
private fun LabeledField(
    label: Int,
    value: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(label),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXSmall))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.CornerXSmall))
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimens.SpaceSmall)
                .height(Dimens.InputFieldHeight),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(value),
                modifier = Modifier.padding(horizontal = Dimens.SpaceSmall),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun LabeledMultilineField(
    label: Int,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Column (modifier = modifier) {
        Text(
            text = stringResource(label),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.CornerXSmall))
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = stringResource(value),
                modifier = Modifier.padding(Dimens.SpaceLarge),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}