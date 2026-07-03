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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pocketlibrary.R

@Composable
fun AddBookScreenVisual() {

    val chipColor = Color.White
    val backgroundColor = Color.LightGray
    val mutedTextColor = Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {

        Text(
            text = stringResource(R.string.save),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.add_a_book),
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(100.dp)
                .height(145.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(R.drawable.ic_image_placeholder),
                contentDescription = null,
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        LabeledField(
            label = R.string.title,
            value = R.string.the_star
        )

        Spacer(modifier = Modifier.height(24.dp))

        LabeledField(
            label = R.string.author,
            value = R.string.enter_the_author
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row (
            horizontalArrangement = Arrangement.spacedBy(16.dp)
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

        Spacer(modifier = Modifier.height(24.dp))

        LabeledMultilineField(
            label = R.string.description,
            value = R.string.enter_the_description,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.ic_tag),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.add_tags),
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow (
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            listOf(
                R.string.tag_romantic,
            ).forEach { tag ->

                Surface (
                    color = chipColor,
                    shape = RoundedCornerShape(6.dp)
                ) {

                    Text(
                        text = stringResource(tag),
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        color = mutedTextColor,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LabeledMultilineField(
            label = R.string.notes,
            value = R.string.enter_the_notes,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button (
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = stringResource(R.string.save),
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
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
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .padding(10.dp)
                .height(32.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(value),
                modifier = Modifier.padding(horizontal = 10.dp),
                color = Color.Gray,
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
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = stringResource(value),
                modifier = Modifier.padding(20.dp),
                color = Color.Gray,
            )
        }
    }
}