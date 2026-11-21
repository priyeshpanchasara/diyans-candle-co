package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.CandleCategory
import com.example.data.ProductEntity
import com.example.ui.components.AdminCatalogPanel
import com.example.ui.components.BrandFlameEmblem
import com.example.ui.components.CandleArtworkBox
import com.example.ui.components.CartCheckoutModal
import com.example.ui.components.HeroDecorativeFlame
import com.example.ui.components.ProductDetailModal
import com.example.ui.components.WhatsAppIcon
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.Charcoal
import com.example.ui.theme.Cream
import com.example.ui.theme.HeroCinnamon
import com.example.ui.theme.HeroEspresso
import com.example.ui.theme.HeroUmber
import com.example.ui.theme.PureWhite
import com.example.ui.theme.Sand
import com.example.ui.theme.SoftErrorBg
import com.example.ui.theme.SoftErrorText
import com.example.ui.theme.Terracotta
import com.example.ui.theme.WarmGray
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppOrderHelper
import com.example.viewmodel.CandleShopViewModel
import kotlinx.coroutines.launch

@Composable
fun DiyansShopScreen(
    viewModel: CandleShopViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    // Proper Android Back Button handling for Manage mode & active filters
    BackHandler(
        enabled = state.isAdminMode || state.searchQuery.isNotEmpty() || state.selectedCategory != CandleCategory.ALL
    ) {
        when {
            state.isAdminMode -> viewModel.toggleAdminMode()
            else -> viewModel.clearFilters()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Sticky Brand Header
            StickyBrandHeader(
                isAdminMode = state.isAdminMode,
                onToggleAdmin = viewModel::toggleAdminMode
            )

            // Adaptive Responsive Grid Content
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                val columns = when {
                    maxWidth >= 900.dp -> 4
                    maxWidth >= 600.dp -> 3
                    else -> 2
                }
                val contentMaxWidth = if (maxWidth >= 900.dp) 1040.dp else 720.dp

                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 14.dp,
                        bottom = 112.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = contentMaxWidth)
                        .testTag("catalog_product_grid")
                ) {
                    // 1. Artisanal Hero Banner + Animated Inline Catalog Manager
                    item(span = { GridItemSpan(maxLineSpan) }, key = "hero_and_admin") {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ArtisanalHeroBanner(
                                onExploreClick = {
                                    coroutineScope.launch {
                                        gridState.animateScrollToItem(1)
                                    }
                                }
                            )

                            AnimatedVisibility(
                                visible = state.isAdminMode,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Spacer(Modifier.height(14.dp))
                                    AdminCatalogPanel(
                                        form = state.adminForm,
                                        businessWhatsAppNumber = state.businessWhatsAppNumber,
                                        onNameChange = viewModel::updateAdminName,
                                        onCategoryChange = viewModel::updateAdminCategory,
                                        onPriceChange = viewModel::updateAdminPrice,
                                        onArtStyleChange = viewModel::updateAdminArtStyle,
                                        onSpecChange = viewModel::updateAdminSpec,
                                        onWaxTypeChange = viewModel::updateAdminWaxType,
                                        onDescriptionChange = viewModel::updateAdminDescription,
                                        onAttachPhoto = viewModel::attachGalleryPhoto,
                                        onClearPhoto = viewModel::clearAttachedPhoto,
                                        onSaveProduct = viewModel::saveAdminProduct,
                                        onCancelEdit = viewModel::resetAdminForm,
                                        onResetDefaults = viewModel::resetDefaultProducts,
                                        onSaveWhatsAppNumber = viewModel::updateBusinessWhatsAppNumber
                                    )
                                }
                            }
                        }
                    }

                    // 3. Search Bar & Category Filter Pills
                    item(span = { GridItemSpan(maxLineSpan) }, key = "search_and_filters") {
                        SearchAndCategorySection(
                            searchQuery = state.searchQuery,
                            selectedCategory = state.selectedCategory,
                            onSearchChange = viewModel::updateSearchQuery,
                            onSelectCategory = viewModel::selectCategory
                        )
                    }

                    // 4. Catalog Heading & Item Count
                    item(span = { GridItemSpan(maxLineSpan) }, key = "catalog_heading") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (state.selectedCategory == CandleCategory.ALL) {
                                    stringResource(R.string.catalog_all_heading)
                                } else {
                                    state.selectedCategory.label
                                },
                                style = MaterialTheme.typography.headlineSmall,
                                color = Charcoal
                            )
                            val count = state.filteredProducts.size
                            Text(
                                text = "$count ${if (count == 1) "item" else "items"}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = WarmGray
                            )
                        }
                    }

                    // 5. Product Grid or Empty Filter State
                    if (state.filteredProducts.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }, key = "empty_catalog") {
                            EmptyCatalogCard(onClearFilters = viewModel::clearFilters)
                        }
                    } else {
                        items(
                            items = state.filteredProducts,
                            key = { it.id }
                        ) { product ->
                            val qtyInCart = state.cartQuantities[product.id] ?: 0
                            CandleProductCard(
                                product = product,
                                quantityInCart = qtyInCart,
                                isAdminMode = state.isAdminMode,
                                onCardClick = { viewModel.openProductDetail(product) },
                                onUpdateCart = { delta -> viewModel.updateCartItem(product.id, delta) },
                                onEditClick = {
                                    viewModel.startEditingProduct(product)
                                    coroutineScope.launch {
                                        gridState.animateScrollToItem(0)
                                    }
                                },
                                onDeleteClick = { viewModel.deleteProduct(product.id) }
                            )
                        }
                    }

                    // 6. Artisanal Brand Footer with Direct WhatsApp Contact
                    item(span = { GridItemSpan(maxLineSpan) }, key = "brand_footer") {
                        BrandFooterSection(
                            whatsappDisplay = WhatsAppOrderHelper.formatPhoneDisplay(state.businessWhatsAppNumber),
                            onWhatsAppInquiryClick = {
                                val opened = WhatsAppOrderHelper.openWhatsApp(
                                    context = context,
                                    phoneDigits = state.businessWhatsAppNumber,
                                    message = WhatsAppOrderHelper.buildInquiryMessage()
                                )
                                if (!opened) {
                                    viewModel.showToast("Could not open WhatsApp on this device")
                                }
                            }
                        )
                    }
                }
            }
        }

        // Floating Bottom Cart Bar
        FloatingCartBar(
            totalCount = state.totalCartCount,
            totalAmount = state.totalCartAmount,
            onClick = viewModel::openCartSheet,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .widthIn(max = 460.dp)
        )

        // Luxury Top Toast Notification Banner
        AnimatedVisibility(
            visible = state.toastMessage != null,
            enter = fadeIn() + slideInVertically { -it / 2 },
            exit = fadeOut() + slideOutVertically { -it / 2 },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 68.dp, start = 20.dp, end = 20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Charcoal,
                border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.4f)),
                shadowElevation = 10.dp,
                modifier = Modifier
                    .widthIn(max = 380.dp)
                    .testTag("toast_banner")
            ) {
                Text(
                    text = state.toastMessage.orEmpty(),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = Cream,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                )
            }
        }

        // Product Detail Popup Modal
        val activeProduct = state.activeDetailProduct
        if (activeProduct != null) {
            ProductDetailModal(
                product = activeProduct,
                quantity = state.detailModalQuantity,
                onQuantityChange = viewModel::changeDetailModalQuantity,
                onAddToOrder = viewModel::addDetailProductToCart,
                onDismiss = viewModel::closeProductDetail
            )
        }

        // Persistent Cart & WhatsApp Order Checkout Modal
        if (state.isCartSheetOpen) {
            CartCheckoutModal(
                lineItems = state.cartLineItems,
                totalAmount = state.totalCartAmount,
                customerDetails = state.customerDetails,
                businessWhatsAppNumber = state.businessWhatsAppNumber,
                onUpdateQuantity = viewModel::updateCartItem,
                onRemoveItem = viewModel::removeCartItem,
                onNameChange = viewModel::updateCustomerName,
                onPhoneChange = viewModel::updateCustomerPhone,
                onAddressChange = viewModel::updateCustomerAddress,
                onNoteChange = viewModel::updateCustomerNote,
                onSendWhatsAppOrder = {
                    val details = state.customerDetails
                    if (details.fullName.isBlank() || details.phoneNumber.isBlank() || details.deliveryAddress.isBlank()) {
                        viewModel.showToast("Please enter Name, Phone & Delivery Address")
                        return@CartCheckoutModal
                    }
                    viewModel.persistCustomerDetails()
                    val message = WhatsAppOrderHelper.buildOrderMessage(
                        items = state.cartLineItems,
                        customer = details
                    )
                    val launched = WhatsAppOrderHelper.openWhatsApp(
                        context = context,
                        phoneDigits = state.businessWhatsAppNumber,
                        message = message
                    )
                    if (launched) {
                        viewModel.showToast("Opening WhatsApp with your order...")
                    } else {
                        WhatsAppOrderHelper.copyToClipboard(context, "Diyan's Candle Co. Order", message)
                        viewModel.showToast("WhatsApp unavailable — order copied to clipboard!")
                    }
                },
                onCopyOrderText = {
                    val details = state.customerDetails
                    if (details.fullName.isBlank() || details.phoneNumber.isBlank() || details.deliveryAddress.isBlank()) {
                        viewModel.showToast("Please enter Name, Phone & Delivery Address")
                        return@CartCheckoutModal
                    }
                    viewModel.persistCustomerDetails()
                    val message = WhatsAppOrderHelper.buildOrderMessage(
                        items = state.cartLineItems,
                        customer = details
                    )
                    val copied = WhatsAppOrderHelper.copyToClipboard(
                        context = context,
                        label = "Diyan's Candle Co. Order",
                        text = message
                    )
                    viewModel.showToast(
                        if (copied) "Order summary copied! Paste in WhatsApp."
                        else "Unable to copy order text"
                    )
                },
                onDismiss = viewModel::closeCartSheet
            )
        }
    }
}

@Composable
private fun StickyBrandHeader(
    isAdminMode: Boolean,
    onToggleAdmin: () -> Unit
) {
    Surface(
        color = Cream.copy(alpha = 0.97f),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.statusBarsPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    BrandFlameEmblem(size = 42.dp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Charcoal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(R.string.brand_tagline_short),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = WarmGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                Surface(
                    onClick = onToggleAdmin,
                    shape = RoundedCornerShape(14.dp),
                    color = if (isAdminMode) Terracotta else Sand,
                    border = BorderStroke(
                        1.dp,
                        if (isAdminMode) Terracotta else Terracotta.copy(alpha = 0.35f)
                    ),
                    modifier = Modifier.testTag("toggle_manage_mode_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = if (isAdminMode) PureWhite else Charcoal,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = if (isAdminMode) {
                                stringResource(R.string.done_button)
                            } else {
                                stringResource(R.string.manage_button)
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isAdminMode) PureWhite else Charcoal
                        )
                    }
                }
            }
            HorizontalDivider(color = ChampagneGold.copy(alpha = 0.25f))
        }
    }
}

@Composable
private fun ArtisanalHeroBanner(
    onExploreClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(HeroEspresso, HeroUmber, HeroCinnamon)
                )
            )
            .testTag("hero_banner")
    ) {
        // Decorative Gold Flame Watermark
        HeroDecorativeFlame(
            modifier = Modifier
                .size(124.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 12.dp, y = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = ChampagneGold.copy(alpha = 0.2f)
            ) {
                Text(
                    text = stringResource(R.string.hero_badge),
                    style = MaterialTheme.typography.labelSmall,
                    color = ChampagneGold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.hero_title),
                style = MaterialTheme.typography.headlineLarge,
                color = Cream
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.hero_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = Cream.copy(alpha = 0.84f)
            )

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = onExploreClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terracotta,
                    contentColor = PureWhite
                ),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                modifier = Modifier.testTag("explore_collection_button")
            ) {
                Text(
                    text = stringResource(R.string.hero_cta),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun SearchAndCategorySection(
    searchQuery: String,
    selectedCategory: CandleCategory,
    onSearchChange: (String) -> Unit,
    onSelectCategory: (CandleCategory) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.search_placeholder),
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmGray.copy(alpha = 0.72f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = WarmGray,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.cd_clear_search),
                            tint = WarmGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = PureWhite,
                unfocusedContainerColor = PureWhite,
                focusedBorderColor = Terracotta,
                unfocusedBorderColor = ChampagneGold.copy(alpha = 0.35f),
                cursorColor = Terracotta,
                focusedTextColor = Charcoal,
                unfocusedTextColor = Charcoal
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("product_search_input")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CandleCategory.filterCategories.forEach { category ->
                val isSelected = selectedCategory == category
                Surface(
                    onClick = { onSelectCategory(category) },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) Terracotta else PureWhite,
                    border = if (isSelected) null else BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.35f)),
                    shadowElevation = if (isSelected) 2.dp else 0.dp,
                    modifier = Modifier.testTag("category_pill_${category.name.lowercase()}")
                ) {
                    Text(
                        text = category.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) PureWhite else WarmGray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CandleProductCard(
    product: ProductEntity,
    quantityInCart: Int,
    isAdminMode: Boolean,
    onCardClick: () -> Unit,
    onUpdateCart: (Int) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.28f)),
        shadowElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Clickable visual + info area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCardClick)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(142.dp)
                ) {
                    CandleArtworkBox(
                        artStyleKey = product.artStyle,
                        customImagePath = product.customImagePath,
                        contentDescription = product.name,
                        artworkSize = 112.dp,
                        modifier = Modifier.fillMaxSize()
                    )

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = PureWhite.copy(alpha = 0.92f),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = product.category,
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.sp),
                            color = Charcoal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 6.dp)
                ) {
                    Text(
                        text = product.spec.ifBlank { "Pure Soy • Cotton Wick" },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.sp
                        ),
                        color = WarmGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 17.sp,
                            lineHeight = 21.sp
                        ),
                        color = Charcoal,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Price & Add / Stepper Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = WhatsAppOrderHelper.formatRupees(product.price),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Terracotta
                    )

                    if (quantityInCart > 0) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Sand,
                            border = BorderStroke(1.dp, Terracotta.copy(alpha = 0.35f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onUpdateCart(-1) }
                                        .testTag("card_minus_${product.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = stringResource(R.string.cd_decrease_qty),
                                        tint = Charcoal,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                                Text(
                                    text = quantityInCart.toString(),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Charcoal,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onUpdateCart(1) }
                                        .testTag("card_plus_${product.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(R.string.cd_increase_qty),
                                        tint = Charcoal,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Surface(
                            onClick = { onUpdateCart(1) },
                            shape = RoundedCornerShape(12.dp),
                            color = Charcoal,
                            modifier = Modifier.testTag("card_add_${product.id}")
                        ) {
                            Text(
                                text = stringResource(R.string.add_button),
                                style = MaterialTheme.typography.labelMedium,
                                color = PureWhite,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                // Admin Quick Edit / Delete Row
                if (isAdminMode) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Sand
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            onClick = onEditClick,
                            shape = RoundedCornerShape(10.dp),
                            color = ChampagneGold.copy(alpha = 0.24f),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_edit_${product.id}")
                        ) {
                            Text(
                                text = stringResource(R.string.edit_button),
                                style = MaterialTheme.typography.labelSmall,
                                color = Charcoal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 7.dp)
                            )
                        }
                        Surface(
                            onClick = onDeleteClick,
                            shape = RoundedCornerShape(10.dp),
                            color = SoftErrorBg,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_delete_${product.id}")
                        ) {
                            Text(
                                text = stringResource(R.string.delete_button),
                                style = MaterialTheme.typography.labelSmall,
                                color = SoftErrorText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyCatalogCard(onClearFilters: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.empty_catalog_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Charcoal
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.empty_catalog_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = WarmGray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onClearFilters,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Terracotta,
                    contentColor = PureWhite
                ),
                modifier = Modifier.testTag("show_all_candles_button")
            ) {
                Text(
                    text = stringResource(R.string.show_all_candles),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun BrandFooterSection(
    whatsappDisplay: String,
    onWhatsAppInquiryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(color = ChampagneGold.copy(alpha = 0.28f))
        Spacer(Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            color = Charcoal
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(R.string.brand_tagline),
            style = MaterialTheme.typography.bodySmall,
            color = WarmGray
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.footer_note),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
            color = WarmGray.copy(alpha = 0.85f),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Surface(
            onClick = onWhatsAppInquiryClick,
            shape = RoundedCornerShape(50),
            color = PureWhite,
            border = BorderStroke(1.dp, WhatsAppGreen.copy(alpha = 0.45f)),
            modifier = Modifier.testTag("footer_whatsapp_inquiry_button")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                WhatsAppIcon(color = WhatsAppGreen, modifier = Modifier.size(16.dp))
                Text(
                    text = "${stringResource(R.string.footer_whatsapp_inquiry)} • $whatsappDisplay",
                    style = MaterialTheme.typography.labelMedium,
                    color = Charcoal
                )
            }
        }
    }
}

@Composable
private fun FloatingCartBar(
    totalCount: Int,
    totalAmount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Charcoal,
        border = BorderStroke(1.dp, ChampagneGold.copy(alpha = 0.4f)),
        shadowElevation = 12.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("floating_cart_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Terracotta,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = totalCount.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = PureWhite,
                            modifier = Modifier.testTag("cart_badge_count")
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.cart_bar_title),
                        style = MaterialTheme.typography.labelLarge,
                        color = Cream,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (totalCount > 0) {
                            "$totalCount ${if (totalCount == 1) "candle" else "candles"} selected • Tap to order"
                        } else {
                            stringResource(R.string.cart_bar_subtitle_empty)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = ChampagneGold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = WhatsAppOrderHelper.formatRupees(totalAmount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = PureWhite,
                    modifier = Modifier.testTag("cart_bar_total")
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = ChampagneGold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
