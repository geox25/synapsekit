package io.mpack

import com.fasterxml.jackson.core.type.TypeReference
import org.msgpack.jackson.dataformat.MessagePackMapper

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

data class KeyValueStore(private val data: MutableMap<String, Any>) : CustomPackable {

    // Instance of ObjectMapper
    private var objectMapper: ObjectMapper

    // Creates an ObjectMapper if none is specified
    init {
        this.objectMapper = MessagePackMapper()
    }

    // Injects specified ObjectMapper
    constructor(data: MutableMap<String, Any>, objectMapper: ObjectMapper) : this(data) {
        this.objectMapper = objectMapper
    }
    constructor() : this(mutableMapOf()) {}

    /*
     * Maps the given key to the given value
     *
     * @param key The key
     * @param value The value
     */
    fun put(key: String, value: Any) {
        data[key] = value
    }

    /*
     * Returns the value mapped to key or -1 if null
     *
     * @param key The key
     */
    fun get(key: String): Any? {
        return if (data[key] == null) {
            -1
        } else {
            data[key]
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
     * Serialize KeyValueStore object to a ByteArray
     *
     * Returns ByteArray
     */
    override fun serialize() : ByteArray {
        return objectMapper.writeValueAsBytes(data)
    }

    /*
     * Serialize KeyValueStore object to a file
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
        private val companionObjectMapper: ObjectMapper = MessagePackMapper()

        /*
         * Deserialize KeyValueStore from ByteArray
         *
         * Returns KeyValueStore
         *
         * @param bytes The bytes to deserialize
         * @param objectMapper Optional
         */
        fun deserialize(bytes: ByteArray, objectMapper: ObjectMapper = companionObjectMapper) : KeyValueStore {
            return KeyValueStore(objectMapper.readValue(bytes, object: TypeReference<MutableMap<String, Any>>() {}))
        }

        /*
         * Deserialize from a file
         *
         * Returns KeyValueStore
         *
         * @param file The file to read from
         * @param objectMapper Optional
         */
        fun deserializeFrom(file: File, objectMapper: ObjectMapper = companionObjectMapper) : KeyValueStore {
            val stream = FileInputStream(file)
            val bytes: ByteArray = stream.readBytes()
            val kvStore = deserialize(bytes, objectMapper)
            stream.close()

            return kvStore
        }
    }
}
