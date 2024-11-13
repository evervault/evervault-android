package com.evervault.sdk.crypto.exceptions

import org.bouncycastle.crypto.InvalidCipherTextException

class InvalidCipherException(originalException: InvalidCipherTextException) : Exception(originalException.message)