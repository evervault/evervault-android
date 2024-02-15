# com.evervault.sdk

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
