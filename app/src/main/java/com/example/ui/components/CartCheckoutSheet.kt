package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CustomerCheckoutDetails
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.Charcoal
import com.example.ui.theme.Cream
import com.example.ui.theme.EmeraldWrap
import com.example.ui.theme.PureWhite
import com.example.ui.theme.Sand
import com.example.ui.theme.SoftErrorText
import com.example.ui.theme.Terracotta
import com.example.ui.theme.WarmGray
import com.example.ui.theme.WhatsAppGreen
import com.example.util.CartLineItem
import com.example.util.WhatsAppOrderHelper

@Composable
fun CartCheckoutModal(
    lineItems: List<CartLineItem>,
    totalAmount: Int,
    customerDetails: CustomerCheckoutDetails,
    businessWhatsAppNumber: String,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSendWhatsAppOrder: () -> Unit,
    onCopyOrderText: () -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)
    var showPreview by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Charcoal.copy(alpha = 0.62f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .fillMaxHeight(0.92f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
                .testTag("cart_checkout_modal"),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Cream,
            shadowElevation = 20.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                // Header
                Surface(
                    color = PureWhite,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.cart_bar_title),
                                style = MaterialTheme.typography.headlineMedium,
                                color = Charcoal
                            )
                            Text(
                                text = stringResource(R.string.cart_sheet_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmGray
                            )
                        }
                        Surface(
                            onClick = onDismiss,
                            shape = CircleShape,
                            color = Sand,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("close_cart_modal_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.cd_close_modal),
                                    tint = Charcoal,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (lineItems.isEmpty()) {
                        EmptyCartCard(onBrowseClick = onDismiss)
                    } else {
                        // Cart Items List
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            lineItems.forEach { entry ->
                                CartLineItemCard(
                                    entry = entry,
                                    onDecrease = { onUpdateQuantity(entry.product.id, -1) },
                                    onIncrease = { onUpdateQuantity(entry.product.id, 1) },
                                    onRemove = { onRemoveItem(entry.product.id) }
                                )
                            }
                        }

                        // Order Summary Card
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = PureWhite,
                            border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.35f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.subtotal_label),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = WarmGray
                                    )
                                    Text(
                                        text = WhatsAppOrderHelper.formatRupees(totalAmount),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = Charcoal
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.packaging_label),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = WarmGray
                                    )
                                    Text(
                                        text = stringResource(R.string.packaging_value),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = EmeraldWrap
                                    )
                                }
                                HorizontalDivider(color = Sand, modifier = Modifier.padding(vertical = 2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.estimated_total_label),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Charcoal
                                    )
                                    Text(
                                        text = WhatsAppOrderHelper.formatRupees(totalAmount),
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Terracotta,
                                        modifier = Modifier.testTag("cart_summary_total")
                                    )
                                }
                            }
                        }

                        // Customer Checkout Form
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = stringResource(R.string.delivery_details_heading),
                                style = MaterialTheme.typography.headlineSmall,
                                color = Charcoal
                            )

                            CheckoutTextField(
                                label = stringResource(R.string.customer_name_label),
                                value = customerDetails.fullName,
                                onValueChange = onNameChange,
                                placeholder = stringResource(R.string.customer_name_hint),
                                testTag = "checkout_name_input"
                            )

                            CheckoutTextField(
                                label = stringResource(R.string.customer_phone_label),
                                value = customerDetails.phoneNumber,
                                onValueChange = onPhoneChange,
                                placeholder = stringResource(R.string.customer_phone_hint),
                                keyboardType = KeyboardType.Phone,
                                testTag = "checkout_phone_input"
                            )

                            CheckoutTextField(
                                label = stringResource(R.string.customer_address_label),
                                value = customerDetails.deliveryAddress,
                                onValueChange = onAddressChange,
                                placeholder = stringResource(R.string.customer_address_hint),
                                minLines = 2,
                                maxLines = 3,
                                singleLine = false,
                                testTag = "checkout_address_input"
                            )

                            CheckoutTextField(
                                label = stringResource(R.string.customer_note_label),
                                value = customerDetails.customNote,
                                onValueChange = onNoteChange,
                                placeholder = stringResource(R.string.customer_note_hint),
                                testTag = "checkout_note_input"
                            )
                        }

                        // WhatsApp Recipient Info + Toggleable Message Preview
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sending to ${WhatsAppOrderHelper.formatPhoneDisplay(businessWhatsAppNumber)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmGray
                            )
                            TextButton(
                                onClick = { showPreview = !showPreview },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ReceiptLong,
                                    contentDescription = null,
                                    tint = Terracotta,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = if (showPreview) {
                                        stringResource(R.string.hide_preview_text)
                                    } else {
                                        stringResource(R.string.preview_order_text)
                                    },
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Terracotta
                                )
                            }
                        }

                        AnimatedVisibility(visible = showPreview) {
                            val previewCustomer = CustomerCheckoutDetails(
                                fullName = customerDetails.fullName.ifBlank { "[Your Name]" },
                                phoneNumber = customerDetails.phoneNumber.ifBlank { "[Your Phone]" },
                                deliveryAddress = customerDetails.deliveryAddress.ifBlank { "[Delivery Address]" },
                                customNote = customerDetails.customNote
                            )
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Sand.copy(alpha = 0.7f),
                                border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.35f))
                            ) {
                                Text(
                                    text = WhatsAppOrderHelper.buildOrderMessage(lineItems, previewCustomer),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        lineHeight = 17.sp
                                    ),
                                    color = Charcoal,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp)
                                )
                            }
                        }
                    }
                }

                // Sticky Bottom WhatsApp Checkout Actions
                if (lineItems.isNotEmpty()) {
                    Surface(
                        color = PureWhite,
                        shadowElevation = 10.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(horizontal = 18.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onSendWhatsAppOrder,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WhatsAppGreen,
                                    contentColor = PureWhite
                                ),
                                contentPadding = PaddingValues(vertical = 14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("send_whatsapp_order_button")
                            ) {
                                WhatsAppIcon(
                                    color = PureWhite,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.send_whatsapp_cta),
                                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                                )
                            }

                            Surface(
                                onClick = onCopyOrderText,
                                shape = RoundedCornerShape(14.dp),
                                color = Sand.copy(alpha = 0.85f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("copy_order_text_button")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 11.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ContentCopy,
                                        contentDescription = null,
                                        tint = WarmGray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.copy_order_fallback),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Charcoal
                                    )
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
private fun EmptyCartCard(onBrowseClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BrandFlameEmblem(size = 52.dp)
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.empty_cart_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Charcoal,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.empty_cart_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = WarmGray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onBrowseClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terracotta,
                    contentColor = PureWhite
                ),
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 12.dp),
                modifier = Modifier.testTag("empty_cart_browse_button")
            ) {
                Text(
                    text = stringResource(R.string.browse_candles),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun CartLineItemCard(
    entry: CartLineItem,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cart_item_${entry.product.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CandleArtworkBox(
                artStyleKey = entry.product.artStyle,
                customImagePath = entry.product.customImagePath,
                contentDescription = entry.product.name,
                artworkSize = 46.dp,
                animateFlame = false,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.product.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                    color = Charcoal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${WhatsAppOrderHelper.formatRupees(entry.product.price)} × ${entry.quantity} = ",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarmGray
                    )
                    Text(
                        text = WhatsAppOrderHelper.formatRupees(entry.lineTotal),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Terracotta
                    )
                }
            }

            // Stepper
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Sand
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(2.dp)
                ) {
                    IconButton(
                        onClick = onDecrease,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("cart_minus_${entry.product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = stringResource(R.string.cd_decrease_qty),
                            tint = Charcoal,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = entry.quantity.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = Charcoal,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    IconButton(
                        onClick = onIncrease,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("cart_plus_${entry.product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.cd_increase_qty),
                            tint = Charcoal,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .size(34.dp)
                    .testTag("cart_remove_${entry.product.id}")
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = stringResource(R.string.cd_remove_item),
                    tint = SoftErrorText.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun CheckoutTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    testTag: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = WarmGray,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmGray.copy(alpha = 0.65f)
                )
            },
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = PureWhite,
                unfocusedContainerColor = PureWhite,
                focusedBorderColor = Terracotta,
                unfocusedBorderColor = ChampagneGold.copy(alpha = 0.42f),
                cursorColor = Terracotta,
                focusedTextColor = Charcoal,
                unfocusedTextColor = Charcoal
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
        )
    }
}

@Composable
fun WhatsAppIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val s = minOf(size.width, size.height) / 24f
        // Chat bubble circle
        drawCircle(
            color = color,
            radius = 8.6f * s,
            center = Offset(12.2f * s, 11.6f * s),
            style = Stroke(width = 2.1f * s)
        )
        // Speech tail bottom-left
        val tail = Path().apply {
            moveTo(6.2f * s, 17.5f * s)
            lineTo(4.2f * s, 21f * s)
            lineTo(8.5f * s, 19.6f * s)
        }
        drawPath(tail, color = color, style = Stroke(width = 2f * s, cap = StrokeCap.Round))
        // Stylized phone handset curve inside
        val phone = Path().apply {
            moveTo(9.4f * s, 8.8f * s)
            cubicTo(8.9f * s, 10.2f * s, 10.6f * s, 13.3f * s, 13.7f * s, 14.5f * s)
            cubicTo(14.8f * s, 14.9f * s, 15.6f * s, 14.1f * s, 15.8f * s, 13.4f * s)
        }
        drawPath(phone, color = color, style = Stroke(width = 2.1f * s, cap = StrokeCap.Round))
    }
}
