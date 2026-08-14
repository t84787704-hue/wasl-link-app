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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WaslBgCream
import com.example.ui.theme.WaslBorderBeige
import com.example.ui.theme.WaslPrimaryCharcoal
import com.example.ui.theme.WaslSandGold
import com.example.ui.theme.WaslSaudiGreen
import com.example.ui.theme.WaslSaudiGreenLight
import com.example.ui.theme.WaslSurfaceBeige
import com.example.ui.theme.WaslSurfaceWhite
import com.example.ui.theme.WaslTextPrimary
import com.example.ui.theme.WaslTextSecondary
import com.example.ui.theme.WaslWhatsAppGreen

data class MenuItemParsed(
    val rawText: String,
    val title: String,
    val price: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    shopNameArabic: String,
    shopName: String,
    whatsappNumber: String,
    menuText: String,
    isArabic: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val menuItems = parseMenuItems(menuText)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = WaslBgCream,
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
                            .background(WaslSurfaceBeige)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestaurantMenu,
                            contentDescription = null,
                            tint = WaslPrimaryCharcoal,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isArabic) "قائمة المنتجات والأسعار" else "Menu & Products",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WaslTextPrimary
                        )
                        Text(
                            text = if (shopNameArabic.isNotBlank()) shopNameArabic else shopName,
                            style = MaterialTheme.typography.bodySmall,
                            color = WaslSandGold
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(WaslSurfaceWhite)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = WaslTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (menuItems.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                    border = BorderStroke(1.dp, WaslBorderBeige),
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
                            text = if (isArabic) "لم تتم إضافة منتجات بعد" else "No menu items added yet",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = WaslTextPrimary
                        )
                        Text(
                            text = if (isArabic) "يمكنك إضافة المنتجات والأسعار من صفحة التعديل" else "You can add items and prices from the edit form",
                            style = MaterialTheme.typography.bodySmall,
                            color = WaslTextSecondary,
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
                            isArabic = isArabic,
                            onOrderClick = {
                                orderItemViaWhatsApp(
                                    context = context,
                                    whatsappNumber = whatsappNumber,
                                    itemTitle = item.title,
                                    shopName = shopNameArabic.ifBlank { shopName },
                                    isArabic = isArabic
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
                    val rawNumber = whatsappNumber.trim()
                    val formatted = if (rawNumber.startsWith("966")) rawNumber else "966$rawNumber"
                    val text = if (isArabic) "السلام عليكم، أود الطلب من القائمة" else "Hello, I would like to order from the menu"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$formatted?text=${Uri.encode(text)}"))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
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
                        text = if (isArabic) "إرسال طلب مباشر عبر واتساب" else "Send Order via WhatsApp",
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
    isArabic: Boolean,
    onOrderClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
        border = BorderStroke(1.dp, WaslBorderBeige),
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
                    color = WaslTextPrimary
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
                    containerColor = WaslSurfaceBeige,
                    contentColor = WaslPrimaryCharcoal
                ),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = if (isArabic) "طلب" else "Order",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun parseMenuItems(raw: String): List<MenuItemParsed> {
    return raw.lines()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map { line ->
            var cleaned = line
            if (cleaned.startsWith("•") || cleaned.startsWith("-") || cleaned.startsWith("*")) {
                cleaned = cleaned.substring(1).trim()
            }

            // Extract price if format contains '-' or '—'
            if (cleaned.contains(" - ")) {
                val parts = cleaned.split(" - ", limit = 2)
                MenuItemParsed(rawText = line, title = parts[0].trim(), price = parts[1].trim())
            } else if (cleaned.contains(" — ")) {
                val parts = cleaned.split(" — ", limit = 2)
                MenuItemParsed(rawText = line, title = parts[0].trim(), price = parts[1].trim())
            } else {
                MenuItemParsed(rawText = line, title = cleaned, price = null)
            }
        }
}

private fun orderItemViaWhatsApp(
    context: Context,
    whatsappNumber: String,
    itemTitle: String,
    shopName: String,
    isArabic: Boolean
) {
    val rawNumber = whatsappNumber.trim()
    val formatted = if (rawNumber.startsWith("966")) rawNumber else "966$rawNumber"
    val greeting = if (isArabic) {
        "السلام عليكم، أود طلب ($itemTitle) من $shopName."
    } else {
        "Hello, I would like to order ($itemTitle) from $shopName."
    }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$formatted?text=${Uri.encode(greeting)}"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback
    }
}
