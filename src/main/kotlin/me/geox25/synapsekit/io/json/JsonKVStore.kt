package me.geox25.synapsekit.io.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

open class JsonKVStoreResult

data class ValueResult(val value: String) : JsonKVStoreResult()

data class ErrorResult(val errorCode: Int) : JsonKVStoreResult()


@Serializable
data class JsonKVStore(private val data: MutableMap<String, String>) : JsonCustomPackable {

    // Empty Constructor Support
    constructor() : this(mutableMapOf()) {}

    /*
     * Maps the given key to the given value
     *
     * @param key The key
     * @param value The value
     */
    fun put(key: String, value: Any) {
        data[key] = value.toString()
    }

    /*
     * Returns the value mapped to key or -1 if null
     *
     * @param key The key
     */
    fun get(key: String): JsonKVStoreResult {
        return if (data[key] == null) {
            ErrorResult(-1)
        } else {
            ValueResult(data[key].toString())
        }
    }

    /*
     * Removes the specified key
     *
     * @param key The key
     */
    fun remove(key: String) {
        data.remove(key)
    }

    /*
     * Serialize object to a string
     *
     * Returns String
     */
    override fun serialize() : String {
        return Json.encodeToString(this)
    }

    /*
     * Serialize object to a file
     *
     * @param file The file to serialize the object to
     */
    fun serializeTo(file: File) {
        val stream = FileOutputStream(file)
        stream.write(Json.encodeToString(this).toByteArray())
        stream.close()
    }

    companion object {

        /*
         * Deserialize from a file
         *
         * Returns JsonKVStore
         *
         * @param file The file to read from
         */
        fun deserializeFrom(file: File) : JsonKVStore {
            val stream = FileInputStream(file)
            val store = Json.decodeFromString<JsonKVStore>(stream.readBytes().toString())
            stream.close()
            return store
        }
    }
}