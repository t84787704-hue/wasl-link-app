package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

/**
 * Big Rounded Action Button for the Preview Page (WhatsApp, Location, Menu)
 */
@Composable
fun WaslBigActionButton(
    icon: ImageVector,
    iconColor: Color,
    iconBgColor: Color,
    titleArabic: String,
    titleEnglish: String,
    subtitle: String,
    badgeText: String? = null,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = WaslSurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        border = BorderStroke(1.5.dp, WaslBorderBeige),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 84.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            // Icon container with soft circular pastel background
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(iconBgColor)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = titleArabic,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = WaslTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (badgeText != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = WaslSaudiGreenLight
                        ) {
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                color = WaslSaudiGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = titleEnglish,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = WaslSandGold
                )

                if (subtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = WaslTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Subtle arrow indicator
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(WaslSurfaceBeige)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = WaslTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Shop Logo Badge with elegant warm border and shadow
 */
@Composable
fun ShopLogoAvatar(
    emoji: String,
    size: Int = 96,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    listOf(WaslSurfaceWhite, WaslSurfaceBeige)
                )
            )
            .background(WaslSurfaceWhite)
    ) {
        // Inner decorative ring
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(3.dp, WaslBorderBeige),
            modifier = Modifier.size((size - 4).dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = emoji,
                    fontSize = (size * 0.44f).sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Verified Saudi Shop Badge
 */
@Composable
fun SaudiVerifiedBadge(
    isArabic: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = WaslSaudiGreenLight,
        border = BorderStroke(1.dp, WaslSaudiGreen.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = WaslSaudiGreen,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isArabic) "متجر معتمد 🇸🇦" else "Verified Saudi Store 🇸🇦",
                style = MaterialTheme.typography.labelSmall,
                color = WaslSaudiGreen,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Quick Template Preset Chip for fast filling
 */
@Composable
fun TemplatePresetChip(
    title: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) WaslPrimaryCharcoal else WaslSurfaceWhite,
        border = BorderStroke(1.dp, if (isSelected) WaslPrimaryCharcoal else WaslBorderBeige),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Text(text = emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else WaslTextPrimary
            )
        }
    }
}
