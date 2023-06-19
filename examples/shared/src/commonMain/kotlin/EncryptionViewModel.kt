import com.evervault.sdk.CustomConfig
import com.evervault.sdk.Evervault

class EncryptionViewModel {

    init {
        Evervault.shared.configure("teamId", "appId", CustomConfig(isDebugMode = true))
    }

    suspend fun encryptedValue(): String {
        return Evervault.shared.encrypt("Foo") as String
    }
}
