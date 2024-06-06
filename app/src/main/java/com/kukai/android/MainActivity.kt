package com.kukai.android

import android.os.Bundle
import android.webkit.WebChromeClient
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        val defaultUserAgent = webView.settings.userAgentString
        val customUserAgent = "$defaultUserAgent Kukai/1.0"
        webView.settings.userAgentString = customUserAgent

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && !url.startsWith("https://wallet") && (url.startsWith("http") || url.startsWith("https"))) {
                    // Open URLs in Custom Tabs
                    launchCustomTab(url)
                    return true
                }
                return false
            }
        }

        webView.loadUrl("https://kukai.app")
    }

    private fun launchCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}