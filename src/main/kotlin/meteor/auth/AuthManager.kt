package meteor.auth

import com.google.gson.reflect.TypeToken
import meteor.Main
import java.io.File
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

object AuthManager {
    var users = arrayListOf<User>()
    var usersDatabase = File("./data/users.json")

    fun loadUsers() {
        if (usersDatabase.exists()) {
            val type: Type = object : TypeToken<ArrayList<User>>() {}.type
            users = Main.gson.fromJson(usersDatabase.readText(), type)
        }
    }

    fun createUser(id: ULong): User {
        val newUser = User()
        newUser.discordId = id
        newUser.uuid = getUnusedUUID()
        users.add(newUser)
        saveData()
        return newUser
    }

    fun getUser(discordId: ULong): User? {
        for (user in users)
            if (user.discordId == discordId)
                return user
        return null
    }

    fun hasUser(discordId: ULong): Boolean {
        for (user in users)
            if (user.discordId == discordId)
                return true
        return false
    }

    fun hasUser(uuid: String): Boolean {
        for (user in users)
            if (user.uuid == uuid)
                return true
        return false
    }

    fun banUser(user: User) {
        user.plugins = arrayListOf()
        user.banned = true
        saveData()
    }

    fun getUnusedUUID() : String {
        while (true) {
            val uuid = UUID.randomUUID().toString()
            if (!users.any { it.uuid == uuid})
                return uuid
        }
    }

    fun saveData() {
        usersDatabase.writeText(Main.gson.toJson(users))
    }
}