package com.example.data

enum class CandleCategory(val label: String) {
    ALL("All"),
    HEART_AND_SHELL("Heart & Shell"),
    FLOATING_AND_URLI("Floating & Urli"),
    JAR_CANDLES("Jar Candles"),
    GIFT_SETS("Gift Sets");

    companion object {
        val filterCategories: List<CandleCategory> = entries
        val editableCategories: List<CandleCategory> = listOf(
            HEART_AND_SHELL,
            FLOATING_AND_URLI,
            JAR_CANDLES,
            GIFT_SETS
        )

        fun fromLabel(label: String): CandleCategory {
            return entries.firstOrNull { it.label.equals(label, ignoreCase = true) }
                ?: JAR_CANDLES
        }
    }
}

enum class CandleArtStyle(val key: String, val displayName: String) {
    HEART("heart", "Multi-Wick Heart"),
    SHELL("shell", "Pearl Conch Shell"),
    FLOATING("floating", "Floating Flowers"),
    URLI("urli", "Golden Metal Urli"),
    BOAT("boat", "Oval Boat Multi-Wick"),
    HAMPER("hamper", "Festive Gift Set"),
    JAR("jar", "Classic Soy Jar");

    companion object {
        fun fromKey(key: String): CandleArtStyle {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: JAR
        }
    }
}

object DefaultCatalogProvider {
    const val DEFAULT_WHATSAPP_NUMBER = "919924472228"

    fun defaultProducts(): List<ProductEntity> = listOf(
        ProductEntity(
            id = "candle-heart-1",
            name = "Multi-Wick Heart Soy Candle",
            category = CandleCategory.HEART_AND_SHELL.label,
            price = 349,
            artStyle = CandleArtStyle.HEART.key,
            spec = "Pure Soy Wax • Cotton Wick",
            waxType = "100% Pure Soy Wax",
            description = "Hand-poured heart-shaped pure soy wax candle featuring dual braided cotton wicks for an even, warm, and long-lasting romantic glow.",
            customImagePath = null,
            sortOrder = 1
        ),
        ProductEntity(
            id = "candle-shell-2",
            name = "Pearl Conch Shell Gel Candle",
            category = CandleCategory.HEART_AND_SHELL.label,
            price = 399,
            artStyle = CandleArtStyle.SHELL.key,
            spec = "Crystal Gel Wax • Cotton Wick",
            waxType = "Pure Gel & Soy Blend",
            description = "Ocean-inspired conch shell candle crafted with translucent gel wax, shimmering embedded pearls, and a clean-burning cotton wick.",
            customImagePath = null,
            sortOrder = 2
        ),
        ProductEntity(
            id = "candle-floating-3",
            name = "Floating Daisy & Peony Wax Candles - Set of 4",
            category = CandleCategory.FLOATING_AND_URLI.label,
            price = 249,
            artStyle = CandleArtStyle.FLOATING.key,
            spec = "Pure Soy Wax • Set of 4",
            waxType = "100% Pure Soy Wax",
            description = "Delicate multi-color floral daisy and peony floating candles designed for water bowls, festive decor, and serene table centerpieces.",
            customImagePath = null,
            sortOrder = 3
        ),
        ProductEntity(
            id = "candle-urli-4",
            name = "Golden Petal Metal Urli Candle",
            category = CandleCategory.FLOATING_AND_URLI.label,
            price = 499,
            artStyle = CandleArtStyle.URLI.key,
            spec = "Metal Container • Cotton Wick",
            waxType = "Pure Soy Wax",
            description = "Traditional hammered golden metal urli filled with scented pure soy wax, botanical petal accents, and multiple cotton wicks.",
            customImagePath = null,
            sortOrder = 4
        ),
        ProductEntity(
            id = "candle-boat-5",
            name = "Oval Boat-Shaped Multi-Wick Candle",
            category = CandleCategory.JAR_CANDLES.label,
            price = 449,
            artStyle = CandleArtStyle.BOAT.key,
            spec = "Multi-Cotton Wick • Handcrafted",
            waxType = "100% Pure Soy Wax",
            description = "Elegant artisanal oval boat-shaped container poured with creamy soy wax and triple cotton wicks for wide fragrance throw.",
            customImagePath = null,
            sortOrder = 5
        ),
        ProductEntity(
            id = "candle-hamper-6",
            name = "Festive Candle Gift Hamper",
            category = CandleCategory.GIFT_SETS.label,
            price = 699,
            artStyle = CandleArtStyle.HAMPER.key,
            spec = "Assorted Soy Candles • Gift Box",
            waxType = "Pure Soy & Gel Wax",
            description = "Curated luxury gift box featuring assorted handcrafted soy jar, floating floral, and sculpted candles with cotton wicks.",
            customImagePath = null,
            sortOrder = 6
        )
    )
}
