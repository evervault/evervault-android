package com.evervault.sdk.core.exceptions

import org.bouncycastle.crypto.InvalidCipherTextException

class InvalidCipherException(originalException: InvalidCipherTextException) : Exception(originalException.message)
