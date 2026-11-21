package com.example.data

import kotlinx.coroutines.flow.Flow

class CandleShopRepository(
    private val dao: CandleDao,
    private val preferences: StorePreferencesRepository
) {
    val products: Flow<List<ProductEntity>> = dao.observeProducts()
    val cartItems: Flow<List<CartItemEntity>> = dao.observeCartItems()
    val businessWhatsAppNumber: Flow<String> = preferences.businessWhatsAppNumber
    val savedCustomerDetails: Flow<CustomerCheckoutDetails> = preferences.savedCustomerDetails

    suspend fun ensureDefaultCatalogSeeded() {
        val seeded = preferences.isCatalogSeeded()
        val count = dao.getProductCount()
        if (!seeded && count == 0) {
            dao.insertProducts(DefaultCatalogProvider.defaultProducts())
            preferences.markCatalogSeeded()
        } else if (!seeded && count > 0) {
            preferences.markCatalogSeeded()
        }
    }

    suspend fun saveProduct(product: ProductEntity) {
        dao.upsertProduct(product)
    }

    suspend fun deleteProduct(productId: String) {
        dao.deleteProductAndCartEntry(productId)
    }

    suspend fun resetDefaultProducts() {
        dao.resetProductsToDefault(DefaultCatalogProvider.defaultProducts())
        preferences.markCatalogSeeded()
    }

    suspend fun updateCartDelta(productId: String, delta: Int): Int {
        val existing = dao.getCartItem(productId)
        val nextQty = (existing?.quantity ?: 0) + delta
        if (nextQty <= 0) {
            dao.deleteCartItem(productId)
            return 0
        } else {
            dao.upsertCartItem(CartItemEntity(productId = productId, quantity = nextQty))
            return nextQty
        }
    }

    suspend fun removeCartItem(productId: String) {
        dao.deleteCartItem(productId)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }

    suspend fun updateBusinessWhatsAppNumber(number: String) {
        preferences.updateWhatsAppNumber(number)
    }

    suspend fun saveCustomerDetails(details: CustomerCheckoutDetails) {
        preferences.saveCustomerDetails(details)
    }
}
