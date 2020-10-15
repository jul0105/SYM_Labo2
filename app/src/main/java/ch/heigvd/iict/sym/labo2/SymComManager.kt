package ch.heigvd.iict.sym.lab.comm

import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {

    fun sendRequest(url: String, request: String) {
        val urlConnection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
        try {
            val input: InputStream = BufferedInputStream(urlConnection.getInputStream())
            val isw = InputStreamReader(input)

            var data = isw.read()
            while (data != -1) {
                val current = data.toChar()
                data = isw.read()
                print(current)
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}