package com.example.pocketlibrary.ui.screen.element

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.theme.LightGray

@Composable
fun OutlinedTag(text: String) {

    Surface(
        shape = RoundedCornerShape(Dimens.CornerPill),
        color = LightGray,
        border = BorderStroke(Dimens.BorderThin, MaterialTheme.colorScheme.outline)
    ) {

        Text(
            text = "#$text",
            modifier = Modifier.padding(horizontal = Dimens.SpaceSmall, vertical = Dimens.SpaceXSmall),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}