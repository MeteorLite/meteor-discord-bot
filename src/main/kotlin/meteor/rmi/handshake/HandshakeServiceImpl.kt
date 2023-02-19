package meteor.rmi.handshake

import meteor.auth.AuthManager

object HandshakeServiceImpl : HandshakeService {
    override fun handshake(uuid: String): String {
        return if (AuthManager.hasUser(uuid))
            "Meteor Auth Success!"
        else
            "Meteor Auth Failure!"
    }
}