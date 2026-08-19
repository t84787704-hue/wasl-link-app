package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.screens.FormScreen
import com.example.ui.screens.MenuBottomSheet
import com.example.ui.screens.PreviewScreen
import com.example.ui.screens.QrBottomSheet
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.WaslGoldLight

@Composable
fun WaslApp(viewModel: WaslViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            WaslTopAppBar(
                isDarkMode = uiState.isDarkMode,
                onOpenLanguageSettings = { viewModel.setActiveTab(2) },
                onToggleDarkMode = { viewModel.toggleDarkMode() },
                onOpenQr = { viewModel.setShowQrSheet(true) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
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
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                    onTabSelected = { viewModel.setActiveTab(it) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
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
                            onDefaultGreetingChange = viewModel::onDefaultGreetingChange,
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
                        2 -> SettingsScreen(
                            uiState = uiState,
                            onLanguageSelect = viewModel::selectLanguage,
                            onToggleDarkMode = viewModel::toggleDarkMode,
                            onToggleDynamicColor = viewModel::toggleDynamicColor
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
                isArabic = false,
                onDismiss = { viewModel.setShowMenuSheet(false) }
            )
        }

        // QR Code Bottom Sheet Modal
        if (uiState.showQrSheet) {
            QrBottomSheet(
                uiState = uiState,
                isArabic = false,
                onDismiss = { viewModel.setShowQrSheet(false) }
            )
        }
    }
}

@Composable
fun WaslTopAppBar(
    isDarkMode: Boolean,
    onOpenLanguageSettings: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onOpenQr: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
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
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.wasl_brand_arabic),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.wasl_brand_sub),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = stringResource(R.string.app_tagline),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Top Actions: Dark Mode Toggle + QR Quick Action + Language Switcher
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Dark Mode Toggle Button
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onToggleDarkMode)
                        .testTag("button_toggle_dark_mode")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Light Mode" else "Dark Mode",
                            tint = if (isDarkMode) WaslGoldLight else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // QR Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onOpenQr)
                        .testTag("button_top_qr")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = "Show QR",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "QR",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Language Switcher / Settings Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onOpenLanguageSettings)
                        .testTag("button_open_language")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = stringResource(R.string.language_title),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "🌐",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
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
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
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
                color = if (tab0Selected) MaterialTheme.colorScheme.surface else Color.Transparent,
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
                        tint = if (tab0Selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.tab_edit_form),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (tab0Selected) FontWeight.Bold else FontWeight.Medium,
                        color = if (tab0Selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Tab 1: Preview Page
            val tab1Selected = selectedTab == 1
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (tab1Selected) MaterialTheme.colorScheme.surface else Color.Transparent,
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
                        tint = if (tab1Selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.tab_preview_page),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (tab1Selected) FontWeight.Bold else FontWeight.Medium,
                        color = if (tab1Selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Tab 2: Settings
            val tab2Selected = selectedTab == 2
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (tab2Selected) MaterialTheme.colorScheme.surface else Color.Transparent,
                shadowElevation = if (tab2Selected) 2.dp else 0.dp,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onTabSelected(2) }
                    .testTag("tab_settings")
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = if (tab2Selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.tab_settings),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (tab2Selected) FontWeight.Bold else FontWeight.Medium,
                        color = if (tab2Selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
