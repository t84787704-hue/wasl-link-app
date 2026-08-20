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
import androidx.compose.material.icons.filled.QrCode2
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasl.saudishop.R
import com.example.data.CountryCodeHelper
import com.example.ui.WaslUiState
import com.example.ui.components.ShopLocationPreviewCard
import com.example.ui.components.ShopLogoAvatar
import com.example.ui.components.WaslBigActionButton
import com.example.ui.theme.WaslBgCream
import com.example.ui.theme.WaslBorderBeige
import com.example.ui.theme.WaslBorderDark
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

@Composable
fun PreviewScreen(
    uiState: WaslUiState,
    onOpenWhatsApp: (Context) -> Unit,
    onOpenGoogleMaps: (Context) -> Unit,
    onShowMenu: () -> Unit,
    onShareStore: (Context) -> Unit,
    onShareQr: () -> Unit,
    onEditFormClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                        text = stringResource(R.string.preview_live_badge),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = WaslTextPrimary
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Share as QR button
                IconButton(
                    onClick = onShareQr,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(WaslSurfaceWhite)
                        .testTag("button_share_qr_top")
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = stringResource(R.string.btn_share_as_qr),
                        tint = WaslSandGold,
                        modifier = Modifier.size(20.dp)
                    )
                }

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
                        contentDescription = stringResource(R.string.btn_share_link),
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
                        contentDescription = stringResource(R.string.tab_edit_form),
                        tint = WaslPrimaryCharcoal,
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

                // Shop Name
                val displayName = uiState.shopName.ifBlank { "Wasl Market" }
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = WaslTextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("preview_shop_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // City & Category
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = WaslSurfaceBeige
                ) {
                    Text(
                        text = uiState.city.ifBlank { stringResource(R.string.preview_country_default) },
                        style = MaterialTheme.typography.labelSmall,
                        color = WaslTextSecondary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
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
                val defaultCC = uiState.whatsappCountryCode.ifBlank { CountryCodeHelper.DEFAULT_COUNTRY_CODE }
                val formattedPhone = CountryCodeHelper.formatDisplayInternational(
                    countryCode = uiState.whatsappCountryCode,
                    number = uiState.whatsappNumber,
                    fallbackDefault = "+$defaultCC 5X XXX XXXX"
                )
                val whatsappSubtitle = if (uiState.defaultGreeting.isNotBlank()) {
                    "$formattedPhone • 💬 \"${uiState.defaultGreeting.take(28)}...\""
                } else {
                    formattedPhone
                }
                WaslBigActionButton(
                    icon = Icons.Default.Storefront,
                    iconColor = WaslSaudiGreen,
                    iconBgColor = WaslWhatsAppContainer,
                    titleArabic = stringResource(R.string.btn_whatsapp_chat),
                    titleEnglish = "WhatsApp",
                    subtitle = whatsappSubtitle,
                    badgeText = null,
                    testTag = "button_preview_whatsapp",
                    onClick = { onOpenWhatsApp(context) }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Component 2: Interactive Shop Map Location Preview Card with Google Maps Launcher
                ShopLocationPreviewCard(
                    shopName = uiState.shopName.ifBlank { "Wasl Market" },
                    city = uiState.city.ifBlank { "Saudi Arabia" },
                    locationUrl = uiState.locationUrl,
                    isArabic = false,
                    onOpenGoogleMaps = onOpenGoogleMaps,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Button 3: Menu Button that shows menu list
                val menuLinesCount = uiState.menuItemsText.lines().filter { it.isNotBlank() }.size
                val menuSubtitle = if (menuLinesCount > 0) {
                    stringResource(R.string.menu_items_available, menuLinesCount)
                } else {
                    stringResource(R.string.btn_view_menu)
                }
                WaslBigActionButton(
                    icon = Icons.Default.RestaurantMenu,
                    iconColor = WaslSandGold,
                    iconBgColor = WaslSurfaceBeige,
                    titleArabic = stringResource(R.string.btn_view_menu),
                    titleEnglish = "Menu & Catalog",
                    subtitle = menuSubtitle,
                    testTag = "button_preview_menu",
                    onClick = onShowMenu
                )

                // Inline Menu List under shop buttons if menu items are entered
                val parsedItems = parseMenuItems(uiState.menuItemsText, uiState.selectedCurrency)
                if (parsedItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                        border = BorderStroke(1.dp, WaslBorderBeige),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("preview_menu_list_card")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.RestaurantMenu,
                                        contentDescription = null,
                                        tint = WaslSandGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(R.string.field_menu_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = WaslTextPrimary
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = WaslSurfaceBeige
                                ) {
                                    Text(
                                        text = "${parsedItems.size} items",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WaslTextSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            parsedItems.forEachIndexed { index, item ->
                                if (index > 0) {
                                    androidx.compose.material3.HorizontalDivider(
                                        color = WaslBorderBeige.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = WaslTextPrimary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (item.price != null) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = WaslSaudiGreenLight
                                        ) {
                                            Text(
                                                text = item.price,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = WaslSaudiGreen,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Footer branding
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = WaslSurfaceBeige.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = stringResource(R.string.preview_powered_by),
                        style = MaterialTheme.typography.labelSmall,
                        color = WaslTextTertiary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Store QR Code Action Card
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
            border = BorderStroke(1.5.dp, WaslSandGold.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(WaslSurfaceBeige)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = null,
                                tint = WaslSandGold,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = stringResource(R.string.qr_card_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WaslTextPrimary
                            )
                            Text(
                                text = stringResource(R.string.qr_card_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = WaslTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Big Gold 'Share as QR' button (56px height)
                Button(
                    onClick = onShareQr,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC9A86A)
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("button_share_as_qr")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.btn_share_as_qr),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

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
                text = stringResource(R.string.btn_back_to_edit),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
