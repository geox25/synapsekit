package player.apts

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

class TrackingSystem {

    // Players that are actively being tracked
    private val activePlayers: MutableMap<String, Location> = mutableMapOf()

    // Whether the tracking system is active
    private val isEnabled: Boolean = false

    fun updateAllLocations() {
        if (isEnabled) {
            for ((key, value) in activePlayers) {
                // If player is online
                val player: Player? = Bukkit.getPlayerExact(key)
                if (isActivePlayer(key) && player != null) {
                    activePlayers[key] = player.location
                }
            }
        }
    }

    fun updateSpecificPlayerLocation(username: String) {
        if (isActivePlayer(username)) {
            val player: Player? = Bukkit.getPlayerExact(username)
            if (player != null) {
                activePlayers[username] = player.location
            }
        }
    }

    fun requestLocation(username: String) : Location? {
        if (isActivePlayer(username)) {
            return activePlayers[username]
        } else {
            return null
        }
    }

    private fun isActivePlayer(username: String) : Boolean {
        return (isEnabled && Bukkit.getPlayerExact(username) != null && activePlayers.containsKey(username))
    }
}