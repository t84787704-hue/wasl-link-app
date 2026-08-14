package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.WaslUiState
import com.example.ui.components.SaudiVerifiedBadge
import com.example.ui.components.ShopLogoAvatar
import com.example.ui.components.WaslBigActionButton
import com.example.ui.theme.WaslBgCream
import com.example.ui.theme.WaslBorderBeige
import com.example.ui.theme.WaslBorderDark
import com.example.ui.theme.WaslMapsBlue
import com.example.ui.theme.WaslMapsContainer
import com.example.ui.theme.WaslPrimaryCharcoal
import com.example.ui.theme.WaslSandGold
import com.example.ui.theme.WaslSaudiGreen
import com.example.ui.theme.WaslSaudiGreenLight
import com.example.ui.theme.WaslSurfaceBeige
import com.example.ui.theme.WaslSurfaceCard
import com.example.ui.theme.WaslSurfaceWhite
import com.example.ui.theme.WaslTextPrimary
import com.example.ui.theme.WaslTextSecondary
import com.example.ui.theme.WaslTextTertiary
import com.example.ui.theme.WaslWhatsAppContainer
import com.example.ui.theme.WaslWhatsAppGreen

@Composable
fun PreviewScreen(
    uiState: WaslUiState,
    onOpenWhatsApp: (Context) -> Unit,
    onOpenGoogleMaps: (Context) -> Unit,
    onShowMenu: () -> Unit,
    onShareStore: (Context) -> Unit,
    onEditFormClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isArabic = uiState.isArabicLayout

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("preview_screen")
    ) {
        // Top simulation control bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .padding(bottom = 12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = WaslSurfaceWhite,
                border = BorderStroke(1.dp, WaslBorderBeige)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(WaslSaudiGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "معاينة حية للمتجر" else "Live Storefront Preview",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = WaslTextPrimary
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Share link button
                IconButton(
                    onClick = { onShareStore(context) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(WaslSurfaceWhite)
                        .testTag("button_share_store")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Storefront Link",
                        tint = WaslPrimaryCharcoal,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Edit button
                IconButton(
                    onClick = onEditFormClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(WaslSurfaceWhite)
                        .testTag("button_quick_edit")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = WaslSandGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Phone Container Mockup
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(1.5.dp, WaslBorderBeige),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                WaslSurfaceWhite,
                                WaslBgCream,
                                WaslSurfaceCard
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 28.dp)
            ) {
                // 1. Shop Logo on Top
                ShopLogoAvatar(
                    emoji = uiState.logoEmoji.ifBlank { "☕" },
                    size = 92,
                    modifier = Modifier.testTag("preview_shop_logo")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Shop Names
                Text(
                    text = uiState.shopNameArabic.ifBlank { "متجر وصل" },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = WaslTextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("preview_shop_name_arabic")
                )

                if (uiState.shopName.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.shopName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = WaslSandGold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("preview_shop_name")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Saudi Verified Badge & City
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    SaudiVerifiedBadge(isArabic = isArabic)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = WaslSurfaceBeige
                    ) {
                        Text(
                            text = uiState.city.ifBlank { "المملكة العربية السعودية" },
                            style = MaterialTheme.typography.labelSmall,
                            color = WaslTextSecondary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                if (uiState.category.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = uiState.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = WaslTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Divider line in soft beige
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(1.dp)
                        .background(WaslBorderBeige)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- 3 BIG BUTTONS ---

                // Button 1: WhatsApp Button that opens WhatsApp
                val formattedPhone = if (uiState.whatsappNumber.startsWith("966")) {
                    "+${uiState.whatsappNumber}"
                } else {
                    "+966 ${uiState.whatsappNumber}"
                }
                WaslBigActionButton(
                    icon = Icons.Default.Storefront,
                    iconColor = WaslSaudiGreen,
                    iconBgColor = WaslWhatsAppContainer,
                    titleArabic = "تواصل عبر واتساب",
                    titleEnglish = "WhatsApp Chat",
                    subtitle = formattedPhone,
                    badgeText = "رد فوري",
                    testTag = "button_preview_whatsapp",
                    onClick = { onOpenWhatsApp(context) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Button 2: Location Button that opens Google Maps
                WaslBigActionButton(
                    icon = Icons.Default.LocationOn,
                    iconColor = WaslMapsBlue,
                    iconBgColor = WaslMapsContainer,
                    titleArabic = "موقع المتجر على الخريطة",
                    titleEnglish = "Google Maps Location",
                    subtitle = if (isArabic) "عرض الاتجاهات وساعات العمل" else "Open in Google Maps",
                    testTag = "button_preview_location",
                    onClick = { onOpenGoogleMaps(context) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Button 3: Menu Button that shows menu list
                val menuLinesCount = uiState.menuItemsText.lines().filter { it.isNotBlank() }.size
                WaslBigActionButton(
                    icon = Icons.Default.RestaurantMenu,
                    iconColor = WaslSandGold,
                    iconBgColor = WaslSurfaceBeige,
                    titleArabic = "قائمة المنتجات والأسعار",
                    titleEnglish = "View Menu & Prices",
                    subtitle = if (isArabic) "$menuLinesCount أصناف متاحة للطلب" else "$menuLinesCount items available",
                    testTag = "button_preview_menu",
                    onClick = onShowMenu
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Footer branding
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = WaslSurfaceBeige.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = if (isArabic) "✨ صفحة متجر مدعومة بواسطة وصل | Wasl" else "✨ Powered by Wasl Storefront",
                        style = MaterialTheme.typography.labelSmall,
                        color = WaslTextTertiary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Return to edit button
        OutlinedButton(
            onClick = onEditFormClick,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.5.dp, WaslBorderDark),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = WaslPrimaryCharcoal
            ),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = WaslPrimaryCharcoal,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isArabic) "العودة لتعديل بيانات المتجر" else "Back to Edit Shop Form",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
