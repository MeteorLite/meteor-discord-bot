package meteor

import dev.kord.common.entity.PresenceStatus
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.edit
import dev.kord.core.entity.channel.VoiceChannel
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.rest.builder.channel.VoiceChannelModifyBuilder
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import java.net.URL
import kotlin.time.Duration.Companion.seconds

object Main {
    private lateinit var kord: Kord
    private val dataDir = File("./data/")
    private val peakSessionsFile = File("./data/peaksessions.txt")
    private var peakSessions = -1
    @JvmStatic
    fun main(args: Array<String>) {

        // init data dir
        if (!dataDir.exists())
            dataDir.mkdir()

        // restore/init peak session count
        peakSessions = if (!peakSessionsFile.exists()) {
            peakSessionsFile.writeText("-1")
            -1
        } else
            peakSessionsFile.readText().toInt()

        // kord must runBlocking
        runBlocking {
            kord = Kord(Secrets.botToken)
            scheduleSessionUpdating()
            kord.login()
        }
    }

    /**
     * Create a daemon thread (GlobalScope) to update session counts every 30sec
     * This expects the session-service to be running on the same machine (127.0.0.1)
     * We simply change the name of the "METEOR STATS" category's channels to provide updates
     */
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun scheduleSessionUpdating() {
        GlobalScope.launch {
            while (isActive) {
                delay(30.seconds)
                try {
                    val activeSessions = URL("http://127.0.0.1:8080/session/count").readText().toInt()
                    if (activeSessions > peakSessions) {
                        peakSessions = activeSessions
                        peakSessionsFile.writeText("$peakSessions")
                        val sessionsPeakChannel = kord.getChannelOf<VoiceChannel>(Snowflake(1074852396053827674))
                        sessionsPeakChannel?.let {
                            it.asChannel().edit { this.name = "sessions-peak: $peakSessions" }
                        }
                    }
                    val sessionsChannel = kord.getChannelOf<VoiceChannel>(Snowflake(1074847580401442846))
                    sessionsChannel?.let {
                        it.asChannel().edit { this.name = "sessions: $activeSessions" }
                    }
                    kord.editPresence {
                        status = PresenceStatus.Online
                        afk = false
                        playing("$activeSessions online")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}