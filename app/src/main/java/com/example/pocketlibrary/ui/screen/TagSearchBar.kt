package com.example.pocketlibrary.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens

@Composable
fun TagSearchBar() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.SearchBarHeight)
            .clip(RoundedCornerShape(Dimens.CornerXSmall))
            .background(MaterialTheme.colorScheme.surface)
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

            //Todo TextField
            Text(
                text = stringResource(R.string.search),
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
