package ch.heigvd.iict.sym.labo2

import android.os.Looper
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.logging.Handler
import kotlin.text.Charsets.UTF_8

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {

    fun sendRequest(url: String, request: String, content_type: String = "text/plain", compressed: Boolean = false) =
        object : Thread() {


            override fun run() {

                val serverURL: String = url;
                val connection = URL(serverURL).openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val postData: ByteArray = request.toByteArray(UTF_8)

                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Content-lenght", postData.size.toString())
                connection.setRequestProperty("Content-Type", content_type)


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
                    val output: String = reader.readText()

                    communicationEventListener?.handleServerResponse(output);

                } catch (exception: Exception) {
                    exception.printStackTrace()
                }


            }
        }.start()
}


