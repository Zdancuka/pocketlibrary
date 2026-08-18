package com.example.pocketlibrary.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.pocketlibrary.R
import com.example.pocketlibrary.ui.theme.Dimens
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.pocketlibrary.data.local.entity.BookEntity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import com.example.pocketlibrary.ui.viewmodel.BookViewModel


@Composable
fun AddBookScreenVisual(
    bookViewModel: BookViewModel,
    onBookSaved: () -> Unit = {}
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("")}
    var language by remember { mutableStateOf("")}
    var pageCountText by remember { mutableStateOf("")}
    var description by remember { mutableStateOf("")}
    var notes by remember { mutableStateOf("")}
    var tagInput by remember { mutableStateOf("")}
    var tags by remember { mutableStateOf(listOf<String>()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null){
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri = uri
        }
    }

    fun addCurrentTag(){
        val trimmed = tagInput.trim()
        if(trimmed.isNotEmpty()&& !tags.contains(trimmed)){
            tags += trimmed
        }
        tagInput = ""
    }

    val canSave = title.isNotBlank() && author.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.SpaceLarge, vertical = Dimens.SpaceLarge)
    ) {

        AddBookHeader(
            selectedImageUri = selectedImageUri,
            onImageClick = {
                imagePickerLaunch.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )

        LabeledField(
            label = stringResource(R.string.title),
            value = title,
            onValueChange = { title = it },
            placeholder = stringResource(R.string.enter_title)
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        LabeledField(
            label = stringResource(R.string.author),
            value = author,
            onValueChange = { author = it },
            placeholder = stringResource(R.string.enter_author)
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceMedium)) {
            Box(modifier = Modifier.weight(1f)) {
                LabeledField(
                    label = stringResource(R.string.total_pages),
                    value = pageCountText,
                    onValueChange = { new -> if (new.all { it.isDigit() }) pageCountText = new },
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


        Spacer(modifier = Modifier.height(Dimens.SpaceXLarge))

        LabeledMultilineField(
            label = stringResource(R.string.description),
            value = description,
            onValueChange = { description = it },
            placeholder = stringResource(R.string.enter_description)
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

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

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
                            modifier = Modifier.clickable{ tags = tags - tag }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))

        LabeledMultilineField(
            label = stringResource(R.string.notes),
            value = notes,
            onValueChange = { notes = it },
            placeholder = stringResource(R.string.enter_notes)
        )

        Spacer(modifier = Modifier.height(Dimens.SpaceXXXLarge))

        Button (
            onClick = {
                val pageCount = pageCountText.toIntOrNull() ?: 0
                bookViewModel.addBook(
                    book = BookEntity(
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
                onBookSaved()
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
                text = stringResource(R.string.save),
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
    }
}

@Composable
fun AddBookHeader(
    selectedImageUri: Uri?,
    onImageClick: () -> Unit
) {
    Text(
        text = stringResource(R.string.add_a_book),
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(Dimens.SpaceXXLarge))

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(Dimens.BookCoverWidthMedium)
                .height(Dimens.BookCoverHeightMedium)
                .clip(RoundedCornerShape(Dimens.CornerLarge))
                .background(MaterialTheme.colorScheme.background)
                .border(
                    Dimens.BorderThin,
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(Dimens.CornerLarge)
                )
                .clickable{onImageClick ()},
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimens.CornerLarge)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_image_placeholder),
                    contentDescription = null,
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(Dimens.SpaceMedium))
}
