import io.mpack.KeyValueStore
import java.io.File

fun main(args: Array<String>) {
    // Example Usage
    val kvStore = KeyValueStore()
    kvStore.put("foo", "bar")

    val file: File = File("kvstore.bin")
    kvStore.serializeTo(file)
    println("Written to file!")

    val readKvStore = KeyValueStore.deserializeFrom(file)
    println(readKvStore.get("foo")) // bar
}