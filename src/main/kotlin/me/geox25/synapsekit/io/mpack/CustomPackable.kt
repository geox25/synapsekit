package me.geox25.synapsekit.io.mpack

sealed interface CustomPackable {
    fun serialize() : ByteArray
}