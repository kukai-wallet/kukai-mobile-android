package com.kukai.android

import org.bouncycastle.util.encoders.Hex
import org.bitcoinj.core.Base58
import org.bouncycastle.crypto.digests.Blake2bDigest
import org.bouncycastle.asn1.sec.SECNamedCurves
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.math.ec.FixedPointCombMultiplier
import java.math.BigInteger

object TezosKeyGenerator {
    fun generateTezosKeys(hexKey: String): Pair<String, String> {
        val privKeyBytes = Hex.decode(hexKey)
        val privKey = BigInteger(1, privKeyBytes)

        // Generate Secp256k1 public key
        val curveParams = SECNamedCurves.getByName("secp256k1")
        val ecDomain = ECDomainParameters(curveParams.curve, curveParams.g, curveParams.n, curveParams.h)
        val pubKeyPoint = FixedPointCombMultiplier().multiply(ecDomain.g, privKey).normalize()
        val pubKeyBytes = pubKeyPoint.getEncoded(false)  // uncompressed public key

        // Encode the public key in Tezos format (sppk)
        val sppkPrefix = byteArrayOf(0x03, 0x02, 0xaa.toByte(), 0xaa.toByte())  // prefix for Secp256k1 public keys (sppk)
        val pubKeyHash = blake2bHash(pubKeyBytes, 20)
        val sppk = "sppk" + Base58.encode(pubKeyHash)

        // Generate Tezos address (tz2)
        val addressPrefix = byteArrayOf(0x06, 0xa1.toByte(), 0xa1.toByte())  // prefix for tz2 addresses
        val addressWithPrefix = addressPrefix + pubKeyHash
        val tz2Address = Base58.encode(addChecksum(addressWithPrefix))

        return Pair(sppk, tz2Address)
    }

    private fun blake2bHash(input: ByteArray, size: Int): ByteArray {
        val digest = Blake2bDigest(size * 8)
        digest.update(input, 0, input.size)
        val hash = ByteArray(size)
        digest.doFinal(hash, 0)
        return hash
    }

    private fun addChecksum(input: ByteArray): ByteArray {
        val checksum = blake2bHash(input, 4)
        return input + checksum
    }
}