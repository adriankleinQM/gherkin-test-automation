package de.qualityminds.gta.driver.db2

import de.qualityminds.gta.driver.db2.config.ConnectionSettings
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.IOException
import java.security.Security
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DB2DriverKT {
    private val jdbcClassName = "com.ibm.db2.jcc.DB2Driver"

    companion object {
        val connections: MutableMap<String, Connection> = HashMap()
    }

    @Throws(SQLException::class, IOException::class, ClassNotFoundException::class)
    private fun initConnection(settings: ConnectionSettings) {
        val db2Url = String.format("jdbc:db2:%s", settings.db2Database)
        val db2User = settings.db2User
        val db2Password = settings.db2Password
        val db2Schema = settings.db2Schema
        Security.insertProviderAt(BouncyCastleProvider(), 1)
        Class.forName(jdbcClassName)
        val connection = DriverManager.getConnection(db2Url, db2User, db2Password)
        connection?.schema = db2Schema
        connections.put(getConnectionIdentifier(settings), connection);
    }

    @Throws(SQLException::class)
    fun getConnection(settings: ConnectionSettings): Connection? {
        return try {
            val connection = connections.get(getConnectionIdentifier(settings))
            if (connection == null || connection.isClosed) {
                initConnection(settings)
            }
            connection
        } catch (e: ClassNotFoundException) {
            throw SQLException(e)
        } catch (e: IOException) {
            throw SQLException(e)
        }
    }

    private fun getConnectionIdentifier(settings: ConnectionSettings) : String{
        return (settings.db2Database + settings.db2Schema + settings.db2User)
    }

}
