package com.kukai.android

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CustomTabsRedirectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the redirect URI
        val uri: Uri? = intent?.data

        if (uri != null && uri.toString().startsWith("kukai://")) {
            // Extract information from the URI and proceed with login
            println("deeplink: $uri")
        }
        // Close the activity
        finish()
    }
}