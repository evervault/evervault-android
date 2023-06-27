import CryptoKit
import Foundation

@objc public class ObjcCryptoSharedSecretDeriver: NSObject {

    @objc public func deriveSharedSecret(ecdhP256KeyUncompressed: String, publicKeyEncoder: PublicKeyEncoder) -> ObjcGeneratedSharedKey {
        let privateKey = P256.KeyAgreement.PrivateKey()
        let derivedAesKey = try! deriveSharedSecret(ecdh: privateKey, publicKey: ecdhP256KeyUncompressed, ephemeralPublicKey: privateKey.publicKey, publicKeyEncoder: publicKeyEncoder)

        let publicKey = privateKey.publicKey
        let exportableEcdhPublicKey = publicKey.x963Representation
        let compressedKey = ecPointCompress(ecdhRawPublicKey: exportableEcdhPublicKey)

        return ObjcGeneratedSharedKey(
            generatedEcdhKey: compressedKey,
            sharedKey: derivedAesKey
        )
    }

    private func deriveSharedSecret(ecdh: P256.KeyAgreement.PrivateKey, publicKey: String, ephemeralPublicKey: P256.KeyAgreement.PublicKey, publicKeyEncoder: PublicKeyEncoder) throws -> Data {
        // Convert the public key from base64 string to Data
        let publicKeyData = Data(base64Encoded: publicKey)!

        // Import the public key as a CryptoKey object
        let importedPublicKey = try P256.KeyAgreement.PublicKey(x963Representation: publicKeyData)

        // Perform the key agreement to derive the shared secret
        let sharedSecret = try ecdh.sharedSecretFromKeyAgreement(with: importedPublicKey)

        // Export the ephemeral public key and the secret key as Data
        let exportableEphemeralPublicKey = publicKeyEncoder.encodePublicKey(decompressedKeyData: ephemeralPublicKey.x963Representation)


        let exportableSecret = sharedSecret.withUnsafeBytes { secretPtr in
            Data(secretPtr)
        }

        // Concatenate the secret key and the ephemeral public key
        var concatSecret = Data()
        concatSecret.append(exportableSecret)
        concatSecret.append(Data([0x00, 0x00, 0x00, 0x01]))
        concatSecret.append(exportableEphemeralPublicKey)

        // Hash the concatenated secret using SHA-256
        let hashDigest = SHA256.hash(data: concatSecret)

        return Data(hashDigest)
    }

}

private func ecPointCompress(ecdhRawPublicKey: Data) -> Data {
    let u8full = Array(ecdhRawPublicKey)
    let len = u8full.count
    var u8 = Array(u8full.prefix((1 + len) >> 1)) // drop `y`
    u8[0] = 0x2 | (u8full[len - 1] & 0x01) // encode sign of `y` in first bit
    return Data(u8)
}
