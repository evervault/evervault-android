import CryptoKit
import Foundation

@objc public class SealedBoxWrapper: NSObject {

    @objc public let ciphertext: Data
    @objc public let tag: Data

    @objc public init(data: Data, ecdhTeamKey: Data, derivedSecret: Data, nonce: Data) throws {

        let symmetricKey = SymmetricKey(data: derivedSecret)

        let sealedBox = try AES.GCM.seal(
            data,
            using: symmetricKey,
            nonce: AES.GCM.Nonce(data: nonce),
            authenticating: ecdhTeamKey
        )

        self.ciphertext = sealedBox.ciphertext
        self.tag = sealedBox.tag
    }
}
