# com.evervault.sdk

## 2.3.2

### Patch Changes

- 7e20108: Update junit dependency to 4.13.2

## 2.3.1

### Patch Changes

- 27c568f: Update dependenciesa to fix security vulnerabilities and restore kotlin serialization plugin

## 2.3.0

### Minor Changes

- 58586d8: Upgrade bouncycastle dependency

## 2.2.1

### Patch Changes

- 4f358bb: consolidate libraries into evervault-android

## 2.2.0

### Minor Changes

- 90232f8: release new evervault-pay features

## 2.1.1

### Patch Changes

- 527f161: Fix workflow

## 2.1.0

### Minor Changes

- 6c91091: Add support for 16KB pages and update evervault-pay dependencies
- 723375c: \* Update evervault-pay dependency
  - Automatically set Google Pay sandbox config based on Evervault App
  - Update proguard rules for `evervault-pay` symbols
  - Expose underlying Google Pay API for more customization

## 2.0.0

### Major Changes

- 3540747: Add Evervault Google Pay to Inputs module

## 1.13.1

### Patch Changes

- 9575d9c: A deadlock can occur when `AttestationTrustManagerGA` is initialized and a request to get the
  attestation doc from the enclave are made simultaneously. This is dues to the modification of the
  `SSLContext` when a request is made which would lead to a read timeout in OkHTTP Client.

  - [3a93fedf814c37ed698fa46432e758ac4f3bd885](https://github.com/evervault/evervault-android/commit/3a93fedf814c37ed698fa46432e758ac4f3bd885):
    `enclavesTrustManager` will block to initialize the cache before modifying the
    `AttestationTrustManagerGA` and subsequently the `SSLContext`
  - The polling logic has been changed to delay before populating the cache to prevent duplicate requests
  - `get()` calls to the cache are now always synchronous as it is not called from a coroutine.

## 1.13.0

### Minor Changes

- cb51dea: fix: A bug exists with slower network connectivity that can cause an encryption to fail. This will result
  in a card being marked as valid as the card number is valid. Explit empty checks are not performed before
  the card is returned from the card component.
  feat: implement new CardNumberField for control over onNext capability. When using a CardNumberField without
  CVC or expiry expose the onNext function to control the focus behaviour of the compose component.

## 1.12.3

### Patch Changes

- afd82f7: Fix 1Password autofil

## 1.12.2

### Patch Changes

- 0311fbb: Removing attestation doc request on the main thread during initialisation

## 1.12.1

### Patch Changes

- 0c1a815: Remove duplicate dependency

## 1.12.0

### Minor Changes

- c5bfdba: Allow validation of just individual fields

## 1.11.0

### Minor Changes

- 01bbd03: Update evervault-core dependencies

## 1.10.1

### Patch Changes

- db2198f: Add signging to evervault-enclaves

## 1.10.0

### Minor Changes

- 6b9b373: This release allows for the setting of the cursor color for all input fields

## 1.9.0

### Minor Changes

- 89c0cb5: Deprecate evervault-cages move to evervault-enclaves

## 1.8.0

### Minor Changes

- 2a2aad8: PCRs for attestation data are optional. Instead of passing all PCRs to attest a Cage connection you can now pass N PCRs where N is 1..4 valid PCRs

## 1.7.0

### Minor Changes

- 28ceca6: Implement a PCR cache manager for working with attestable cages. This feature allows for the updating of a list of PCRs from an external callback url

## 1.6.2

### Patch Changes

- dda310b: revert to using external evervault-core library

## 1.6.1

### Patch Changes

- 49b792a: Adding version to evervault-common to fix version dependency issue.

## 1.6.0

### Minor Changes

- d398fc1: \* evervault-core is now part of evervault-android
  - evervault-inputs is now more customizable then before, new Composable components for customizing inputs and new data classes
    allow for more control over the UI components.

## 1.5.0
