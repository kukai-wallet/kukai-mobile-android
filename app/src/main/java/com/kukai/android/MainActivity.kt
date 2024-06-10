package com.kukai.android

import android.os.Bundle
import android.content.Intent
import android.webkit.WebChromeClient
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent

const val BASE_URL: String = "https://fd8d-79-103-44-127.ngrok-free.app"

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var secondWebView: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadInitialURL()
    }

    private fun loadInitialURL() {
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        val defaultUserAgent = webView.settings.userAgentString
        val customUserAgent = "$defaultUserAgent Kukai/1.0"
        webView.settings.userAgentString = customUserAgent

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && !url.startsWith(BASE_URL) && (url.startsWith("http") || url.startsWith("https"))) {
                    // Open URLs in Custom Tabs
                    openSecondWebView(url)
                    launchCustomTab(url)
                    return true
                }
                return false
            }
        }

        webView.loadUrl(BASE_URL)
    }

    private fun openSecondWebView(url: String) {
        val secondWebViewContainer = findViewById<ViewGroup>(R.id.secondWebViewContainer)

        if (secondWebView == null) {
            secondWebView = WebView(this).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
            }
            
            secondWebViewContainer.addView(secondWebView)
        }

        secondWebView?.loadUrl(url)
        secondWebViewContainer.visibility = ViewGroup.VISIBLE
    }

    private fun launchCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        if (intent != null && intent.action == Intent.ACTION_VIEW) {

            val data: Uri? = intent.data
            if (data != null) {
                val scheme: String? = data.scheme
                val host: String? = data.host

                println("deeplink::$data")

                val newUrl = replaceUri(data.toString())
                secondWebView?.loadUrl(newUrl)
                findViewById<ViewGroup>(R.id.secondWebViewContainer).visibility = ViewGroup.GONE
                }
        }
    }

    private fun replaceUri(uri: String): String {
        return uri.replace("kukai://auth/", "$BASE_URL/serviceworker/redirect?test=123")
    }
}