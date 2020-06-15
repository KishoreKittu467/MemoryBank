package com.kkapps.memorybank.commons.interfaces

interface CopyMoveListener {
    fun copySucceeded(copyOnly: Boolean, copiedAll: Boolean, destinationPath: String)

    fun copyFailed()
}
