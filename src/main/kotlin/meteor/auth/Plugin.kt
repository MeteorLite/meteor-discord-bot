package meteor.auth

class Plugin {
    var name = ""

    override fun equals(other: Any?): Boolean {
        if (other !is Plugin)
            return false
        return name == other.name
    }
}