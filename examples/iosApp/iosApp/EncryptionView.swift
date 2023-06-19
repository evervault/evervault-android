import SwiftUI

import shared

struct EncryptionView: View {

    @State private var encryptedValue: String?

    var body: some View {
        VStack {
            Text("Encrypted string:")
            if let encryptedValue = encryptedValue {
                Text(encryptedValue)
            } else {
                Text("Loading...")
            }
        }
        .padding()
        .task {
            do {
                encryptedValue = try await EncryptionViewModel().encryptedValue()
            } catch {
                encryptedValue = error.localizedDescription
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        EncryptionView()
    }
}
