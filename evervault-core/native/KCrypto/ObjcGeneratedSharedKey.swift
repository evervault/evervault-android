import CryptoKit
import Foundation

@objc public class ObjcGeneratedSharedKey: NSObject {
    @objc public let generatedEcdhKey: Data
    @objc public let sharedKey: Data

    @objc public init(generatedEcdhKey: Data, sharedKey: Data) {
        self.generatedEcdhKey = generatedEcdhKey
        self.sharedKey = sharedKey
    }
}