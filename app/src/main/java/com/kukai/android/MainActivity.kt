package com.kukai.android

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.webkit.WebChromeClient
import android.net.Uri
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.Network
import com.web3auth.core.types.*
import android.view.ViewGroup
//import org.web3j.crypto.Credentials
//import org.web3j.crypto.Hash
//import org.web3j.crypto.RawTransaction
//import org.web3j.crypto.Sign
//import org.web3j.crypto.TransactionEncoder
//import org.web3j.protocol.Web3j
//import org.web3j.protocol.core.DefaultBlockParameterName
//import org.web3j.protocol.core.methods.response.EthChainId
//import org.web3j.protocol.core.methods.response.EthGetBalance
//import org.web3j.protocol.core.methods.response.EthGetTransactionCount
//import org.web3j.protocol.core.methods.response.EthSendTransaction
//import org.web3j.protocol.http.HttpService
//import org.web3j.utils.Convert
//import org.web3j.utils.Numeric
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture
import kotlin.math.log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent

const val BASE_URL: String = "https://b850-109-54-39-42.ngrok-free.app"

class MainActivity : AppCompatActivity() {

    private lateinit var web3Auth: Web3Auth
    companion object {
        var HAS_LOADED = false
    }

    private lateinit var webView: WebView
    private var secondWebView: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadInitialURL()

        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            openWeb3Auth()
        }

        web3Auth = Web3Auth(
            Web3AuthOptions(
                context = this,
                clientId = "BKZdK9AS4MBhdhcxVeOXGdwz6bAnb6tr2oUUol3G07aICtDwvDaJfnITgRKGURF-IDTfNWnGlWIjKeIzhd5ZuDk",
                network = Network.TESTNET,
                redirectUrl = Uri.parse("kukai://auth"),
//                whiteLabel = WhiteLabelData(  // Optional param
//                    "Web3Auth Sample App", null, null, "en", true,
//                    hashMapOf(
//                        "primary" to "#123456"
//                    )
//                )
            )
        )

        // Handle user signing in when app is not alive
        web3Auth.setResultUrl(intent?.data)

        // Call initialize function to get private key and user information value without relogging in user if a user has an active session
        val sessionResponse: CompletableFuture<Void> = web3Auth.initialize()
        sessionResponse.whenComplete { _, error ->
            if (error == null) {
                println("PrivKey: " + web3Auth.getPrivkey())
                println("ed25519PrivKey: " + web3Auth.getEd25519PrivKey())
                println("Web3Auth UserInfo" + web3Auth.getUserInfo())
            } else {
                // render error UI
            }
        }
    }

    private fun openWeb3Auth() {
        val selectedLoginProvider = Provider.GOOGLE   // Can be Google, Facebook, Twitch etc
        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> = web3Auth.login(LoginParams(selectedLoginProvider))

        loginCompletableFuture.whenComplete { _, error ->
            if (error == null) {
                // render logged in UI
                println("PrivKey: " + web3Auth.getPrivkey())
                println("ed25519PrivKey: " + web3Auth.getEd25519PrivKey())
                println("Web3Auth UserInfo" + web3Auth.getUserInfo())
            } else {
                // render login error UI
            }

        }
    }

    private fun loadInitialURL() {
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        val defaultUserAgent = webView.settings.userAgentString
//        val customUserAgent = "$defaultUserAgent Kukai/1.0"
//        webView.settings.userAgentString = customUserAgent

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.contains("accounts.google.com")) {
                    launchCustomTab("$BASE_URL?provider=google")
                    return true
                }
//                if (url != null && !url.startsWith(BASE_URL) && (url.startsWith("http") || url.startsWith("https"))) {
//                    // Open URLs in Custom Tabs
////                    openSecondWebView(url)
//
////                    launchCustomTab(url)
//                    return true
//                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

//                if (HAS_LOADED) {
//                    return
//                }
                // Execute JavaScript here

            }
        }

        webView.loadUrl(BASE_URL)

//        val jsCode = """
//            (function() {
//                const data = {"type":"TorusWallet","localStorageId":1718060960731,"data":{"totalBalanceXTZ":19653073,"totalBalanceUSD":16.1253463965,"XTZrate":0.8205,"implicitAccounts":[{"balanceXTZ":19653073,"balanceUSD":16.1253463965,"activities":[{"type":"transaction","block":"BMELXHV4zVCqrQq5BkHPo1LQXqQ5HAYBmPbDH5K1vRHCw5ZrthP","status":1,"amount":"6872","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"ooYydRx8uLhnuwGHZsi3P4gYw2gGSU1bJ7WA1L33EuyFA1QVztY","counter":19166145,"timestamp":1718048355000,"entrypoint":"","opId":"t1136125298081792"},{"type":"transaction","block":"BKry5hnVJu9proCRRz3UoXt4tJSf17Wk5AriLbU8NwskDckMQhd","status":1,"amount":"7900","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"oogvDugCs2jfcKEd9orwM1DH92g8PVWC7B4YGd8SSzC4A91cqYo","counter":19165723,"timestamp":1717801805000,"entrypoint":"","opId":"t1130797072384000"},{"type":"transaction","block":"BMLMy9rfnR45wuzxo9J9cuqwP6LWJrBjGqJ7sSTTZcRQgt5vKEW","status":1,"amount":"6567","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"ootitBsB6Jc9gtj8uqejR4FH4Zbm4SRn7uUnb25E8sX35s6CtcV","counter":19165303,"timestamp":1717555130000,"entrypoint":"","opId":"t1125742466301952"},{"type":"transaction","block":"BKuhPFS7u2k3kk2VvmocyLCpSEsCZNpFP9VaF7HipeY1PcMQ785","status":1,"amount":"7536","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"oortXHhpqmxj4PqoScxxwh6dYgZ6ZiK563bhR8gP5fTp6ebwqLG","counter":19164880,"timestamp":1717298170000,"entrypoint":"","opId":"t1121746411323392"},{"type":"transaction","block":"BLEPkZeDcwxSfv6yzDtaCTaZKwAcdTLJtGf7C3HYesBry59VjSK","status":1,"amount":"8049","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"ookfQG5GbejMJhckrwM35wKxw9F1bW56aK24fRbHvzAx9hN7FVW","counter":19164463,"timestamp":1717101024000,"entrypoint":"","opId":"t1118723864788992"},{"type":"transaction","block":"BM1cwbd1SUffYNk8dxEYqcEX16zUdUBh5rgodsSGLrDD8ybTmVh","status":1,"amount":"7297","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"ooVhYYGwNBWseh8WWJgDg3R9nHPe4CY74Ca4pHYRFKwQ5vvsoAU","counter":19164042,"timestamp":1716800422000,"entrypoint":"","opId":"t1113868517507072"},{"type":"transaction","block":"BLy7eEuEN5P7WfH5PmRbSsM8RVeYYYjYUYNxvVfanbepJxxeB8z","status":1,"amount":"6589","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"opFoyH1556Tg1orKmiDUrtMpUmHiRm4boKrGjrh91v7mAWtqhp5","counter":19163619,"timestamp":1716553698000,"entrypoint":"","opId":"t1109924574658560"},{"type":"transaction","block":"BLn9nj1frMK7uuLGMPBApHN9SJxbtFWijHSqxR1Ey1Ag7A1Cv8V","hash":"ookA2uLucTkHCjyVnHCF6FwRWuuQ4eab3Wf7gCcoiBe1Zo3uuKx","status":1,"amount":"1","tokenId":"KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi:1611769","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1716401022000,"opId":"t1107539524583424"},{"type":"transaction","block":"BLn9nj1frMK7uuLGMPBApHN9SJxbtFWijHSqxR1Ey1Ag7A1Cv8V","status":1,"amount":"0","source":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"destination":{"alias":"FXHASH Generative Tokens v2","address":"KT1BJC12dG17CVvPKJ1VYaNnaT5mzfnUTwXv"},"hash":"ookA2uLucTkHCjyVnHCF6FwRWuuQ4eab3Wf7gCcoiBe1Zo3uuKx","counter":46425836,"timestamp":1716401022000,"entrypoint":"mint","opId":"t1107539523534848"},{"type":"transaction","block":"BMFGe67RX77Je4XwindWShG67mH528S7Dwme8S2jmXrdpZysjfa","status":1,"amount":"6972","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"ooDfz2GJPmLESPkfMW1uLEfAPGUExoXT8wZKjSVzocmYrmYvvnF","counter":19163199,"timestamp":1716306098000,"entrypoint":"","opId":"t1106039386669056"},{"type":"transaction","block":"BMW6FkaJ4oFzcQ2xGbJ4x26pW8jLfsxfe9AQU617CNRxpYGwCKK","status":1,"amount":"6450","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"oo8gYvs9sfifn7ppY96pipuUVHS7qYWEuQwc7fVHE8Zqm4qTfBx","counter":19162777,"timestamp":1716058154000,"entrypoint":"","opId":"t1102112154976256"},{"type":"transaction","block":"BLFcjMazuECWm38nNMAzesRHUjNqy2dJsEPD8sLsfvFMrtjK9QS","status":1,"amount":"6390","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"opT4evRHfwYf96nF1cauHbQiLGKVpLeSQVtC85hG3K3XzmGF5W5","counter":19162355,"timestamp":1715813697000,"entrypoint":"","opId":"t1098244185653248"},{"type":"transaction","block":"BMbx7T9KLrwFjWp18y6opexbGr9qUp6uBiMuqovuB87TU9byTQu","status":1,"amount":"8581","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"oo49DkTyeGfXAXdd3qGvcwX2oSAHCvT81LBPxBy3ajhfuBwVMhJ","counter":19161934,"timestamp":1715570939000,"entrypoint":"","opId":"t1094294291087360"},{"type":"transaction","block":"BMXFNLa5UgxQLFjq6412rwgE4hyCpjMyh9uygLzRaPpETdSqs6N","status":1,"amount":"8074","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"opWnxVBm27SXr4J7kZ9EGPQLnLYm1XRcfqaAFFw9qF4iYjRrWwr","counter":19161512,"timestamp":1715323928000,"entrypoint":"","opId":"t1090338917711872"},{"type":"transaction","block":"BMazFmJzugjYzbS1riL1uZ8KWzkjuCGsKsHnKEnosaAvA8JZrws","status":1,"amount":"5692","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"opX6Yzx5EokKAizTijCbcAD2WYoDVKgcmgiUvaeAso59DA1LteJ","counter":19161093,"timestamp":1715077530000,"entrypoint":"","opId":"t1086416985522176"},{"type":"transaction","block":"BLps9zWQrXYJNC5RkXLQsQqoLkaguvNE92xLtBdMZhL5Pcy9HFS","status":1,"amount":"6457","source":{"alias":"Baking Benjamins Payouts","address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"hash":"ooZa6Nh2RAFKZtAmDKfzPfzvzjY9kgT3PwXi8xikmnMDcMK5913","counter":19160672,"timestamp":1714831140000,"entrypoint":"","opId":"t1082415328002048"},{"type":"transaction","block":"BLupBMzZx6st2tqtSdNxyg5eBxXrKS76Tm2CHkefPoRBgcG8qSa","hash":"ongpxdpR91hQofF1ahXHf2aEDQwzaKM5WhaHGtigaz1LFT6Sgsi","status":1,"amount":"1","tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:157","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1714773826000,"opId":"t1081491925762048"},{"type":"transaction","block":"BKnwsUgszMRb33sPaFzUopnzdHpSce5hY3sPyT1ULsTMMbvuo1N","hash":"opEKdhTQVWq4zEEsVapCLC6Y2RBhTxdmjbX5xh2VmWGsjTBuER2","status":1,"amount":"1","tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:162","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1714773691000,"opId":"t1081489865310208"},{"type":"transaction","block":"BKk1SzQrF18LiiFdVtbhBeL1yMS6fu5wnvwXWXJV5HYdm9Dyude","hash":"opWnXNs5WnyMPHo7mDWX6yShC9mEt9SP18NzzgqwxJR8goL2CZa","status":1,"amount":"51501000000","tokenId":"KT1Avad9GZXRSoKzUukD5qSen44WxPRGsQCN:0","source":{"address":"tz1Ltx73buQXZ8y4vSydrss1sakZ83UXF6Cq"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1712419053000,"opId":"t1043112004681728"},{"type":"transaction","block":"BLtALAyAUd4uYC1yzgfWQbSpJJcC4unUn3HLDDLgKFXs9Je2JGk","hash":"ooALikPbdEW36DRKxaDTTdPCBh4EmbkHPbht3SFG9c2y8V8gyH1","status":1,"amount":"8888000000","tokenId":"KT1Hc6KSHp3pLLiCXUkuHva62PRJQ8JmyAcP:0","source":{"address":"tz1dPVCJvoNR2V537ix9H8AaFkWt8vMjE4vb"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1712042042000,"opId":"t1036886338437120"},{"type":"transaction","block":"BLKMJEJ1ni9sEFBYSNT2FRVKHym4bjU9sYRtHZ46zeaxVp5VAtV","hash":"oofbGvqmGofAMQxsG4P89tCqs6hJdfEWuVTh9qxvfmZLAmSbg79","status":1,"amount":"10","tokenId":"KT19AGCrw6ub2nKV46mN64k8RmiRMfpgah2B:0","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711650242000,"opId":"t1030435712794624"},{"type":"transaction","block":"","hash":"","status":1,"amount":"200000000000","tokenId":"KT1FE8LS1D4DFwEjLKLgVKDFNF5w2j3vBDyK:0","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711628582000},{"type":"transaction","block":"","hash":"","status":1,"amount":"200000000000","tokenId":"KT1PegvRtG4LTWGjNx8bswVEvqKNC1FBZBjL:0","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711628522000},{"type":"transaction","block":"BMMf9eE21qZGhjt7NHimgug8JLFp9Ncdn3q5dTwUh6aYaxiHBwc","hash":"oogesvogwPFh8vSCZsHk62B2A8gXp6NbEJTHhQYD2JNrzbEw6aa","status":1,"amount":"10000000000000000","tokenId":"KT1K9gCRgaLRFKTErYt1wVxA3Frb9FjasjTV:0","source":{"address":"KT1CojtgtVHVarS135fnV3y4z8TiKXrsRHJr"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711546022000,"opId":"t1028717233045504"},{"type":"transaction","block":"BMMf9eE21qZGhjt7NHimgug8JLFp9Ncdn3q5dTwUh6aYaxiHBwc","hash":"oogesvogwPFh8vSCZsHk62B2A8gXp6NbEJTHhQYD2JNrzbEw6aa","status":1,"amount":"9916995342702661727255967498156329","tokenId":"KT1CojtgtVHVarS135fnV3y4z8TiKXrsRHJr:2","source":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"destination":{"address":""},"timestamp":1711546022000,"opId":"t1028717231996928"},{"type":"transaction","block":"BM7UJZJ9zjw78FMHiZU48GKPeBP5GkJJ842LbfvbZYXdBrCWUvL","hash":"onkUjxzTwNqiprYbWa1aQUJHW64XdQFUNoz1PKAk77cFgCXBHK8","status":1,"amount":"100000000000000000","tokenId":"KT1K9gCRgaLRFKTErYt1wVxA3Frb9FjasjTV:0","source":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"destination":{"address":"KT1CojtgtVHVarS135fnV3y4z8TiKXrsRHJr"},"timestamp":1711545977000,"opId":"t1028716530499584"},{"type":"transaction","block":"BM7UJZJ9zjw78FMHiZU48GKPeBP5GkJJ842LbfvbZYXdBrCWUvL","hash":"onkUjxzTwNqiprYbWa1aQUJHW64XdQFUNoz1PKAk77cFgCXBHK8","status":1,"amount":"99169963791590000346479370294958188","tokenId":"KT1CojtgtVHVarS135fnV3y4z8TiKXrsRHJr:2","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711545977000,"opId":"t1028716529451008"},{"type":"transaction","block":"BLuE2jV5GJmJaLtDCEDg1qMCNrZzzo5ZbBdiCsxMaRhzELTpNgp","hash":"opKw8rUn4FS6bnZySPPJogiBjjyxsCeQvZAphCP9omWmYf8X5YA","status":1,"amount":"994135166","tokenId":"KT1GrCKjNNYqonyFxDvANvmW1aofeASSEhb2:0","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711545887000,"opId":"t1028715121213440"},{"type":"transaction","block":"BLuE2jV5GJmJaLtDCEDg1qMCNrZzzo5ZbBdiCsxMaRhzELTpNgp","hash":"opKw8rUn4FS6bnZySPPJogiBjjyxsCeQvZAphCP9omWmYf8X5YA","status":1,"amount":"992259340588400","tokenId":"KT1JVjgXPMMSaa6FkzeJcgb8q9cUaLmwaJUX:0","source":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"destination":{"address":"KT1PUvSeUkBf5adoFY1VxgQArMx8pCdUSq7N"},"timestamp":1711545887000,"opId":"t1028715120164864"},{"type":"transaction","block":"BLuE2jV5GJmJaLtDCEDg1qMCNrZzzo5ZbBdiCsxMaRhzELTpNgp","hash":"opKw8rUn4FS6bnZySPPJogiBjjyxsCeQvZAphCP9omWmYf8X5YA","status":1,"amount":"999","tokenId":"KT1DCiLVoASFp2R4t5Vpg3FVDH6QTdENd6Tw:0","source":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"destination":{"address":"KT1PUvSeUkBf5adoFY1VxgQArMx8pCdUSq7N"},"timestamp":1711545887000,"opId":"t1028715119116288"},{"type":"transaction","block":"BMRw61xstbsgobRYGxShyKeHz4KiffTrnTUNZrYXdU6Wux1AGmn","hash":"ookCBzR4AQGMCid4kpHDMCVUa7BmGtK6Z3XyQYTiK1qtmwePuwG","status":1,"amount":"137826","tokenId":"KT1LN4LPSqTMS7Sd2CJw4bbDGRkMv2t68Fy9:0","source":{"address":"KT1V5XKmeypanMS9pR65REpqmVejWBZURuuT"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711545827000,"opId":"t1028714192175104"},{"type":"transaction","block":"BLawne356LqDLX9bEcApTDUQwX3isKp6sUaPfdKS4Y2Fxzaw2ue","hash":"opHCfvzLKzHcvcqV8u2UoQv9X9RGAVwJWbTDgnrLx8z3bvhbsgs","status":1,"amount":"50000000000","tokenId":"KT1XRPEPXbZK25r3Htzp2o1x7xdMMmfocKNW:0","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711545617000,"opId":"t1028710856654848"},{"type":"transaction","block":"BL97HFsuFQq2me21GXAdxYppqwysuxfU42RLhzQCqgtwzR2AaVa","hash":"op1KTVDCbFbBqs6ivMaLj6RDEcR2tPLbsRbqwKVJuxtd62WQp6H","status":1,"amount":"22477235774","tokenId":"KT1NodvAh8uTny1uU35rLAErzkTG66uxKNiM:0","source":{},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711545527000,"opId":"t1028709453660160"},{"type":"transaction","block":"BL97HFsuFQq2me21GXAdxYppqwysuxfU42RLhzQCqgtwzR2AaVa","hash":"op1KTVDCbFbBqs6ivMaLj6RDEcR2tPLbsRbqwKVJuxtd62WQp6H","status":1,"amount":"13304118766","tokenId":"KT1XRPEPXbZK25r3Htzp2o1x7xdMMmfocKNW:3","source":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"destination":{"address":"KT1SPUvH5khHtirTEVeECiKrnh4FFXxWZ6ui"},"timestamp":1711545527000,"opId":"t1028709452611584"},{"type":"transaction","block":"BL9aU8SC4Q58JzaBJ38izJEXDec96e88R2pxAJ8UrV1xmaE9fEq","hash":"ooiJHWtKBGJLVE2Qa8NQs47JPdLkprDnHdBRXJokooLNQqQdxtZ","status":1,"amount":"99850230009","tokenId":"KT1XRPEPXbZK25r3Htzp2o1x7xdMMmfocKNW:3","source":{"address":"KT1SPUvH5khHtirTEVeECiKrnh4FFXxWZ6ui"},"destination":{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo"},"timestamp":1711545497000,"opId":"t1028708883234816"}],"tokens":[{"tokenId":"KT1XnTn74bUtxHfDtBmm2bGZAQfhPbvKWR8o:0","balance":"7400"},{"tokenId":"KT1XRPEPXbZK25r3Htzp2o1x7xdMMmfocKNW:3","balance":"86546111243"},{"tokenId":"KT1XRPEPXbZK25r3Htzp2o1x7xdMMmfocKNW:0","balance":"50709978197"},{"tokenId":"KT1XNJ67F3JN2cmq6s1LmqtVg7gy9tCcN4E2:7","balance":"1"},{"tokenId":"KT1XNJ67F3JN2cmq6s1LmqtVg7gy9tCcN4E2:21","balance":"1"},{"tokenId":"KT1WGDVRnff4rmGzJUbdCRAJBmYt12BrPzdD:1033","balance":"1"},{"tokenId":"KT1V7QCmuKpGsThwCNRALmsVfDAYopV98EEL:1742935","balance":"1"},{"tokenId":"KT1UmNSC5gjZeTcTeMEGpXqUZaJwUVRqvunM:14403","balance":"1"},{"tokenId":"KT1UQVEDf4twF2eMbrCKQAxN7YYunTAiCTTm:0","balance":"25147605088750"},{"tokenId":"KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi:1611769","balance":"1"},{"tokenId":"KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi:1600310","balance":"1"},{"tokenId":"KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi:1527145","balance":"1"},{"tokenId":"KT1U6EHmNxJTkvaWJ4ThczG4FSDaHC21ssvi:1159376","balance":"1"},{"tokenId":"KT1ShjB24tTXX8m5oDCrBHXqynCpmNJDxpD5:0","balance":"866922371"},{"tokenId":"KT1SQFuskMGQB7arqvQyuCH9v2utbJNmGcR6:5","balance":"1"},{"tokenId":"KT1RJ6PbjHpwc3M5rw5s2Nbmefwbuwbdxton:206733","balance":"1"},{"tokenId":"KT1Qsf5L7M7Vsv5b428VTFYG7YQdRPeUpHT9:1","balance":"1"},{"tokenId":"KT1PnUZCp3u2KzWr93pn4DD7HAJnm3rWVrgn:0","balance":"82"},{"tokenId":"KT1PegvRtG4LTWGjNx8bswVEvqKNC1FBZBjL:0","balance":"200000000000"},{"tokenId":"KT1PNcZQkJXMQ2Mg92HG1kyrcu3auFX5pfd8:3814","balance":"1"},{"tokenId":"KT1PEcahU7mnDYEgpRtnY4SiKQantq8GXvCU:0","balance":"8110563291"},{"tokenId":"KT1NodvAh8uTny1uU35rLAErzkTG66uxKNiM:0","balance":"22477235774"},{"tokenId":"KT1MxZTs3QrWffkGCLrkw8hVXxBiSbqo3JZn:0","balance":"1"},{"tokenId":"KT1MsdyBSAMQwzvDH4jt2mxUKJvBSWZuPoRJ:7895","balance":"1"},{"tokenId":"KT1LN4LPSqTMS7Sd2CJw4bbDGRkMv2t68Fy9:0","balance":"137826"},{"tokenId":"KT1LBXXg9UFvyfdvPmRMc4JZQsYWdyPCS6ue:1","balance":"1"},{"tokenId":"KT1L6BTeGP5NcVmRjys85EaDxBymxMyx5rj8:0","balance":"1"},{"tokenId":"KT1KrTm6Aei9zp6UH7zYi5EUx23PAvcoK5B5:0","balance":"1"},{"tokenId":"KT1K9gCRgaLRFKTErYt1wVxA3Frb9FjasjTV:0","balance":"967037022613933412"},{"tokenId":"KT1JVjgXPMMSaa6FkzeJcgb8q9cUaLmwaJUX:0","balance":"10711769817403067"},{"tokenId":"KT1J13kB3GQBuu9VJv5jsuCXJf6jSQoJjkPf:135","balance":"1"},{"tokenId":"KT1Hc6KSHp3pLLiCXUkuHva62PRJQ8JmyAcP:0","balance":"8888000000"},{"tokenId":"KT1Ha4yFVeyzw6KRAdkzq6TxDHB97KG4pZe8:0","balance":"186"},{"tokenId":"KT1GrCKjNNYqonyFxDvANvmW1aofeASSEhb2:0","balance":"994135166"},{"tokenId":"KT1G5ph5ybHBbAy2hEd5RbPTuGEQuWXMWsBB:1384","balance":"1"},{"tokenId":"KT1FfjhvJZppBFQuUzNAdFPR1Z2jpD4XiXrF:0","balance":"300000000000"},{"tokenId":"KT1FE8LS1D4DFwEjLKLgVKDFNF5w2j3vBDyK:0","balance":"200000000000"},{"tokenId":"KT1FDPuhFJkdmsmcFwA1PPbKErz29ZCTiEWm:0","balance":"20"},{"tokenId":"KT1DCiLVoASFp2R4t5Vpg3FVDH6QTdENd6Tw:0","balance":"29299"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:46","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:43","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:37","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:36","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:33","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:24","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:203","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:166","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:162","balance":"2"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:16","balance":"2"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:158","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:157","balance":"3"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:156","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:155","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:144","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:143","balance":"2"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:136","balance":"3"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:135","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:134","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:13","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:128","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:126","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:124","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:12","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:118","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:117","balance":"2"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:116","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:112","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:104","balance":"2"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:102","balance":"2"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:101","balance":"1"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:1","balance":"4"},{"tokenId":"KT1CzVSa18hndYupV9NcXy3Qj7p8YFDZKVQv:0","balance":"1"},{"tokenId":"KT1CojtgtVHVarS135fnV3y4z8TiKXrsRHJr:2","balance":"89252968448887338619223402796801859"},{"tokenId":"KT1CS2xKGHNPTauSh5Re4qE3N9PCfG5u4dPx:0","balance":"127448"},{"tokenId":"KT1BRADdqGk2eLmMqvyWzqVmPQ1RCBCbW5dY:8","balance":"1"},{"tokenId":"KT1BDdJTSr177iQzbnGDKLzxMpsuCTvv2uDE:16","balance":"1"},{"tokenId":"KT1BDdJTSr177iQzbnGDKLzxMpsuCTvv2uDE:0","balance":"1"},{"tokenId":"KT1Avad9GZXRSoKzUukD5qSen44WxPRGsQCN:0","balance":"51501000000"},{"tokenId":"KT1AafHA1C1vk959wvHWBispY9Y2f3fxBUUo:0","balance":"1"},{"tokenId":"KT1AM3PV1cwmGRw28DVTgsjjsjHvmL6z4rGh:0","balance":"1000"},{"tokenId":"KT1AFq5XorPduoYyWxs5gEyrFK6fVjJVbtCj:24862","balance":"10"},{"tokenId":"KT1AFq5XorPduoYyWxs5gEyrFK6fVjJVbtCj:24856","balance":"1"},{"tokenId":"KT19AGCrw6ub2nKV46mN64k8RmiRMfpgah2B:0","balance":"10"}],"delegate":"tz1S5WxdZR5f9NzsPXhr7L9L1vrEb5spZFur","state":"81f4808e05a02f0184ea2e4cacb97716fe4da7e2d42181b11ed6d08c68802d591b6cf444de09b74fb2097020acccbe856cf0e63ebd2289a40afa0c7b245d1558","pkh":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo","pk":"sppk7ZNYgPT1czYX5GrKUgNxmzRAGhkFcfh7eFDcN7yQYC6SrUkkG9m","address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo","originatedAccounts":[]}],"lookups":[{"address":"tz2SxeQwLfqA2Fr1ink3B8V7uWNrPWJakEYo","data":[{"name":"","lookupType":1}]},{"address":"tz1gnuBF9TbBcgHPV2mUE96tBrW7PxqRmx1h","data":[{"name":"Baking Benjamins Payouts","lookupType":5},{"name":"","lookupType":1}]},{"address":"KT1BJC12dG17CVvPKJ1VYaNnaT5mzfnUTwXv","data":[{"name":"FXHASH Generative Tokens v2","lookupType":5},{"name":"","lookupType":1}]},{"address":"tz1Ltx73buQXZ8y4vSydrss1sakZ83UXF6Cq","data":[{"name":"","lookupType":1}]},{"address":"tz1dPVCJvoNR2V537ix9H8AaFkWt8vMjE4vb","data":[{"name":"","lookupType":1}]},{"address":"KT1CojtgtVHVarS135fnV3y4z8TiKXrsRHJr","data":[{"name":"","lookupType":1}]},{"address":"KT1PUvSeUkBf5adoFY1VxgQArMx8pCdUSq7N","data":[{"name":"","lookupType":1}]},{"address":"KT1V5XKmeypanMS9pR65REpqmVejWBZURuuT","data":[{"name":"","lookupType":1}]},{"address":"KT1SPUvH5khHtirTEVeECiKrnh4FFXxWZ6ui","data":[{"name":"","lookupType":1}]}],"verifier":"google","id":"gounisalex@gmail.com","name":"Alexandros Gounis"}}
//                localStorage.setItem('kukai-wallet', JSON.stringify(data));
//                localStorage.setItem('accepted-terms', '1');
//            })();
//        """
//        webView.evaluateJavascript(jsCode, null)
//        webView.reload()
    }

    //    private fun openSecondWebView(url: String) {
//        val secondWebViewContainer = findViewById<ViewGroup>(R.id.secondWebViewContainer)
//
//        if (secondWebView == null) {
//            secondWebView = WebView(this).apply {
//                settings.javaScriptEnabled = true
//                webViewClient = WebViewClient()
//            }
//
//            secondWebViewContainer.addView(secondWebView)
//        }
//
//        secondWebView?.loadUrl(url)
//        secondWebViewContainer.visibility = ViewGroup.VISIBLE
//    }
    private fun launchCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        handleDeepLink(intent)
        web3Auth.setResultUrl(intent?.data)
    }

    private fun handleDeepLink(intent: Intent?) {
        println("deeplink::entered...")
        if (intent != null && intent.action == Intent.ACTION_VIEW) {

            val data: Uri? = intent.data
            if (data != null) {
                val scheme: String? = data.scheme
                val host: String? = data.host

                val userData = data.getQueryParameter("userData")

                handleUserDataFromDeeplink(userData)

//                val newUrl = replaceUri(data.toString())
//                secondWebView?.loadUrl(newUrl)
//                findViewById<ViewGroup>(R.id.secondWebViewContainer).visibility = ViewGroup.GONE
            }
        }
    }

    private fun handleUserDataFromDeeplink(userData: String?) {
        val script = """
                const data = $userData
                localStorage.setItem('kukai-wallet', JSON.stringify(data));
                localStorage.setItem('accepted-terms', '1');
        """.trimIndent()

        println("deeplink::passing-data::$userData")

        webView.evaluateJavascript(script) { result ->
            println("JavaScript execution result: $result")
            webView.reload()
            HAS_LOADED = true
        }
    }

//    private fun replaceUri(uri: String): String {
//        return uri.replace("kukai://auth/", "$BASE_URL/serviceworker/redirect?tes")
//    }

}

