package com.example.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wasl.saudishop.R
import com.example.data.CountryCodeHelper
import com.example.ui.WaslUiState
import com.example.ui.components.SaudiVerifiedBadge
import com.example.ui.components.ShopLogoAvatar
import com.example.ui.theme.WaslSandGold
import com.example.ui.theme.WaslSaudiGreen
import com.example.ui.theme.WaslSaudiGreenLight
import com.example.ui.util.QrCodeGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrBottomSheet(
    uiState: WaslUiState,
    isArabic: Boolean = false,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val fullNumber = CountryCodeHelper.formatFullInternational(uiState.whatsappCountryCode, uiState.whatsappNumber)
    val defaultCC = uiState.whatsappCountryCode.ifBlank { CountryCodeHelper.DEFAULT_COUNTRY_CODE }
    val formattedWhatsapp = if (fullNumber.isNotBlank()) fullNumber else "${defaultCC}591257059"
    val greetingText = uiState.defaultGreeting.trim()
    val encodedGreeting = if (greetingText.isNotBlank()) Uri.encode(greetingText) else ""
    val waLinkWithGreeting = if (encodedGreeting.isNotBlank()) {
        "https://wa.me/$formattedWhatsapp?text=$encodedGreeting"
    } else {
        "https://wa.me/$formattedWhatsapp"
    }

    val mapsLink = uiState.locationUrl.trim().let { rawUrl ->
        val coordsMatch = Regex("([0-9.-]+)\\s*[,\\s]\\s*([0-9.-]+)").find(rawUrl)
        when {
            rawUrl.startsWith("http://") || rawUrl.startsWith("https://") -> rawUrl
            coordsMatch != null -> {
                val (lat, lng) = coordsMatch.destructured
                "https://www.google.com/maps/search/?api=1&query=$lat,$lng"
            }
            rawUrl.isNotBlank() -> "https://www.google.com/maps/search/?api=1&query=${Uri.encode(rawUrl)}"
            uiState.city.isNotBlank() -> "https://www.google.com/maps/search/?api=1&query=${Uri.encode(uiState.city + ", Saudi Arabia")}"
            else -> "https://www.google.com/maps/search/?api=1&query=24.56418,46.87677"
        }
    }

    val qrBitmap: Bitmap? = remember(waLinkWithGreeting) {
        QrCodeGenerator.generateQrBitmap(waLinkWithGreeting, 600)
    }

    val nameEn = uiState.shopName.trim()
    val city = uiState.city.trim()

    val shopName = nameEn.ifBlank { "My Store" }

    val menuRaw = uiState.menuItemsText.trim()
    val menuSection = if (menuRaw.isNotBlank()) {
        "🍽️ Menu & Prices:\n$menuRaw\n\n"
    } else {
        ""
    }

    val shareText = buildString {
        appendLine(if (city.isNotBlank()) "📍 $shopName - $city" else "📍 $shopName")
        appendLine()
        if (menuSection.isNotBlank()) {
            append(menuSection)
        }
        appendLine("💬 WhatsApp: $waLinkWithGreeting")
        appendLine("🗺️ Location: $mapsLink")
        appendLine()
        append("✨ Created via Wasl Market | وصل")
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
                .testTag("qr_bottom_sheet")
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
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = WaslSandGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.qr_bottom_sheet_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.qr_bottom_sheet_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Branded QR Card Container
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Logo and shop names
                    ShopLogoAvatar(
                        emoji = uiState.logoEmoji.ifBlank { "☕" },
                        size = 56,
                        modifier = Modifier.testTag("qr_modal_logo")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = uiState.shopName.ifBlank { "My Shop" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    val subtitleText = when {
                        uiState.category.isNotBlank() && uiState.city.isNotBlank() -> "${uiState.category} • ${uiState.city}"
                        uiState.category.isNotBlank() -> uiState.category
                        uiState.city.isNotBlank() -> uiState.city
                        else -> "Specialty Cafe & Pastries"
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subtitleText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // The Rendered QR Code (250x250 with padding)
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color.White,
                        border = BorderStroke(2.dp, WaslSandGold.copy(alpha = 0.4f)),
                        shadowElevation = 3.dp,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            if (qrBitmap != null) {
                                Image(
                                    bitmap = qrBitmap.asImageBitmap(),
                                    contentDescription = "Store QR Code",
                                    modifier = Modifier
                                        .size(250.dp)
                                        .testTag("qr_code_image")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Green Arabic text
                    Text(
                        text = "امسح الرمز لزيارة المتجر والتواصل فوراً",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = WaslSaudiGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Small grey text
                    Text(
                        text = "Scan to visit store & chat on WhatsApp",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bottom gold text
                    Text(
                        text = "Wasl Storefront | وصل ✨",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = WaslSandGold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Primary Share as Branded Poster Button (56px height, elegant gold)
            Button(
                onClick = {
                    val brandedCard = QrCodeGenerator.generateBrandedStoreCard(
                        context = context,
                        shopName = uiState.shopName,
                        category = uiState.category,
                        city = uiState.city,
                        logoEmoji = uiState.logoEmoji,
                        qrContent = waLinkWithGreeting
                    )
                    if (brandedCard != null) {
                        QrCodeGenerator.shareQrImage(context, brandedCard, shareText)
                    } else if (qrBitmap != null) {
                        QrCodeGenerator.shareQrImage(context, qrBitmap, shareText)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WaslSandGold
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("button_share_qr_image")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.btn_share_qr_poster),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary Share & Copy Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Share Text Link
                FilledTonalButton(
                    onClick = {
                        val sendIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        val chooser = android.content.Intent.createChooser(sendIntent, context.getString(R.string.share_chooser_title))
                        chooser.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(chooser)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("button_share_qr_link")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.btn_share_link),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Copy Link Button
                OutlinedButton(
                    onClick = { QrCodeGenerator.copyToClipboard(context, waLinkWithGreeting) },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("button_copy_qr_link")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.btn_copy_link),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
