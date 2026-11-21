package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.CandleArtStyle
import com.example.data.CandleCategory
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.Charcoal
import com.example.ui.theme.Cream
import com.example.ui.theme.PureWhite
import com.example.ui.theme.Sand
import com.example.ui.theme.Terracotta
import com.example.ui.theme.WarmGray
import com.example.util.WhatsAppOrderHelper
import com.example.viewmodel.AdminProductFormState

@Composable
fun AdminCatalogPanel(
    form: AdminProductFormState,
    businessWhatsAppNumber: String,
    onNameChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onArtStyleChange: (String) -> Unit,
    onSpecChange: (String) -> Unit,
    onWaxTypeChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAttachPhoto: (android.net.Uri) -> Unit,
    onClearPhoto: () -> Unit,
    onSaveProduct: () -> Unit,
    onCancelEdit: () -> Unit,
    onResetDefaults: () -> Unit,
    onSaveWhatsAppNumber: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showResetConfirm by remember { mutableStateOf(false) }
    var whatsappInput by remember { mutableStateOf(businessWhatsAppNumber) }

    LaunchedEffect(businessWhatsAppNumber) {
        whatsappInput = businessWhatsAppNumber
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onAttachPhoto(uri)
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            containerColor = Cream,
            title = {
                Text(
                    text = stringResource(R.string.reset_defaults_button),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Charcoal
                )
            },
            text = {
                Text(
                    text = "Restore the original 6 Diyan's Candle Co. products and prices?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetConfirm = false
                        onResetDefaults()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                    modifier = Modifier.testTag("confirm_reset_defaults_button")
                ) {
                    Text("Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text(stringResource(R.string.cancel_button), color = Charcoal)
                }
            }
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("admin_catalog_panel"),
        shape = RoundedCornerShape(24.dp),
        color = PureWhite,
        border = BorderStroke(1.5.dp, Terracotta.copy(alpha = 0.45f)),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (form.isEditing) "Edit Candle" else stringResource(R.string.catalog_manager_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Charcoal
                    )
                    Text(
                        text = stringResource(R.string.catalog_manager_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGray
                    )
                }
                TextButton(
                    onClick = { showResetConfirm = true },
                    modifier = Modifier.testTag("reset_defaults_button"),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Restore,
                        contentDescription = null,
                        tint = Terracotta,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.reset_defaults_button),
                        style = MaterialTheme.typography.labelMedium,
                        color = Terracotta,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Sand
            )

            // Candle Name
            AdminFieldLabel(stringResource(R.string.candle_name_label))
            OutlinedTextField(
                value = form.name,
                onValueChange = onNameChange,
                placeholder = { Text(stringResource(R.string.candle_name_hint), color = WarmGray.copy(alpha = 0.65f)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = adminTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_candle_name_input")
            )

            Spacer(Modifier.height(12.dp))

            // Category Pills
            AdminFieldLabel(stringResource(R.string.category_label))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CandleCategory.editableCategories.forEach { cat ->
                    val selected = form.category.equals(cat.label, ignoreCase = true)
                    Surface(
                        onClick = { onCategoryChange(cat.label) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (selected) Terracotta else Cream,
                        border = BorderStroke(
                            1.dp,
                            if (selected) Terracotta else ChampagneGold.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier.testTag("admin_cat_${cat.name.lowercase()}")
                    ) {
                        Text(
                            text = cat.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selected) PureWhite else Charcoal,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Price & Spec Badge row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(0.42f)) {
                    AdminFieldLabel(stringResource(R.string.price_label))
                    OutlinedTextField(
                        value = form.priceText,
                        onValueChange = onPriceChange,
                        placeholder = { Text(stringResource(R.string.price_hint), color = WarmGray.copy(alpha = 0.65f)) },
                        prefix = { Text("₹", color = Terracotta, fontWeight = FontWeight.Bold) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        colors = adminTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_price_input")
                    )
                }
                Column(modifier = Modifier.weight(0.58f)) {
                    AdminFieldLabel(stringResource(R.string.spec_badge_label))
                    OutlinedTextField(
                        value = form.spec,
                        onValueChange = onSpecChange,
                        placeholder = { Text("Pure Soy • Cotton Wick", color = WarmGray.copy(alpha = 0.65f)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = adminTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_spec_input")
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Wax Blend
            AdminFieldLabel(stringResource(R.string.wax_type_input_label))
            OutlinedTextField(
                value = form.waxType,
                onValueChange = onWaxTypeChange,
                placeholder = { Text("100% Pure Soy Wax", color = WarmGray.copy(alpha = 0.65f)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = adminTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_wax_input")
            )

            Spacer(Modifier.height(12.dp))

            // Candle Design Visual Picker (all 7 bespoke illustrations)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AdminFieldLabel(stringResource(R.string.art_style_label))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (!form.customImagePath.isNullOrBlank()) {
                        TextButton(
                            onClick = onClearPhoto,
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null,
                                tint = WarmGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = stringResource(R.string.remove_gallery_photo),
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmGray
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.testTag("admin_pick_photo_button"),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddPhotoAlternate,
                            contentDescription = null,
                            tint = Terracotta,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (form.customImagePath.isNullOrBlank()) {
                                stringResource(R.string.pick_gallery_photo)
                            } else {
                                stringResource(R.string.change_gallery_photo)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = Terracotta
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CandleArtStyle.entries.forEach { style ->
                    val isSelected = form.artStyle.equals(style.key, ignoreCase = true) &&
                        form.customImagePath.isNullOrBlank()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(74.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Terracotta else ChampagneGold.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .background(if (isSelected) Sand else Cream)
                            .clickable {
                                onClearPhoto()
                                onArtStyleChange(style.key)
                            }
                            .padding(6.dp)
                            .testTag("admin_art_${style.key}")
                    ) {
                        CandleArtworkBox(
                            artStyleKey = style.key,
                            contentDescription = style.displayName,
                            artworkSize = 44.dp,
                            animateFlame = false,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = style.displayName.substringBefore(" "),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Terracotta else Charcoal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Description
            AdminFieldLabel(stringResource(R.string.description_label))
            OutlinedTextField(
                value = form.description,
                onValueChange = onDescriptionChange,
                placeholder = { Text(stringResource(R.string.description_hint), color = WarmGray.copy(alpha = 0.65f)) },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(14.dp),
                colors = adminTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_desc_input")
            )

            Spacer(Modifier.height(14.dp))

            // Submit / Cancel buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSaveProduct,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = PureWhite
                    ),
                    contentPadding = PaddingValues(vertical = 13.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_save_product_button")
                ) {
                    Text(
                        text = if (form.isEditing) {
                            stringResource(R.string.save_changes_submit)
                        } else {
                            stringResource(R.string.add_candle_submit)
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                if (form.isEditing) {
                    OutlinedButton(
                        onClick = onCancelEdit,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Sand,
                            contentColor = Charcoal
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 13.dp),
                        modifier = Modifier.testTag("admin_cancel_edit_button")
                    ) {
                        Text(
                            text = stringResource(R.string.cancel_button),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 14.dp),
                color = Sand
            )

            // Editable Business WhatsApp Number
            AdminFieldLabel(
                "${stringResource(R.string.whatsapp_settings_title)} (${WhatsAppOrderHelper.formatPhoneDisplay(businessWhatsAppNumber)})"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = whatsappInput,
                    onValueChange = { whatsappInput = it.filter { ch -> ch.isDigit() || ch == '+' || ch == ' ' } },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            tint = WarmGray,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(14.dp),
                    colors = adminTextFieldColors(),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("admin_whatsapp_number_input")
                )
                Button(
                    onClick = { onSaveWhatsAppNumber(whatsappInput) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Charcoal,
                        contentColor = Cream
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                    modifier = Modifier.testTag("admin_save_whatsapp_button")
                ) {
                    Text(
                        text = stringResource(R.string.save_whatsapp_button),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminFieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = WarmGray,
        modifier = Modifier.padding(bottom = 5.dp)
    )
}

@Composable
private fun adminTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Cream,
    unfocusedContainerColor = Cream,
    focusedBorderColor = Terracotta,
    unfocusedBorderColor = ChampagneGold.copy(alpha = 0.4f),
    cursorColor = Terracotta,
    focusedTextColor = Charcoal,
    unfocusedTextColor = Charcoal
)
