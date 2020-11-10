package ch.heigvd.iict.sym.labo2

import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import kotlin.text.Charsets.UTF_8

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

                val serverURL: String = url;
                val connection = URL(serverURL).openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val postData: ByteArray = request.toByteArray(UTF_8)

                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Content-lenght", postData.size.toString())
                connection.setRequestProperty("Content-Type", "text/plain")


                // send request
                try {
                    val outputStream: DataOutputStream = DataOutputStream(connection.outputStream)
                    outputStream.write(postData)
                    outputStream.flush()
                } catch (exception: Exception) {

                }

                // receive response
                try {
                    val inputStream: DataInputStream = DataInputStream(connection.inputStream)
                    val reader: BufferedReader = BufferedReader(InputStreamReader(inputStream))
                    val output: String = reader.readLine()

                    communicationEventListener?.handleServerResponse(output);

                } catch (exception: Exception) {

                }


            }
        }.start()
    }
}


