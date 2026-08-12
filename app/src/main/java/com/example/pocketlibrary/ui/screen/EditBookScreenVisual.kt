package com.example.pocketlibrary.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.net.toUri
import com.example.pocketlibrary.R
import com.example.pocketlibrary.data.local.entity.BookWithTags
import com.example.pocketlibrary.ui.theme.Dimens
import com.example.pocketlibrary.ui.viewmodel.BookViewModel


@Composable
fun EditBookScreenVisual(
    bookViewModel: BookViewModel,
    bookWithTags: BookWithTags,
    onBookUpdate: () -> Unit = {}
) {
    val context = LocalContext.current
    val book = bookWithTags.book

    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var language by remember { mutableStateOf(book.language ?: "") }
    var pageCountText by remember { mutableStateOf(book.pageNumber?.toString() ?: "") }
    var description by remember { mutableStateOf(book.bookDescription ?: "") }
    var notes by remember { mutableStateOf(book.bookNotes ?: "") }
    var tagInput by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf(bookWithTags.tags.map { it.name }) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(book.imageUri?.toUri()) }

    val imagePickerLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri = uri
        }
    }

    fun addCurrentTag() {
        val trimmed = tagInput.trim()
        if (trimmed.isNotEmpty() && !tags.contains(trimmed)) {
            tags += trimmed
        }
        tagInput = ""
    }

    val canSave = title.isNotBlank() && author.isNotBlank()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentPadding = PaddingValues(
            horizontal = Dimens.SpaceLarge,
            vertical = Dimens.SpaceLarge
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceLarge)
    ) {
        item {
            AddBookHeader(
                selectedImageUri = selectedImageUri,
                onImageClick = {
                    imagePickerLaunch.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            )
        }


        item {
            LabeledField(
                label = stringResource(R.string.title),
                value = title,
                onValueChange = { title = it },
                placeholder = stringResource(R.string.enter_title)
            )
        }

        item {
            LabeledField(
                label = stringResource(R.string.author),
                value = author,
                onValueChange = { author = it },
                placeholder = stringResource(R.string.enter_author)
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium)) {
                Box(modifier = Modifier.weight(1f)) {
                    LabeledField(
                        label = stringResource(R.string.total_pages),
                        value = pageCountText,
                        onValueChange = { new ->
                            if (new.all { it.isDigit() }) pageCountText = new
                        },
                        placeholder = stringResource(R.string.enter_page_number),
                        keyboardType = KeyboardType.Number
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    LabeledField(
                        label = stringResource(R.string.language),
                        value = language,
                        onValueChange = { language = it },
                        placeholder = stringResource(R.string.enter_language)
                    )
                }
            }
        }

        item {
            LabeledMultilineField(
                label = stringResource(R.string.description),
                value = description,
                onValueChange = { description = it },
                placeholder = stringResource(R.string.enter_description)
            )
        }


        item {
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
        }

        item {
            TextField(
                value = tagInput,
                onValueChange = { tagInput = it },
                placeholder = { Text(stringResource(R.string.add_tags)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimens.CornerXSmall)),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { addCurrentTag() }),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpaceSmall)
            ) {
                tags.forEach { tag ->
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(Dimens.CornerPill)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = Dimens.SpaceSmall,
                                vertical = Dimens.SpaceXXSmall
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#$tag",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(Dimens.SpaceXXSmall))
                            Text(
                                text = "×",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.clickable { tags = tags - tag }
                            )
                        }
                    }
                }
            }
        }

        item {
            LabeledMultilineField(
                label = stringResource(R.string.notes),
                value = notes,
                onValueChange = { notes = it },
                placeholder = stringResource(R.string.enter_notes)
            )
        }

        item {
            Button(
                onClick = {
                    val pageCount = pageCountText.toIntOrNull() ?: 0
                    bookViewModel.updateBook(
                        book = book.copy(
                            title = title,
                            author = author,
                            language = language,
                            pageNumber = pageCount,
                            bookDescription = description,
                            bookNotes = notes,
                            imageUri = selectedImageUri?.toString()
                        ),
                        tags = tags
                    )
                    onBookUpdate()
                },
                enabled = canSave,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(Dimens.CornerXSmall)
            ) {
                Text(
                    text = stringResource(R.string.edit),
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
        }
    }
}