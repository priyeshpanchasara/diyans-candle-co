package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.example.data.CustomerCheckoutDetails
import com.example.data.ProductEntity
import java.text.NumberFormat
import java.util.Locale

data class CartLineItem(
    val product: ProductEntity,
    val quantity: Int
) {
    val lineTotal: Int get() = product.price * quantity
}

object WhatsAppOrderHelper {

    private val indiaNumberFormat: NumberFormat =
        NumberFormat.getIntegerInstance(Locale("en", "IN"))

    fun formatRupees(amount: Int): String {
        return "₹${indiaNumberFormat.format(amount)}"
    }

    fun formatPhoneDisplay(rawDigits: String): String {
        val digits = rawDigits.filter { it.isDigit() }
        return if (digits.length == 12 && digits.startsWith("91")) {
            "+91 ${digits.substring(2, 7)} ${digits.substring(7)}"
        } else if (digits.isNotEmpty()) {
            "+$digits"
        } else {
            "+91 99244 72228"
        }
    }

    fun buildOrderMessage(
        items: List<CartLineItem>,
        customer: CustomerCheckoutDetails
    ): String {
        val totalAmount = items.sumOf { it.lineTotal }
        val lines = mutableListOf<String>()
        lines += "🕯️ *New Order — Diyan's Candle Co.*"
        lines += "────────────────────"
        items.forEachIndexed { index, entry ->
            lines += "${index + 1}. *${entry.product.name}*\n   Qty: ${entry.quantity} × ₹${entry.product.price} = ₹${entry.lineTotal}"
        }
        lines += "────────────────────"
        lines += "*Total Amount: ₹$totalAmount*"
        lines += ""
        lines += "👤 *Customer Details:*"
        lines += "• Name: ${customer.fullName.trim()}"
        lines += "• Phone: ${customer.phoneNumber.trim()}"
        lines += "• Address: ${customer.deliveryAddress.trim()}"
        if (customer.customNote.isNotBlank()) {
            lines += "• Note: ${customer.customNote.trim()}"
        }
        lines += ""
        lines += "Please confirm availability & payment details. Thank you!"
        return lines.joinToString("\n")
    }

    fun buildInquiryMessage(): String {
        return "Hello Diyan's Candle Co.! 🕯️ I'd love to inquire about your handcrafted Pure Soy & Gel Wax Candles (or a custom/festive order)."
    }

    fun openWhatsApp(context: Context, phoneDigits: String, message: String): Boolean {
        val cleanPhone = phoneDigits.filter { it.isDigit() }.ifBlank { "919924472228" }
        val encodedMessage = Uri.encode(message)
        val uri = Uri.parse("https://wa.me/$cleanPhone?text=$encodedMessage")

        val preferredPackages = listOf("com.whatsapp", "com.whatsapp.w4b")
        for (pkg in preferredPackages) {
            if (isPackageInstalled(context, pkg)) {
                val directIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage(pkg)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    context.startActivity(directIntent)
                    return true
                } catch (_: Exception) {
                    // Fall through to generic handler
                }
            }
        }

        val fallbackIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(fallbackIntent)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun copyToClipboard(context: Context, label: String, text: String): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                ?: return false
            clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }
}
