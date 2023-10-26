package com.evervault.sdk.common.exceptions

class ExceededMaxFileSizeException(maxFileSizeInMB: Int) : Exception("File size exceeds $maxFileSizeInMB MB limit")