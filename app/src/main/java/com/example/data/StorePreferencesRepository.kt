package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.storeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "diyans_candle_prefs"
)

data class CustomerCheckoutDetails(
    val fullName: String = "",
    val phoneNumber: String = "",
    val deliveryAddress: String = "",
    val customNote: String = ""
)

class StorePreferencesRepository(private val context: Context) {

    private object Keys {
        val CATALOG_SEEDED = booleanPreferencesKey("catalog_seeded_v1")
        val WHATSAPP_NUMBER = stringPreferencesKey("business_whatsapp_number")
        val CUSTOMER_NAME = stringPreferencesKey("customer_name")
        val CUSTOMER_PHONE = stringPreferencesKey("customer_phone")
        val CUSTOMER_ADDRESS = stringPreferencesKey("customer_address")
        val CUSTOMER_NOTE = stringPreferencesKey("customer_note")
    }

    val businessWhatsAppNumber: Flow<String> = context.storeDataStore.data.map { prefs ->
        prefs[Keys.WHATSAPP_NUMBER]?.takeIf { it.isNotBlank() }
            ?: DefaultCatalogProvider.DEFAULT_WHATSAPP_NUMBER
    }

    val savedCustomerDetails: Flow<CustomerCheckoutDetails> = context.storeDataStore.data.map { prefs ->
        CustomerCheckoutDetails(
            fullName = prefs[Keys.CUSTOMER_NAME].orEmpty(),
            phoneNumber = prefs[Keys.CUSTOMER_PHONE].orEmpty(),
            deliveryAddress = prefs[Keys.CUSTOMER_ADDRESS].orEmpty(),
            customNote = prefs[Keys.CUSTOMER_NOTE].orEmpty()
        )
    }

    suspend fun isCatalogSeeded(): Boolean {
        return context.storeDataStore.data.first()[Keys.CATALOG_SEEDED] == true
    }

    suspend fun markCatalogSeeded() {
        context.storeDataStore.edit { prefs ->
            prefs[Keys.CATALOG_SEEDED] = true
        }
    }

    suspend fun updateWhatsAppNumber(rawDigits: String) {
        val sanitized = rawDigits.filter { it.isDigit() }
        if (sanitized.isNotBlank()) {
            context.storeDataStore.edit { prefs ->
                prefs[Keys.WHATSAPP_NUMBER] = sanitized
            }
        }
    }

    suspend fun saveCustomerDetails(details: CustomerCheckoutDetails) {
        context.storeDataStore.edit { prefs ->
            prefs[Keys.CUSTOMER_NAME] = details.fullName
            prefs[Keys.CUSTOMER_PHONE] = details.phoneNumber
            prefs[Keys.CUSTOMER_ADDRESS] = details.deliveryAddress
            prefs[Keys.CUSTOMER_NOTE] = details.customNote
        }
    }
}
