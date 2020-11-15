/**
 * SYM : Labo 2 - Protocoles applicatifs
 * Auteurs : Julien Béguin, Robin Cuénoud & Gaëtan Daubresse
 * Date : 15.11.2020
 * Classe : B
 */

package ch.heigvd.iict.sym.labo2

import java.io.BufferedReader
import java.io.DataInputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream
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
                if (compressed) {
                    connection.setRequestProperty("X-Network", "CSD")
                    connection.setRequestProperty("X-Content-Encoding", "deflate");
                }


                // send request
                try {
                    val outputStream: OutputStream

                    if (compressed) {
                        outputStream = DeflaterOutputStream(connection.outputStream, Deflater(9, true))
                    }  else {
                        outputStream = connection.outputStream
                    }

                    outputStream.write(postData)
                    outputStream.close()
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    return
                }

                println(connection.responseCode)

                // receive response
                try {
                    val inputStream: InputStreamReader
                    if (connection.headerFields["X-Content-Encoding"]?.get(0).equals("deflate")) {
                        inputStream = InputStreamReader(InflaterInputStream(connection.inputStream, Inflater(true)))
                    } else {
                        inputStream = InputStreamReader(connection.inputStream)
                    }

                    val reader: BufferedReader = BufferedReader(inputStream)
                    val output: String = reader.readText()

                    communicationEventListener?.handleServerResponse(output);

                } catch (exception: Exception) {
                    exception.printStackTrace()
                    return
                }


            }
        }.start()
}


