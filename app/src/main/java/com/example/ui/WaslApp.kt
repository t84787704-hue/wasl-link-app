package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.FormScreen
import com.example.ui.screens.MenuBottomSheet
import com.example.ui.screens.PreviewScreen
import com.example.ui.screens.QrBottomSheet
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

@Composable
fun WaslApp(viewModel: WaslViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val layoutDirection = if (uiState.isArabicLayout) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            topBar = {
                WaslTopAppBar(
                    isArabic = uiState.isArabicLayout,
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onOpenQr = { viewModel.setShowQrSheet(true) }
                )
            },
            containerColor = WaslBgCream,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) { paddingValues ->
            if (uiState.isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator(color = WaslSandGold)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Top Navigation Switcher Tabs
                    WaslTabSwitcher(
                        selectedTab = uiState.activeTab,
                        isArabic = uiState.isArabicLayout,
                        onTabSelected = { viewModel.setActiveTab(it) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )

                    // Main Screen Area
                    Crossfade(
                        targetState = uiState.activeTab,
                        label = "ScreenTransition",
                        modifier = Modifier.weight(1f)
                    ) { tabIndex ->
                        when (tabIndex) {
                            0 -> FormScreen(
                                uiState = uiState,
                                onShopNameChange = viewModel::onShopNameChange,
                                onShopNameArabicChange = viewModel::onShopNameArabicChange,
                                onWhatsappChange = viewModel::onWhatsappNumberChange,
                                onLocationUrlChange = viewModel::onLocationUrlChange,
                                onMenuItemsChange = viewModel::onMenuItemsTextChange,
                                onLogoEmojiChange = viewModel::onLogoEmojiChange,
                                onPresetSelect = viewModel::loadSampleTemplate,
                                onSaveClick = viewModel::saveProfile,
                                onPreviewClick = {
                                    viewModel.saveProfile()
                                    viewModel.setActiveTab(1)
                                },
                                onShareQr = { viewModel.setShowQrSheet(true) },
                                onDetectLocationClick = { viewModel.detectCurrentLocation(context) },
                                onOpenMapPicker = { viewModel.openMapPicker(context) }
                            )
                            1 -> PreviewScreen(
                                uiState = uiState,
                                onOpenWhatsApp = viewModel::openWhatsApp,
                                onOpenGoogleMaps = viewModel::openGoogleMaps,
                                onShowMenu = { viewModel.setShowMenuSheet(true) },
                                onShareStore = viewModel::shareStoreLink,
                                onShareQr = { viewModel.setShowQrSheet(true) },
                                onEditFormClick = { viewModel.setActiveTab(0) }
                            )
                        }
                    }
                }
            }

            // Menu Bottom Sheet Modal
            if (uiState.showMenuSheet) {
                MenuBottomSheet(
                    shopNameArabic = uiState.shopNameArabic,
                    shopName = uiState.shopName,
                    whatsappNumber = uiState.whatsappNumber,
                    menuText = uiState.menuItemsText,
                    isArabic = uiState.isArabicLayout,
                    onDismiss = { viewModel.setShowMenuSheet(false) }
                )
            }

            // QR Code Bottom Sheet Modal
            if (uiState.showQrSheet) {
                QrBottomSheet(
                    uiState = uiState,
                    isArabic = uiState.isArabicLayout,
                    onDismiss = { viewModel.setShowQrSheet(false) }
                )
            }
        }
    }
}

@Composable
fun WaslTopAppBar(
    isArabic: Boolean,
    onToggleLanguage: () -> Unit,
    onOpenQr: () -> Unit
) {
    Surface(
        color = WaslBgCream,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WaslPrimaryCharcoal,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "و",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WaslSandGold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "وَصْل",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = WaslPrimaryCharcoal
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "• Wasl",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = WaslSandGold
                        )
                    }
                    Text(
                        text = if (isArabic) "صفحة روابط المتاجر السعودية" else "Saudi Shop Link Hub",
                        style = MaterialTheme.typography.labelSmall,
                        color = WaslTextSecondary
                    )
                }
            }

            // Top Actions: QR Code Quick Action + Language Switcher
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // QR Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = WaslSurfaceWhite,
                    border = BorderStroke(1.dp, WaslBorderBeige),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onOpenQr)
                        .testTag("button_top_qr")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = "Show QR",
                            tint = WaslSandGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "QR",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = WaslPrimaryCharcoal
                        )
                    }
                }

                // Language Switcher Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = WaslSurfaceWhite,
                    border = BorderStroke(1.dp, WaslBorderBeige),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onToggleLanguage)
                        .testTag("button_toggle_language")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Switch Language",
                            tint = WaslPrimaryCharcoal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isArabic) "EN" else "عربي",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = WaslPrimaryCharcoal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WaslTabSwitcher(
    selectedTab: Int,
    isArabic: Boolean,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WaslSurfaceBeige),
        border = BorderStroke(1.dp, WaslBorderBeige),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            // Tab 0: Create Page Form
            val tab0Selected = selectedTab == 0
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (tab0Selected) WaslSurfaceWhite else Color.Transparent,
                shadowElevation = if (tab0Selected) 2.dp else 0.dp,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onTabSelected(0) }
                    .testTag("tab_edit_form")
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = if (tab0Selected) WaslPrimaryCharcoal else WaslTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "تعديل البيانات" else "Edit Form",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (tab0Selected) FontWeight.Bold else FontWeight.Medium,
                        color = if (tab0Selected) WaslPrimaryCharcoal else WaslTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Tab 1: Preview Page
            val tab1Selected = selectedTab == 1
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (tab1Selected) WaslSurfaceWhite else Color.Transparent,
                shadowElevation = if (tab1Selected) 2.dp else 0.dp,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onTabSelected(1) }
                    .testTag("tab_preview_page")
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = if (tab1Selected) WaslPrimaryCharcoal else WaslTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "معاينة المتجر" else "Preview Page",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (tab1Selected) FontWeight.Bold else FontWeight.Medium,
                        color = if (tab1Selected) WaslPrimaryCharcoal else WaslTextSecondary
                    )
                }
            }
        }
    }
}
