package me.geox25.synapsekit.io.bson

sealed interface BsonCustomPackable {
    fun serialize() : ByteArray
}