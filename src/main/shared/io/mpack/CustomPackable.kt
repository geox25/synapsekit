package io.mpack

sealed interface CustomPackable {
    fun serialize() : ByteArray
}