package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CandleArtStyle
import com.example.data.CandleCategory
import com.example.data.CandleShopRepository
import com.example.data.CustomerCheckoutDetails
import com.example.data.DefaultCatalogProvider
import com.example.data.ImageStorageHelper
import com.example.data.ProductEntity
import com.example.data.StorePreferencesRepository
import com.example.util.CartLineItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminProductFormState(
    val editingProductId: String? = null,
    val name: String = "",
    val category: String = CandleCategory.HEART_AND_SHELL.label,
    val priceText: String = "",
    val artStyle: String = CandleArtStyle.HEART.key,
    val spec: String = "Pure Soy • Cotton Wick",
    val waxType: String = "100% Pure Soy Wax",
    val description: String = "",
    val customImagePath: String? = null
) {
    val isEditing: Boolean get() = editingProductId != null
}

data class CandleShopUiState(
    val allProducts: List<ProductEntity> = DefaultCatalogProvider.defaultProducts(),
    val filteredProducts: List<ProductEntity> = DefaultCatalogProvider.defaultProducts(),
    val cartQuantities: Map<String, Int> = emptyMap(),
    val cartLineItems: List<CartLineItem> = emptyList(),
    val totalCartCount: Int = 0,
    val totalCartAmount: Int = 0,
    val selectedCategory: CandleCategory = CandleCategory.ALL,
    val searchQuery: String = "",
    val isAdminMode: Boolean = false,
    val adminForm: AdminProductFormState = AdminProductFormState(),
    val activeDetailProduct: ProductEntity? = null,
    val detailModalQuantity: Int = 1,
    val isCartSheetOpen: Boolean = false,
    val customerDetails: CustomerCheckoutDetails = CustomerCheckoutDetails(),
    val businessWhatsAppNumber: String = DefaultCatalogProvider.DEFAULT_WHATSAPP_NUMBER,
    val toastMessage: String? = null
)

private data class TransientUiState(
    val selectedCategory: CandleCategory = CandleCategory.ALL,
    val searchQuery: String = "",
    val isAdminMode: Boolean = false,
    val adminForm: AdminProductFormState = AdminProductFormState(),
    val activeDetailProductId: String? = null,
    val detailModalQuantity: Int = 1,
    val isCartSheetOpen: Boolean = false,
    val toastMessage: String? = null
)

class CandleShopViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val preferences = StorePreferencesRepository(application)
    private val repository = CandleShopRepository(database.candleDao(), preferences)

    private val transientState = MutableStateFlow(TransientUiState())
    private val customerFormState = MutableStateFlow(CustomerCheckoutDetails())
    private var toastDismissJob: Job? = null

    init {
        viewModelScope.launch {
            repository.ensureDefaultCatalogSeeded()
        }
        viewModelScope.launch {
            val savedCustomer = repository.savedCustomerDetails.first()
            customerFormState.value = savedCustomer
        }
    }

    val uiState: StateFlow<CandleShopUiState> = combine(
        repository.products,
        repository.cartItems,
        repository.businessWhatsAppNumber,
        customerFormState,
        transientState
    ) { dbProducts, dbCartItems, whatsappNumber, customer, ui ->
        val products = dbProducts
        val cartMap = dbCartItems.associate { it.productId to it.quantity }
        val productById = products.associateBy { it.id }

        val lineItems = dbCartItems.mapNotNull { entry ->
            val prod = productById[entry.productId]
            if (prod != null && entry.quantity > 0) {
                CartLineItem(product = prod, quantity = entry.quantity)
            } else {
                null
            }
        }

        val query = ui.searchQuery.trim().lowercase()
        val filtered = products.filter { item ->
            val matchesCategory = ui.selectedCategory == CandleCategory.ALL ||
                item.category.equals(ui.selectedCategory.label, ignoreCase = true)
            val matchesSearch = query.isEmpty() ||
                item.name.lowercase().contains(query) ||
                item.description.lowercase().contains(query) ||
                item.category.lowercase().contains(query) ||
                item.spec.lowercase().contains(query)
            matchesCategory && matchesSearch
        }

        val detailProduct = ui.activeDetailProductId?.let { productById[it] }

        CandleShopUiState(
            allProducts = products,
            filteredProducts = filtered,
            cartQuantities = cartMap,
            cartLineItems = lineItems,
            totalCartCount = lineItems.sumOf { it.quantity },
            totalCartAmount = lineItems.sumOf { it.lineTotal },
            selectedCategory = ui.selectedCategory,
            searchQuery = ui.searchQuery,
            isAdminMode = ui.isAdminMode,
            adminForm = ui.adminForm,
            activeDetailProduct = detailProduct,
            detailModalQuantity = ui.detailModalQuantity,
            isCartSheetOpen = ui.isCartSheetOpen,
            customerDetails = customer,
            businessWhatsAppNumber = whatsappNumber,
            toastMessage = ui.toastMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CandleShopUiState()
    )

    // --- Toast Feedback ---
    fun showToast(message: String) {
        transientState.update { it.copy(toastMessage = message) }
        toastDismissJob?.cancel()
        toastDismissJob = viewModelScope.launch {
            delay(2600L)
            transientState.update { it.copy(toastMessage = null) }
        }
    }

    fun dismissToast() {
        toastDismissJob?.cancel()
        transientState.update { it.copy(toastMessage = null) }
    }

    // --- Catalog Filtering ---
    fun selectCategory(category: CandleCategory) {
        transientState.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchQuery(query: String) {
        transientState.update { it.copy(searchQuery = query) }
    }

    fun clearFilters() {
        transientState.update {
            it.copy(
                selectedCategory = CandleCategory.ALL,
                searchQuery = ""
            )
        }
    }

    // --- Product Detail Modal ---
    fun openProductDetail(product: ProductEntity) {
        transientState.update {
            it.copy(
                activeDetailProductId = product.id,
                detailModalQuantity = 1
            )
        }
    }

    fun closeProductDetail() {
        transientState.update { it.copy(activeDetailProductId = null, detailModalQuantity = 1) }
    }

    fun changeDetailModalQuantity(delta: Int) {
        transientState.update {
            it.copy(detailModalQuantity = (it.detailModalQuantity + delta).coerceAtLeast(1))
        }
    }

    fun addDetailProductToCart() {
        val currentState = uiState.value
        val product = currentState.activeDetailProduct ?: return
        val qtyToAdd = currentState.detailModalQuantity
        updateCartItem(product.id, qtyToAdd)
        closeProductDetail()
    }

    // --- Cart Management ---
    fun updateCartItem(productId: String, delta: Int) {
        val product = uiState.value.allProducts.firstOrNull { it.id == productId } ?: return
        viewModelScope.launch {
            val nextQty = repository.updateCartDelta(productId, delta)
            if (nextQty <= 0) {
                showToast("Removed ${product.name}")
            } else if (delta > 0) {
                showToast("Added ${product.name} ($nextQty)")
            }
        }
    }

    fun removeCartItem(productId: String) {
        val product = uiState.value.allProducts.firstOrNull { it.id == productId }
        viewModelScope.launch {
            repository.removeCartItem(productId)
            if (product != null) {
                showToast("Removed ${product.name}")
            }
        }
    }

    fun openCartSheet() {
        transientState.update { it.copy(isCartSheetOpen = true) }
    }

    fun closeCartSheet() {
        transientState.update { it.copy(isCartSheetOpen = false) }
    }

    // --- Customer Checkout Details ---
    fun updateCustomerName(value: String) {
        customerFormState.update { it.copy(fullName = value) }
    }

    fun updateCustomerPhone(value: String) {
        customerFormState.update { it.copy(phoneNumber = value) }
    }

    fun updateCustomerAddress(value: String) {
        customerFormState.update { it.copy(deliveryAddress = value) }
    }

    fun updateCustomerNote(value: String) {
        customerFormState.update { it.copy(customNote = value) }
    }

    fun persistCustomerDetails() {
        val current = customerFormState.value
        viewModelScope.launch {
            repository.saveCustomerDetails(current)
        }
    }

    // --- Admin / Catalog Manager ---
    fun toggleAdminMode() {
        val next = !transientState.value.isAdminMode
        transientState.update {
            it.copy(
                isAdminMode = next,
                adminForm = if (!next) AdminProductFormState() else it.adminForm
            )
        }
        showToast(if (next) "Catalog Manager Enabled" else "Catalog Manager Closed")
    }

    fun updateAdminName(value: String) {
        transientState.update { it.copy(adminForm = it.adminForm.copy(name = value)) }
    }

    fun updateAdminCategory(category: String) {
        transientState.update { it.copy(adminForm = it.adminForm.copy(category = category)) }
    }

    fun updateAdminPrice(value: String) {
        val digitsOnly = value.filter { it.isDigit() }
        transientState.update { it.copy(adminForm = it.adminForm.copy(priceText = digitsOnly)) }
    }

    fun updateAdminArtStyle(artKey: String) {
        transientState.update { it.copy(adminForm = it.adminForm.copy(artStyle = artKey)) }
    }

    fun updateAdminSpec(value: String) {
        transientState.update { it.copy(adminForm = it.adminForm.copy(spec = value)) }
    }

    fun updateAdminWaxType(value: String) {
        transientState.update { it.copy(adminForm = it.adminForm.copy(waxType = value)) }
    }

    fun updateAdminDescription(value: String) {
        transientState.update { it.copy(adminForm = it.adminForm.copy(description = value)) }
    }

    fun attachGalleryPhoto(uri: Uri) {
        viewModelScope.launch {
            val savedPath = ImageStorageHelper.copyPickedImageToInternalStorage(
                getApplication(),
                uri
            )
            if (savedPath != null) {
                transientState.update {
                    it.copy(adminForm = it.adminForm.copy(customImagePath = savedPath))
                }
                showToast("Candle photo attached")
            } else {
                showToast("Could not load selected photo")
            }
        }
    }

    fun clearAttachedPhoto() {
        transientState.update {
            it.copy(adminForm = it.adminForm.copy(customImagePath = null))
        }
    }

    fun startEditingProduct(product: ProductEntity) {
        transientState.update {
            it.copy(
                isAdminMode = true,
                adminForm = AdminProductFormState(
                    editingProductId = product.id,
                    name = product.name,
                    category = product.category,
                    priceText = product.price.toString(),
                    artStyle = product.artStyle,
                    spec = product.spec,
                    waxType = product.waxType,
                    description = product.description,
                    customImagePath = product.customImagePath
                )
            )
        }
    }

    fun resetAdminForm() {
        transientState.update { it.copy(adminForm = AdminProductFormState()) }
    }

    fun saveAdminProduct() {
        val form = transientState.value.adminForm
        val trimmedName = form.name.trim()
        val parsedPrice = form.priceText.toIntOrNull()
        val trimmedDesc = form.description.trim()

        if (trimmedName.isBlank()) {
            showToast("Please enter a candle name")
            return
        }
        if (parsedPrice == null || parsedPrice <= 0) {
            showToast("Please enter a valid price in ₹")
            return
        }
        if (trimmedDesc.isBlank()) {
            showToast("Please enter a candle description")
            return
        }

        val existing = form.editingProductId?.let { id ->
            uiState.value.allProducts.firstOrNull { it.id == id }
        }
        val minSortOrder = (uiState.value.allProducts.minOfOrNull { it.sortOrder } ?: 1L) - 1L

        val entity = ProductEntity(
            id = existing?.id ?: "candle-${System.currentTimeMillis()}",
            name = trimmedName,
            category = form.category.ifBlank { CandleCategory.JAR_CANDLES.label },
            price = parsedPrice,
            artStyle = form.artStyle.ifBlank { CandleArtStyle.JAR.key },
            spec = form.spec.trim().ifBlank { "Pure Soy • Cotton Wick" },
            waxType = form.waxType.trim().ifBlank { "100% Pure Soy Wax" },
            description = trimmedDesc,
            customImagePath = form.customImagePath,
            sortOrder = existing?.sortOrder ?: minSortOrder
        )

        viewModelScope.launch {
            repository.saveProduct(entity)
            resetAdminForm()
            showToast(if (existing != null) "Candle updated!" else "New candle added to catalog!")
        }
    }

    fun deleteProduct(productId: String) {
        val editingSame = transientState.value.adminForm.editingProductId == productId
        viewModelScope.launch {
            repository.deleteProduct(productId)
            if (editingSame) {
                resetAdminForm()
            }
            showToast("Candle removed from catalog")
        }
    }

    fun resetDefaultProducts() {
        viewModelScope.launch {
            repository.resetDefaultProducts()
            resetAdminForm()
            showToast("Default candle catalog restored")
        }
    }

    fun updateBusinessWhatsAppNumber(rawInput: String) {
        val digits = rawInput.filter { it.isDigit() }
        if (digits.length < 10) {
            showToast("Enter a valid WhatsApp number with country code")
            return
        }
        viewModelScope.launch {
            repository.updateBusinessWhatsAppNumber(digits)
            showToast("Business WhatsApp number updated")
        }
    }
}
