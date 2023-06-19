import CryptoKit
import Foundation

@objc public protocol PublicKeyEncoder {
    @objc func encodePublicKey(decompressedKeyData: Data) -> Data
}
