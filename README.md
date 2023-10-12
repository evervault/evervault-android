[![Evervault](https://evervault.com/evervault.svg)](https://evervault.com/)
# Evervault Android SDK
[![CI](https://github.com/evervault/evervault-android/actions/workflows/codeql.yml/badge.svg)](https://github.com/evervault/evervault-android/actions/workflows/codeql.yml)

The [Evervault](https://evervault.com/) Android SDK is a library that provides secure data encryption and secure credit card input. It's simple to integrate, easy to use and it supports a wide range of data types.

## Installation
 * [Gradle](https://docs.evervault.com/sdks/android#gradle-dsl)
 * [Maven](https://docs.evervault.com/sdks/android#maven)

## Features
* [Encrypt](https://docs.evervault.com/security/evervault-encryption) various data types for storing sensitive data.
* Collect credit card information for PCI-DSS compliance with [Inputs](https://docs.evervault.com/products/inputs).
* Connect and Remotely Attest [Cages](https://docs.evervault.com/products/cages)

## SDK Reference
* Browse the upto date documentation [here](https://docs.evervault.com/sdks/android)
## Examples
This project includes a [sample application](https://github.com/evervault/evervault-android/tree/main/sampleapplication) that integrates [Evervault Inputs](https://docs.evervault.com/products/inputs) and [Evervault Cages](https://docs.evervault.com/products/cages)

## Contributing
Bug reports and pull requests are welcome on GitHub.

## Cages

Cage attestation is written in Rust and Kotlin bindings can be found [here](https://github.com/evervault/attestation-doc-validation/tree/main/kotlin-attestation-bindings) that are used to intgrate them into this project.

If you wish to update the Rust bindings, make any necessary changes in the Atttesation crate and run the following [script](https://github.com/evervault/attestation-doc-validation/blob/main/kotlin-attestation-bindings/build-libs.sh) to build and copy the shared library files for each architecture into this repo

## License
The sample app is released under the MIT License. See the [LICENSE](https://github.com/evervault/evervault-android/tree/main/LICENSE) file for more information.
