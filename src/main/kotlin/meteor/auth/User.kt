package meteor.auth

class User {
    var discordId: ULong = 0u
    var uuid: String = ""
    var tier: Int = 0
    var banned = false
    var plugins = arrayListOf<Plugin>()

    fun hasPlugin(plugin: Plugin) : Boolean {
        return plugins.any { it == plugin }
    }

    fun addPlugin(plugin: Plugin) {
        plugins.add(plugin)
        AuthManager.saveData()
    }

    fun removePlugin(plugin: Plugin) {
        plugins.remove(plugin)
        AuthManager.saveData()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is User)
            return false
        return discordId == other.discordId
    }
}