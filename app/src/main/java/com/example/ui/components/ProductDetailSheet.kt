package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ProductEntity
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.Charcoal
import com.example.ui.theme.Cream
import com.example.ui.theme.PureWhite
import com.example.ui.theme.Sand
import com.example.ui.theme.Terracotta
import com.example.ui.theme.WarmGray
import com.example.util.WhatsAppOrderHelper

@Composable
fun ProductDetailModal(
    product: ProductEntity,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToOrder: () -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

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
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
                .testTag("product_detail_modal"),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Cream,
            shadowElevation = 18.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                // Hero Artwork Header with Floating Close Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(228.dp)
                ) {
                    CandleArtworkBox(
                        artStyleKey = product.artStyle,
                        customImagePath = product.customImagePath,
                        contentDescription = product.name,
                        artworkSize = 176.dp,
                        modifier = Modifier.fillMaxSize()
                    )

                    Surface(
                        onClick = onDismiss,
                        shape = CircleShape,
                        color = PureWhite.copy(alpha = 0.92f),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(14.dp)
                            .size(40.dp)
                            .testTag("close_product_detail_button")
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

                // Scrollable Details Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Sand
                        ) {
                            Text(
                                text = product.category,
                                style = MaterialTheme.typography.labelMedium,
                                color = Terracotta,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                        Text(
                            text = product.spec.ifBlank { "Pure Soy • Cotton Wick" },
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmGray
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Charcoal
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = WhatsAppOrderHelper.formatRupees(product.price),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Terracotta
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmGray
                    )

                    Spacer(Modifier.height(16.dp))

                    // Artisanal Highlights (Wax Blend & Wick Type)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ArtisanSpecCard(
                            label = stringResource(R.string.wax_blend_label),
                            value = product.waxType.ifBlank { "100% Pure Soy Wax" },
                            modifier = Modifier.weight(1f)
                        )
                        ArtisanSpecCard(
                            label = stringResource(R.string.wick_type_label),
                            value = stringResource(R.string.wick_type_default),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // Quantity Stepper & Add to Order Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = PureWhite,
                            border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.45f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                IconButton(
                                    onClick = { onQuantityChange(-1) },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .testTag("detail_qty_minus_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = stringResource(R.string.cd_decrease_qty),
                                        tint = Charcoal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = quantity.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Charcoal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .widthIn(min = 32.dp)
                                        .testTag("detail_qty_value")
                                )
                                IconButton(
                                    onClick = { onQuantityChange(1) },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .testTag("detail_qty_plus_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(R.string.cd_increase_qty),
                                        tint = Charcoal,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = onAddToOrder,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Terracotta,
                                contentColor = PureWhite
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 15.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("detail_add_to_order_button")
                        ) {
                            Text(
                                text = "Add to Order • ${WhatsAppOrderHelper.formatRupees(product.price * quantity)}",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArtisanSpecCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = WarmGray
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = Charcoal
            )
        }
    }
}
