package me.geox25.synapsekit.io.json

sealed interface JsonCustomPackable {
    fun serialize() : String
}