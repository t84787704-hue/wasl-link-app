package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CountryCodeHelper
import com.example.data.CurrencyHelper
import com.wasl.saudishop.R
import com.example.ui.theme.WaslSaudiGreen
import com.example.ui.theme.WaslSaudiGreenLight

data class MenuItemParsed(
    val rawText: String,
    val title: String,
    val price: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    shopName: String,
    whatsappNumber: String,
    whatsappCountryCode: String = "966",
    menuText: String,
    selectedCurrency: String = "SAR",
    selectedLanguageCode: String = "system",
    isArabic: Boolean = false,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val menuItems = remember(menuText, selectedCurrency, selectedLanguageCode) {
        parseMenuItems(menuText, selectedCurrency, selectedLanguageCode)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .testTag("menu_bottom_sheet")
        ) {
            // Header Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.menu_sheet_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = shopName.ifBlank { stringResource(R.string.app_name) },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (menuItems.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp)
                    ) {
                        Text(
                            text = "📋",
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.menu_empty_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.menu_empty_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                ) {
                    items(menuItems) { item ->
                        MenuItemCard(
                            item = item,
                            onOrderClick = {
                                orderItemViaWhatsApp(
                                    context = context,
                                    whatsappCountryCode = whatsappCountryCode,
                                    whatsappNumber = whatsappNumber,
                                    itemTitle = item.title,
                                    price = item.price,
                                    shopName = shopName
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Direct WhatsApp Order CTA Button
            Button(
                onClick = {
                    val fullNumber = CountryCodeHelper.formatFullInternational(whatsappCountryCode, whatsappNumber)
                    val defaultCC = whatsappCountryCode.ifBlank { CountryCodeHelper.DEFAULT_COUNTRY_CODE }
                    val formatted = if (fullNumber.isNotBlank()) fullNumber else "${defaultCC}591257059"
                    val text = "Hello, I would like to order from the menu"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$formatted?text=${Uri.encode(text)}"))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Fallback
                    }
                },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WaslSaudiGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("menu_whatsapp_order_btn")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.btn_order_via_whatsapp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun MenuItemCard(
    item: MenuItemParsed,
    onOrderClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (item.price != null) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = WaslSaudiGreenLight
                    ) {
                        Text(
                            text = item.price,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = WaslSaudiGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            FilledTonalButton(
                onClick = onOrderClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_order_via_whatsapp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun parseMenuItems(raw: String, currencyCode: String = "SAR", languageCode: String = "en"): List<MenuItemParsed> {
    val effectiveLang = com.example.data.TranslationHelper.getEffectiveLanguage(languageCode)
    return raw.lines()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map { line ->
            var cleaned = line
            if (cleaned.startsWith("•") || cleaned.startsWith("-") || cleaned.startsWith("*")) {
                cleaned = cleaned.substring(1).trim()
            }

            when {
                cleaned.contains(" - ") -> {
                    val parts = cleaned.split(" - ", limit = 2)
                    val rawTitle = parts[0].trim()
                    val filteredTitle = com.example.data.TranslationHelper.displayMenuItem(rawTitle, effectiveLang)
                    MenuItemParsed(
                        rawText = line,
                        title = filteredTitle,
                        price = CurrencyHelper.formatPrice(parts[1].trim(), currencyCode)
                    )
                }
                cleaned.contains(" — ") -> {
                    val parts = cleaned.split(" — ", limit = 2)
                    val rawTitle = parts[0].trim()
                    val filteredTitle = com.example.data.TranslationHelper.displayMenuItem(rawTitle, effectiveLang)
                    MenuItemParsed(
                        rawText = line,
                        title = filteredTitle,
                        price = CurrencyHelper.formatPrice(parts[1].trim(), currencyCode)
                    )
                }
                cleaned.contains(":") -> {
                    val parts = cleaned.split(":", limit = 2)
                    val rawTitle = parts[0].trim()
                    val filteredTitle = com.example.data.TranslationHelper.displayMenuItem(rawTitle, effectiveLang)
                    MenuItemParsed(
                        rawText = line,
                        title = filteredTitle,
                        price = CurrencyHelper.formatPrice(parts[1].trim(), currencyCode)
                    )
                }
                cleaned.matches(Regex("^[0-9]+(\\s*([A-Za-z﷼₹$€£₺৳]+|ر\\.س|SR))?$", RegexOption.IGNORE_CASE)) -> {
                    val priceFormatted = CurrencyHelper.formatPrice(cleaned, currencyCode)
                    val defaultTitle = if (effectiveLang == "ar") "عنصر" else if (effectiveLang == "ur") "آئٹم" else "Item"
                    MenuItemParsed(rawText = line, title = defaultTitle, price = priceFormatted)
                }
                else -> {
                    val filteredTitle = com.example.data.TranslationHelper.displayMenuItem(cleaned, effectiveLang)
                    MenuItemParsed(rawText = line, title = filteredTitle, price = null)
                }
            }
        }
}

private fun orderItemViaWhatsApp(
    context: Context,
    whatsappCountryCode: String,
    whatsappNumber: String,
    itemTitle: String,
    price: String?,
    shopName: String
) {
    val fullNumber = CountryCodeHelper.formatFullInternational(whatsappCountryCode, whatsappNumber)
    val defaultCC = whatsappCountryCode.ifBlank { CountryCodeHelper.DEFAULT_COUNTRY_CODE }
    val formatted = if (fullNumber.isNotBlank()) fullNumber else "${defaultCC}591257059"
    val priceSuffix = if (!price.isNullOrBlank()) " ($price)" else ""
    val greeting = "Hello, I would like to order $itemTitle$priceSuffix from $shopName."
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$formatted?text=${Uri.encode(greeting)}"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback
    }
}
