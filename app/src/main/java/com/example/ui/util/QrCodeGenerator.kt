package com.example.ui.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.File
import java.io.FileOutputStream

object QrCodeGenerator {

    /**
     * Generates a square QR Code Bitmap from text
     */
    fun generateQrBitmap(
        content: String,
        size: Int = 512,
        darkColor: Int = android.graphics.Color.BLACK,
        lightColor: Int = android.graphics.Color.WHITE
    ): Bitmap? {
        if (content.isBlank()) return null
        return try {
            val hints = hashMapOf<EncodeHintType, Any>().apply {
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
                put(EncodeHintType.MARGIN, 1)
            }
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) darkColor else lightColor
                }
            }
            Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Generates a branded shareable store card with QR Code, Logo, Store name and Wasl branding
     */
    fun generateBrandedStoreCard(
        context: Context,
        shopName: String,
        category: String,
        city: String,
        logoEmoji: String,
        qrContent: String
    ): Bitmap? {
        val qrBitmap = generateQrBitmap(qrContent, size = 420) ?: return null
        val cardWidth = 720
        val cardHeight = 1000

        val bitmap = Bitmap.createBitmap(cardWidth, cardHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Background
        val bgPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#FBF8F2")
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawRect(0f, 0f, cardWidth.toFloat(), cardHeight.toFloat(), bgPaint)

        // Inner Card with border
        val innerMargin = 36f
        val innerCardPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val borderPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#C9A86A")
            style = Paint.Style.STROKE
            strokeWidth = 4f
            isAntiAlias = true
        }
        val cardRect = RectF(
            innerMargin,
            innerMargin,
            cardWidth - innerMargin,
            cardHeight - innerMargin
        )
        canvas.drawRoundRect(cardRect, 36f, 36f, innerCardPaint)
        canvas.drawRoundRect(cardRect, 36f, 36f, borderPaint)

        // Emoji / Logo Header
        val emojiPaint = Paint().apply {
            textSize = 72f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText(logoEmoji.ifBlank { "☕" }, cardWidth / 2f, 140f, emojiPaint)

        // Shop Name
        val namePaint = Paint().apply {
            color = android.graphics.Color.parseColor("#1B1A17")
            textSize = 40f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val displayName = shopName.ifBlank { "Wasl Market" }
        canvas.drawText(displayName, cardWidth / 2f, 210f, namePaint)

        // Category & City subtitle
        val subPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#666056")
            textSize = 26f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val displaySub = when {
            category.isNotBlank() && city.isNotBlank() -> "$category • $city"
            category.isNotBlank() -> category
            city.isNotBlank() -> city
            else -> "Saudi Arabia"
        }
        canvas.drawText(displaySub, cardWidth / 2f, 255f, subPaint)

        // Draw QR Code
        val qrLeft = (cardWidth - qrBitmap.width) / 2f
        val qrTop = 290f
        canvas.drawBitmap(qrBitmap, qrLeft, qrTop, null)
        try { qrBitmap.recycle() } catch (e: Exception) {}

        // Scan Prompt & Link
        val promptPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#0F6B4B")
            textSize = 28f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("امسح الرمز لزيارة المتجر والتواصل فوراً", cardWidth / 2f, 750f, promptPaint)

        val linkDisplay = qrContent.removePrefix("https://").removePrefix("http://")
        val linkPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#1B1A17")
            textSize = 24f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText(linkDisplay, cardWidth / 2f, 795f, linkPaint)

        val promptEnPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#8A8478")
            textSize = 20f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("Scan to visit store & chat on WhatsApp", cardWidth / 2f, 835f, promptEnPaint)

        // Footer Wasl Tag
        val footerPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#C9A86A")
            textSize = 24f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("✨ وصل | Wasl Storefront", cardWidth / 2f, 895f, footerPaint)

        return bitmap
    }

    /**
     * Saves bitmap to app cache and shares via Android Intent
     */
    fun shareQrImage(
        context: Context,
        bitmap: Bitmap,
        shareText: String
    ) {
        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "wasl_store_qr_${System.currentTimeMillis()}.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()

            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, shareText)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val chooser = Intent.createChooser(intent, "مشاركة رمز الاستجابة السريعة | Share QR Code")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "تعذر مشاركة الصورة: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Copies text to Android clipboard
     */
    fun copyToClipboard(context: Context, text: String, label: String = "Wasl Store Link") {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "تم نسخ الرابط بنجاح! | Link copied", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
