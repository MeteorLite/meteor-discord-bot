package meteor.rmi.handshake

import java.rmi.Remote
import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject

class HandshakeServer<T: Remote>(private var name: String = "handshake", private var port: Int = 4000) {
    fun publish(obj: T) {
        LocateRegistry.createRegistry(port).bind(name, UnicastRemoteObject.exportObject(obj, 0))
    }
}