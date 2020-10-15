package ch.heigvd.iict.sym.labo2

import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {

//    fun sendRequest(url: String, request: String) {
//        val urlConnection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
//        try {
//            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
//            val isw = InputStreamReader(input)
//
//            var data = isw.read()
//            while (data != -1) {
//                val current = data.toChar()
//                data = isw.read()
//                print(current)
//            }
//        } finally {
//            urlConnection.disconnect()
//        }
//    }

    fun sendRequest(url: String, request: String) {
        object : Thread() {
            override fun run() {
                communicationEventListener?.handleServerResponse(URL(url).readText())
            }
        }.start()
    }
}