package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.WaslUiState
import com.example.ui.theme.WaslBgCream
import com.example.ui.theme.WaslBorderBeige
import com.example.ui.theme.WaslBorderDark
import com.example.ui.theme.WaslGoldLight
import com.example.ui.theme.WaslMapsBlue
import com.example.ui.theme.WaslPrimaryCharcoal
import com.example.ui.theme.WaslSandGold
import com.example.ui.theme.WaslSaudiGreen
import com.example.ui.theme.WaslSaudiGreenLight
import com.example.ui.theme.WaslSurfaceBeige
import com.example.ui.theme.WaslSurfaceCard
import com.example.ui.theme.WaslSurfaceWhite
import com.example.ui.theme.WaslTextPrimary
import com.example.ui.theme.WaslTextSecondary

val emojiOptions = listOf("☕", "🥐", "👗", "🌸", "🍔", "🍰", "🌿", "💎", "🍕", "🧕", "🪔", "🛍️")

@Composable
fun FormScreen(
    uiState: WaslUiState,
    onShopNameChange: (String) -> Unit,
    onShopNameArabicChange: (String) -> Unit,
    onWhatsappChange: (String) -> Unit,
    onDefaultGreetingChange: (String) -> Unit,
    onLocationUrlChange: (String) -> Unit,
    onMenuItemsChange: (String) -> Unit,
    onLogoEmojiChange: (String) -> Unit,
    onPresetSelect: (String) -> Unit,
    onSaveClick: () -> Unit,
    onPreviewClick: () -> Unit,
    onShareQr: () -> Unit,
    onDetectLocationClick: () -> Unit,
    onOpenMapPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("form_screen")
        ) {
            // Header Banner Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                border = BorderStroke(1.dp, WaslBorderBeige),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(WaslSurfaceBeige)
                        ) {
                            Text(text = uiState.logoEmoji.ifBlank { "☕" }, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.form_header_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WaslTextPrimary
                            )
                            Text(
                                text = stringResource(R.string.form_header_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = WaslTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Preset Templates Row
                    Text(
                        text = stringResource(R.string.quick_templates_title),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = WaslSandGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WaslSurfaceBeige,
                            border = BorderStroke(1.dp, WaslBorderBeige),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onPresetSelect("cafe") }
                                .testTag("button_preset_cafe")
                        ) {
                            Text(
                                text = "☕ " + stringResource(R.string.template_cafe),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = WaslTextPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WaslSurfaceBeige,
                            border = BorderStroke(1.dp, WaslBorderBeige),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onPresetSelect("bakery") }
                                .testTag("button_preset_bakery")
                        ) {
                            Text(
                                text = "🥐 " + stringResource(R.string.template_bakery),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = WaslTextPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WaslSurfaceBeige,
                            border = BorderStroke(1.dp, WaslBorderBeige),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onPresetSelect("boutique") }
                                .testTag("button_preset_boutique")
                        ) {
                            Text(
                                text = "👗 " + stringResource(R.string.template_abayas),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = WaslTextPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WaslSurfaceBeige,
                            border = BorderStroke(1.dp, WaslBorderBeige),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onPresetSelect("perfume") }
                                .testTag("button_preset_perfume")
                        ) {
                            Text(
                                text = "🌸 " + stringResource(R.string.template_perfumes),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = WaslTextPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Field 1: English Shop Name
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                border = BorderStroke(1.dp, WaslBorderBeige),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    FormFieldLabel(
                        title = stringResource(R.string.field_shop_name_en_title),
                        subtitle = stringResource(R.string.field_shop_name_en_subtitle),
                        icon = Icons.Default.Storefront
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.shopName,
                        onValueChange = onShopNameChange,
                        placeholder = { Text(stringResource(R.string.field_shop_name_en_hint), color = WaslTextSecondary.copy(alpha = 0.6f)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = outlinedFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_shop_name")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Field 2: Arabic Shop Name
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                border = BorderStroke(1.dp, WaslBorderBeige),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    FormFieldLabel(
                        title = stringResource(R.string.field_shop_name_ar_title),
                        subtitle = stringResource(R.string.field_shop_name_ar_subtitle),
                        icon = Icons.Default.Edit
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.shopNameArabic,
                        onValueChange = onShopNameArabicChange,
                        placeholder = { Text(stringResource(R.string.field_shop_name_ar_hint), color = WaslTextSecondary.copy(alpha = 0.6f)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = outlinedFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_shop_name_arabic")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Field 3: WhatsApp Number
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                border = BorderStroke(1.dp, WaslBorderBeige),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    FormFieldLabel(
                        title = stringResource(R.string.field_whatsapp_title),
                        subtitle = stringResource(R.string.field_whatsapp_subtitle),
                        icon = Icons.Default.Phone
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = WaslSaudiGreenLight,
                            border = BorderStroke(1.dp, WaslSaudiGreen.copy(alpha = 0.4f)),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    text = "🇸🇦 +966",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = WaslSaudiGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        OutlinedTextField(
                            value = uiState.whatsappNumber,
                            onValueChange = onWhatsappChange,
                            placeholder = { Text(stringResource(R.string.field_whatsapp_hint), color = WaslTextSecondary.copy(alpha = 0.6f)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = outlinedFieldColors(),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_whatsapp_number")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Field 4: Default Greeting Message
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                border = BorderStroke(1.dp, WaslBorderBeige),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    FormFieldLabel(
                        title = stringResource(R.string.field_greeting_title),
                        subtitle = stringResource(R.string.field_greeting_subtitle),
                        icon = Icons.Default.FormatQuote
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.defaultGreeting,
                        onValueChange = onDefaultGreetingChange,
                        placeholder = { Text(stringResource(R.string.field_greeting_hint), color = WaslTextSecondary.copy(alpha = 0.6f)) },
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(14.dp),
                        colors = outlinedFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_default_greeting")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Suggested quick greeting chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = WaslSurfaceBeige,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onDefaultGreetingChange("السلام عليكم، أود الطلب من القائمة")
                                }
                        ) {
                            Text(
                                text = stringResource(R.string.greeting_suggestion_order),
                                style = MaterialTheme.typography.labelSmall,
                                color = WaslTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = WaslSurfaceBeige,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onDefaultGreetingChange("مرحباً، أود الاستفسار عن التوصيل والأسعار")
                                }
                        ) {
                            Text(
                                text = stringResource(R.string.greeting_suggestion_delivery),
                                style = MaterialTheme.typography.labelSmall,
                                color = WaslTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Field 5: Location URL + Auto-GPS Detection
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                border = BorderStroke(1.dp, WaslBorderBeige),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    FormFieldLabel(
                        title = stringResource(R.string.field_location_title),
                        subtitle = stringResource(R.string.field_location_subtitle),
                        icon = Icons.Default.LocationOn
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.locationUrl,
                        onValueChange = onLocationUrlChange,
                        placeholder = { Text(stringResource(R.string.field_location_hint), color = WaslTextSecondary.copy(alpha = 0.6f)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = outlinedFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_location_url")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Location Action Buttons: Auto-Detect & Map Picker
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Auto-detect GPS button
                        Button(
                            onClick = onDetectLocationClick,
                            enabled = !uiState.isDetectingLocation,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WaslSaudiGreen,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(44.dp)
                                .testTag("button_detect_location")
                        ) {
                            if (uiState.isDetectingLocation) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.btn_detecting_location),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.btn_use_current_location),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Open Google Maps Picker
                        OutlinedButton(
                            onClick = onOpenMapPicker,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, WaslSandGold),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = WaslSandGold
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("button_map_picker")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(R.string.btn_pick_on_map),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Field 6: Menu Items & Pricing
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                border = BorderStroke(1.dp, WaslBorderBeige),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    FormFieldLabel(
                        title = stringResource(R.string.field_menu_title),
                        subtitle = stringResource(R.string.field_menu_subtitle),
                        icon = Icons.Default.RestaurantMenu
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.menuItemsText,
                        onValueChange = onMenuItemsChange,
                        placeholder = {
                            Text(
                                stringResource(R.string.field_menu_hint),
                                color = WaslTextSecondary.copy(alpha = 0.6f)
                            )
                        },
                        minLines = 4,
                        maxLines = 10,
                        shape = RoundedCornerShape(14.dp),
                        colors = outlinedFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_menu_items")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Field 7: Logo Emoji Picker
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WaslSurfaceWhite),
                border = BorderStroke(1.dp, WaslBorderBeige),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    FormFieldLabel(
                        title = stringResource(R.string.field_emblem_title),
                        subtitle = "",
                        icon = Icons.Default.EmojiEmotions
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(emojiOptions) { emoji ->
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
                                text = stringResource(R.string.save_success_banner),
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
                                text = stringResource(R.string.btn_save_shop),
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
                                text = stringResource(R.string.btn_preview_link),
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
    icon: ImageVector
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
