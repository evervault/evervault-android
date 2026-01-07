package com.evervault.sdk.core.exceptions

class ExceededMaxFileSizeException(maxFileSizeInMB: Int) : Exception("File size exceeds $maxFileSizeInMB MB limit")
