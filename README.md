# Evervault Android SDK

The [Evervault](https://evervault.com/) Android SDK is a library that provides secure data encryption and secure credit card input. It's simple to integrate, easy to use and it supports a wide range of data types.

## Features
- Core encryption capabilities for various data types.
A secure and customizable PaymentCardInput Compose view.
- Built-in data type recognition and appropriate encryption handling.

## Supported Platforms
- Android

## Related Projects

The Evervault Android SDK uses the [Evervault Kotlin Multiplatform SDK](https://github.com/evervault/evervault-multiplatform) to provide the core encryption functionality. 

## Installation

Our Android SDK distributed via [maven](https://search.maven.org/artifact/com.evervault.sdk/lib), and can be installed using your preferred build tool.

### Gradle DSL

```kotlin
implementation("com.evervault.sdk:evervault-core:1.0.0")
implementation("com.evervault.sdk:evervault-inputs:1.0.0")
implementation("com.evervault.sdk:evervault-cages:1.0.0")
```

### Maven

```xml
<dependency>
  <groupId>com.evervault.sdk</groupId>
  <artifactId>evervault-core</artifactId>
  <version>1.0.0</version>
</dependency>
<dependency>
  <groupId>com.evervault.sdk</groupId>
  <artifactId>evervault-inputs</artifactId>
  <version>1.0.0</version>
</dependency>
<dependency>
  <groupId>com.evervault.sdk</groupId>
  <artifactId>evervault-cages</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Usage

### Configuration

Before using the Evervault Android SDK, you need to configure it with your Evervault Team ID and App ID. This step is essential for establishing a connection with the Evervault encryption service.

```kotlin
Evervault.shared.configure("<TEAM_ID>", "<APP_ID>")
```

Make sure to replace `<TEAM_ID>` and `<APP_ID>` with your actual Evervault Team ID and App ID.

### Encrypting Data

Once the SDK is configured, you can use the `encrypt` method to encrypt your sensitive data. The `encrypt` method accepts various data types, including Boolean, Numerics, Strings, Arrays, Lists, Maps and ByteArrays.

Here's an example of encrypting a password:

```kotlin
val encryptedPassword = Evervault.shared.encrypt("Super Secret Password")
```

The `encrypt` method returns an `Any` type, so you will need to safely cast the result based on the data type you provided. For Boolean, Numerics, and Strings, the encrypted data is returned as a String. For Arrays, Lists and Maps, the encrypted data maintains the same structure but is encrypted (except that Arrays become Lists). For ByteArray, the encrypted data is returned as encrypted ByteArray, which can be useful for encrypting files.

### Inputs

The Evervault Android SDK also provides a Compose view called `PaymentCardInput`. This view is designed for capturing credit card information and automatically encrypts the credit card number and CVC without exposing the unencrypted data. The `PaymentCardInput` view can be customized to fit your application's design.

Here's an example of using the `PaymentCardInput` view:

```kotlin
PaymentCardInput(onDataChange = { paymentCardData: PaymentCardData ->
    // Handle card data
})
```

The encrypted credit card number and CVC are captured in the `PaymentCardData`, as well as the expiry month and year and validation fields.

#### Styling

The `PaymentCardInput` can be customized to fit your application's design. The view accepts a `layout` parameter, which can be used to customize the view's layout. The `layout` parameter accepts a `@Composable` function, which can be used to customize the view's layout. 

Two build-in layouts are provided:

- `inlinePaymentCardInputLayout()` (the default layout) - This layout displays the card number, expiry and CVC fields inline.

<img src="https://github.com/evervault/evervault-android/blob/main/inline.png?raw=true" alt="inlinePaymentCardInputLayout" align="center"/>

To explicitly use this layout:

```kotlin
PaymentCardInput(
    layout = inlinePaymentCardInputLayout(),
    onDataChange = { paymentCardData: PaymentCardData ->
        // Handle card data
    }
)
```

- `rowsPaymentCardInputLayout()` - puts the credit card number on a single row. Below it, places the expiry and cvc fields next to each other.

<img src="https://github.com/evervault/evervault-android/blob/main/rows.png?raw=true" alt="rowsPaymentCardInputLayout" align="center"/>

To use this layout:
    
```kotlin
PaymentCardInput(
    layout = rowsPaymentCardInputLayout(),
    onDataChange = { paymentCardData: PaymentCardData ->
        // Handle card data
    }
)
```

You can also customize these layout through theming and modifiers:

```kotlin
MaterialTheme(
    colorScheme = MaterialTheme.colorScheme.copy(
        primary = Color.Black,
        secondary = Color.White,
        primaryContainer = Color.LightGray,
    ),
) {
    PaymentCardInput(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp)),
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        onDataChange = it
    )
}
```

If these two layouts do not fit your application's design, you can create your own layout by passing a `@Composable` function to the `layout` parameter. The `@Composable` function will receive a `PaymentCardInputScope` object, which contains the `CardImage`, `CardNumberField`, `ExpiryField` and `CVCField` fields. You can use these fields to create your own layout:

```kotlin
PaymentCardInput(
    layout = {
        Column {
            CardImage()
            Row {
                CardNumberField()
                Column {
                    ExpiryField()
                    CVCField()
                }
            }
        }
    },
    onDataChange = { paymentCardData: PaymentCardData ->
        // Handle card data
    }
)
```

### Cages (Beta)

The Evervault Cages SDK provides an accessible solution for developers to deploy Docker containers within a Secure Enclave. It enables you to interact with your Cages endpoints via standard HTTP requests directly from your Android applications. A key feature that enables this is the attestation verification performed before completing the TLS handshake. For a deeper understanding of this process, you can explore our [TLS Attestation documentation](https://docs.evervault.com/products/cages#tls-attestation).

The attestation verification is executed using a custom Trust Manager on a `OkHttpClient.Builder`. This Trust Manager needs initialization with one or more `AttestationData` objects:

```kotlin
data class AttestationData(
    val cageName: String,
    val pcrs: List<PCRs>
) {
    constructor(cageName: String, vararg pcrs: PCRs)
}

data class PCRs(
    val pcr0: String,
    val pcr1: String,
    val pcr2: String,
    val pcr8: String
)
```

It is crucial to ensure that the supplied PCRs align with the PCRs of the respective Cage.

Then you configure the HTTP Client with the attestations:

```kotlin

val client = OkHttpClient.Builder()
    .trustManager(AttestationData(
        cageName = cageName,
        // Replace with legitimate PCR strings when not in debug mode
        PCRs(
            pcr0 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            pcr1 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            pcr2 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            pcr8 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
        )
    ))
    .build()
```

Only use this client for requests to your Cage. For all other requests, use a separate client.

#### Considerations

- It's crucial to note that the PCR values associated with a Cage change with each new deployment. Consequently, older versions of your app with hardcoded PCRs may stop functioning. To alleviate this, you can accommodate previous deployments by providing multiple `PCRs` objects:

```kotlin
AttestationData(
    cageName = cageName,
    listOf(
        PCRs(
            pcr0 = "fd4b",
            pcr1 = "bc3f",
            pcr2 = "2c10",
            pcr8 = "dfb3"
        ),
        PCRs(
            pcr0 = "c779",
            pcr1 = "bc3f",
            pcr2 = "4cbf",
            pcr8 = "dfb3"
        )
    )
)
```

- Please be aware that the Android SDK is compatible only with Cages that have the API Key Authentication set to `false` in your cage.toml file:
```
api_key_auth = false
```

- Only use the `OkHttpClient` configured with the `AttestationData` for requests to your Cage. For all other requests, use a separate client. If you have multiple Cages, you will need to configure a separate `OkHttpClient` for each Cage.

- When calling an endpoint of your Cage, use make sure you do not have any underscore `_` in the request url. Replace any underscore, such as the one in your App ID, with hyphens `-`.

These considerations are essential to remember for a seamless integration and operation of the Evervault Cages SDK in your Android applications.

## Sample App

The Evervault Android SDK Package includes a sample app, located in the `sampleapplication` directory. The sample app demonstrates various use cases of the SDK, including string encryption, file (image) encryption, and the usage of the `PaymentCardInput` view with customized styling.

## License

The sample app is released under the MIT License. See the [LICENSE](https://github.com/evervault/evervault-multiplatform/blob/main/LICENSE) file for more information.

Feel free to experiment with the sample app to understand the capabilities of the Evervault iOS SDK and explore different integration options for your own projects.

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/evervault/evervault-multiplatform.

## Feedback

Questions or feedback? [Let us know](mailto:support@evervault.com).
