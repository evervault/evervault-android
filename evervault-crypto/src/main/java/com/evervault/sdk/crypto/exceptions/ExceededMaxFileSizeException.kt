package com.evervault.sdk.crypto.exceptions

class ExceededMaxFileSizeException(maxFileSizeInMB: Int) : Exception("File size exceeds $maxFileSizeInMB MB limit")