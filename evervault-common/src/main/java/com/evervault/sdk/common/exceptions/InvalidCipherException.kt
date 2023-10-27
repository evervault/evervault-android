package com.evervault.sdk.common.exceptions

import org.bouncycastle.crypto.InvalidCipherTextException

class InvalidCipherException(originalException: InvalidCipherTextException) : Exception(originalException.message)