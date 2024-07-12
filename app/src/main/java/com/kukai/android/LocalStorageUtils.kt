package com.kukai.android

import com.web3auth.core.types.UserInfo

object LocalStorageUtils {
    fun getLocalStorage(privKey: String, userInfo: UserInfo): String {
        val (pk, address) = TezosKeyGenerator.generateTezosKeys(privKey)
        val (name, email) = userInfo

        val payload = """
        {
            "type": "TorusWallet",
            "localStorageId": 1,
            "data": {
                "totalBalanceXTZ": null,
                "totalBalanceUSD": null,
                "XTZrate": 0,
                "implicitAccounts": [
                    {
                        "balanceXTZ": null,
                        "balanceUSD": null,
                        "stakedBalance": null,
                        "unstakedBalance": null,
                        "availableBalance": null,
                        "activities": [],
                        "delegate": "",
                        "pkh": "$address",
                        "pk": "$pk",
                        "address": "$address",
                        "originatedAccounts": []
                    }
                ],
                "lookups": [],
                "verifier": "google",
                "id": "$email",
                "name": "$name"
            }
        }
        """.trimIndent()
        return payload
    }
}