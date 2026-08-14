package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.WaslUiState
import com.example.ui.components.ShopLogoAvatar
import com.example.ui.components.TemplatePresetChip
import com.example.ui.theme.WaslBgCream
import com.example.ui.theme.WaslBorderBeige
import com.example.ui.theme.WaslPrimaryCharcoal
import com.example.ui.theme.WaslSandGold
import com.example.ui.theme.WaslSaudiGreen
import com.example.ui.theme.WaslSaudiGreenLight
import com.example.ui.theme.WaslSurfaceBeige
import com.example.ui.theme.WaslSurfaceCard
import com.example.ui.theme.WaslSurfaceWhite
import com.example.ui.theme.WaslTextPrimary
import com.example.ui.theme.WaslTextSecondary

@Composable
fun FormScreen(
    uiState: WaslUiState,
    onShopNameChange: (String) -> Unit,
    onShopNameArabicChange: (String) -> Unit,
    onWhatsappChange: (String) -> Unit,
    onLocationUrlChange: (String) -> Unit,
    onMenuItemsChange: (String) -> Unit,
    onLogoEmojiChange: (String) -> Unit,
    onPresetSelect: (String) -> Unit,
    onSaveClick: () -> Unit,
    onPreviewClick: () -> Unit,
    onShareQr: () -> Unit = {},
    onDetectLocationClick: () -> Unit = {},
    onOpenMapPicker: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isArabic = uiState.isArabicLayout

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            onDetectLocationClick()
        } else {
            Toast.makeText(
                context,
                if (isArabic) "يرجى منح إذن الموقع لتحديده تلقائياً" else "Location permission is required to detect GPS location",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val handleDetectLocationClick: () -> Unit = {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            onDetectLocationClick()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val emojiList = listOf("☕", "🥐", "👗", "🌸", "🍔", "🛍️", "💎", "💈", "🍦", "📚", "🪴", "🍰")

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("form_screen")
    ) {
        // Scrollable Form Fields
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
        // Hero Header Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
            border = BorderStroke(1.dp, WaslBorderBeige),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isArabic) "إنشاء وتعديل صفحة المتجر" else "Create & Edit Store Page",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = WaslTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isArabic) "املأ بيانات متجرك لتوليد رابط مباشر لعملائك" else "Fill in your shop details to generate a link page for customers",
                            style = MaterialTheme.typography.bodySmall,
                            color = WaslTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    ShopLogoAvatar(
                        emoji = uiState.logoEmoji,
                        size = 56
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Templates
                Text(
                    text = if (isArabic) "قوالب جاهزة سريعة للمتاجر:" else "Quick Shop Templates:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = WaslTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        TemplatePresetChip(
                            title = if (isArabic) "مقهى مختص" else "Coffee",
                            emoji = "☕",
                            isSelected = uiState.logoEmoji == "☕",
                            onClick = { onPresetSelect("cafe") }
                        )
                    }
                    item {
                        TemplatePresetChip(
                            title = if (isArabic) "مخبز وحلويات" else "Bakery",
                            emoji = "🥐",
                            isSelected = uiState.logoEmoji == "🥐",
                            onClick = { onPresetSelect("bakery") }
                        )
                    }
                    item {
                        TemplatePresetChip(
                            title = if (isArabic) "أزياء وعبايات" else "Abayas",
                            emoji = "👗",
                            isSelected = uiState.logoEmoji == "👗",
                            onClick = { onPresetSelect("boutique") }
                        )
                    }
                    item {
                        TemplatePresetChip(
                            title = if (isArabic) "عطور وبخور" else "Perfumes",
                            emoji = "🌸",
                            isSelected = uiState.logoEmoji == "🌸",
                            onClick = { onPresetSelect("perfume") }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form Fields Container
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
            border = BorderStroke(1.dp, WaslBorderBeige),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // 1. Shop Name (English)
                FormFieldLabel(
                    title = if (isArabic) "اسم المتجر (بالإنجليزي)" else "Shop Name",
                    subtitle = if (isArabic) "Shop Name (e.g. Al-Naseem Coffee)" else "English or standard brand name",
                    icon = Icons.Default.Storefront
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.shopName,
                    onValueChange = onShopNameChange,
                    singleLine = true,
                    placeholder = { Text("e.g. Al-Naseem Specialty Coffee") },
                    shape = RoundedCornerShape(16.dp),
                    colors = outlinedFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_shop_name")
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 2. Arabic Shop Name
                FormFieldLabel(
                    title = if (isArabic) "اسم المتجر بالعربي" else "Arabic Shop Name",
                    subtitle = if (isArabic) "الاسم الذي سيظهر لعملائك باللغة العربية" else "Arabic display name for your storefront",
                    icon = Icons.Default.Storefront
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.shopNameArabic,
                    onValueChange = onShopNameArabicChange,
                    singleLine = true,
                    placeholder = { Text("مثال: محمصة وقهوة النسيم") },
                    shape = RoundedCornerShape(16.dp),
                    colors = outlinedFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_shop_name_arabic")
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 3. WhatsApp Number with +966
                FormFieldLabel(
                    title = if (isArabic) "رقم الواتساب (السعودية)" else "WhatsApp Number",
                    subtitle = if (isArabic) "يبدأ برقم 5 بدون الصفر (مثال: 501234567)" else "Saudi WhatsApp number (9 digits)",
                    icon = Icons.Default.Phone
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.whatsappNumber,
                    onValueChange = onWhatsappChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        // Saudi Flag + 966 Prefix Badge
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = WaslSurfaceBeige,
                            modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(text = "🇸🇦", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+966",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = WaslTextPrimary
                                )
                            }
                        }
                    },
                    placeholder = { Text("501234567") },
                    shape = RoundedCornerShape(16.dp),
                    colors = outlinedFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_whatsapp_number")
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 4. Google Maps Location Link
                FormFieldLabel(
                    title = if (isArabic) "رابط موقع المتجر (خرائط جوجل)" else "Google Maps Location Link",
                    subtitle = if (isArabic) "رابط خرائط جوجل أو الإحداثيات المباشرة" else "Google Maps URL or GPS coordinates",
                    icon = Icons.Default.LocationOn
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.locationUrl,
                    onValueChange = onLocationUrlChange,
                    singleLine = true,
                    placeholder = { Text("https://maps.google.com/?q=24.7136,46.6753") },
                    shape = RoundedCornerShape(16.dp),
                    colors = outlinedFieldColors(),
                    trailingIcon = {
                        if (uiState.isDetectingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = WaslSaudiGreen,
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_location_link")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Location Helper Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Button 1: "📍 Use My Current Location"
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (uiState.isDetectingLocation) WaslSaudiGreenLight.copy(alpha = 0.6f) else WaslSaudiGreenLight,
                        border = BorderStroke(1.dp, WaslSaudiGreen.copy(alpha = 0.35f)),
                        modifier = Modifier
                            .weight(1.25f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(enabled = !uiState.isDetectingLocation) {
                                handleDetectLocationClick()
                            }
                            .testTag("button_detect_location")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            if (uiState.isDetectingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = WaslSaudiGreen,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isArabic) "جاري تحديد الموقع..." else "Detecting location...",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = WaslSaudiGreen
                                )
                            } else {
                                Text(
                                    text = "📍",
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isArabic) "استخدام موقعي الحالي" else "Use My Current Location",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = WaslSaudiGreen,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Button 2: "🗺️ Pick on Map"
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = WaslSurfaceBeige,
                        border = BorderStroke(1.dp, WaslBorderBeige),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                onOpenMapPicker()
                            }
                            .testTag("button_pick_on_map")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "🗺️",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isArabic) "تحديد على الخريطة" else "Pick on Map",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = WaslPrimaryCharcoal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 5. Menu Items Field
                FormFieldLabel(
                    title = if (isArabic) "قائمة المنتجات والأسعار (المنيو)" else "Menu Items & Prices",
                    subtitle = if (isArabic) "اكتب كل صنف في سطر مع السعر (مثال: فلات وايت - 18 ر.س)" else "Enter each item with price per line",
                    icon = Icons.Default.RestaurantMenu
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.menuItemsText,
                    onValueChange = onMenuItemsChange,
                    minLines = 4,
                    maxLines = 8,
                    placeholder = {
                        Text(
                            """
                            • فلات وايت | Flat White - 18 ر.س
                            • كورتادو | Cortado - 16 ر.س
                            • كيكة الزعفران | Saffron Cake - 28 ر.س
                            """.trimIndent()
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = outlinedFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_menu_items")
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Logo Emoji Picker
                Text(
                    text = if (isArabic) "أيقونة شعار المتجر" else "Shop Emblem Icon",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = WaslTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(emojiList) { emoji ->
                        val isSelected = uiState.logoEmoji == emoji
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) WaslSandGold else WaslSurfaceBeige)
                                .clickable { onLogoEmojiChange(emoji) }
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }
            }
        }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Sticky Bottom Action Bar Container
        Surface(
            color = WaslBgCream,
            shadowElevation = 8.dp,
            border = BorderStroke(1.dp, WaslBorderBeige.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 20.dp)
            ) {
                // Save Status Banner
                AnimatedVisibility(
                    visible = uiState.isSaveSuccess,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = WaslSaudiGreenLight,
                        border = BorderStroke(1.dp, WaslSaudiGreen.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = WaslSaudiGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isArabic) "تم حفظ بيانات المتجر بنجاح على الجهاز!" else "Shop details saved successfully to phone!",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = WaslSaudiGreen,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Action Buttons Row: Equal width, side by side with 12px gap, height 56px, padding 16px, single line centered
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Save Button: Black background, white text, save icon, text "Save Shop"
                    Button(
                        onClick = onSaveClick,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("button_save_form")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isArabic) "حفظ المتجر" else "Save Shop",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Preview Link Button: Gold background #C9A86A, white text, eye icon, text "Preview Link"
                    Button(
                        onClick = onPreviewClick,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC9A86A)
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("button_preview_page")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isArabic) "معاينة الرابط" else "Preview Link",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormFieldLabel(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = WaslSandGold,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = WaslTextPrimary
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = WaslTextSecondary
                )
            }
        }
    }
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = WaslSurfaceWhite,
    unfocusedContainerColor = WaslSurfaceCard,
    focusedBorderColor = WaslSandGold,
    unfocusedBorderColor = WaslBorderBeige,
    focusedTextColor = WaslTextPrimary,
    unfocusedTextColor = WaslTextPrimary
)
