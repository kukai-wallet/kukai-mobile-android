package com.kukai.android

import android.net.Uri
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CustomTabsRedirectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri: Uri? = intent?.data

        if (uri != null && uri.toString().startsWith("kukai://")) {
            println("deeplink: $uri")

            val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                data = uri
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }

            startActivity(mainActivityIntent)
        }

        finish()
    }
}