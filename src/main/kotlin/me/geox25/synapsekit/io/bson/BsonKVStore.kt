package me.geox25.synapsekit.io.bson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import de.undercouch.bson4jackson.BsonFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

open class BsonKVStoreResult

data class ValueResult(val value: String) : BsonKVStoreResult()

data class ErrorResult(val errorCode: Int) : BsonKVStoreResult()

data class BsonKVStore(private val data: MutableMap<String, Any>) : BsonCustomPackable {

    // Instance of ObjectMapper
    private var objectMapper: ObjectMapper

    // Creates an ObjectMapper if none is specified
    init {
        this.objectMapper = ObjectMapper(BsonFactory())
    }

    // Injects specified ObjectMapper
    constructor(data: MutableMap<String, Any>, objectMapper: ObjectMapper) : this(data) {
        this.objectMapper = objectMapper
    }

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
    fun get(key: String): BsonKVStoreResult {
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
    override fun serialize(): ByteArray {
        return objectMapper.writeValueAsBytes(data)
    }

    /*
     * Serialize object to a file
     *
     * @param file The file to serialize the object to
     */
    fun serializeTo(file: File) {
        val stream = FileOutputStream(file)
        stream.write(objectMapper.writeValueAsBytes(data))
        stream.close()
    }

    companion object {

        // Static Instance of ObjectMapper
        private val companionObjectMapper: ObjectMapper = ObjectMapper(BsonFactory())

        /*
         * Deserialize BsonKVStore from ByteArray
         *
         * Returns BsonKVStore
         *
         * @param bytes The bytes to deserialize
         * @param objectMapper Optional
         */
        fun deserialize(bytes: ByteArray, objectMapper: ObjectMapper = companionObjectMapper) : BsonKVStore {
            return BsonKVStore(objectMapper.readValue(bytes, object : TypeReference<MutableMap<String, Any>>() {}))
        }

        /*
         * Deserialize from a file
         *
         * Returns BsonKVStore
         *
         * @param file The file to read from
         * @param objectMapper Optional
         */
        fun deserializeFrom(file: File, objectMapper: ObjectMapper = companionObjectMapper): BsonKVStore {
            val stream = FileInputStream(file)
            val bytes = stream.readBytes()
            val store = deserialize(bytes, objectMapper)
            stream.close()

            return store
        }
    }
}