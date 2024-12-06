---
"evervault-android": patch
---

A deadlock can occur when `AttestationTrustManagerGA` is initialized and a request to get the 
attestation doc from the enclave are made simultaneously. This is dues to the modification of the
`SSLContext` when a request is made which would lead to a read timeout in OkHTTP Client.

- [3a93fedf814c37ed698fa46432e758ac4f3bd885](https://github.com/evervault/evervault-android/commit/3a93fedf814c37ed698fa46432e758ac4f3bd885): 
`enclavesTrustManager` will block to initialize the cache before modifying the
`AttestationTrustManagerGA` and subsequently the `SSLContext`
- The polling logic has been changed to delay before populating the cache to prevent duplicate requests
- `get()` calls to the cache are now always synchronous as it is not called from a coroutine. 

  
