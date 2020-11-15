/**
 * SYM : Labo 2 - Protocoles applicatifs
 * Auteurs : Julien Béguin, Robin Cuénoud & Gaëtan Daubresse
 * Date : 15.11.2020
 * Classe : B
 */

package ch.heigvd.iict.sym.labo2

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream
import kotlin.text.Charsets.UTF_8

class SymComManager(var communicationEventListener: CommunicationEventListener? = null) {

    fun sendRequest(
        url: String,
        request: String,
        content_type: String = "text/plain",
        compressed: Boolean = false
    ) =
        object : Thread() {

            override fun run() {

                // init connection
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val postData: ByteArray = request.toByteArray(UTF_8)

                // set HTTP headers
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Content-lenght", postData.size.toString())
                connection.setRequestProperty("Content-Type", content_type)
                if (compressed) {
                    connection.setRequestProperty("X-Network", "CSD")
                    connection.setRequestProperty("X-Content-Encoding", "deflate")
                }

                // send request
                try {
                    val outputStream: OutputStream

                    if (compressed) {
                        // get compression steam
                        outputStream = DeflaterOutputStream(connection.outputStream, Deflater(9, true))
                    } else {
                        outputStream = connection.outputStream
                    }

                    outputStream.write(postData)
                    outputStream.close()
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    return
                }

                // receive response
                try {
                    val inputStream: InputStreamReader
                    if (connection.headerFields["X-Content-Encoding"]?.get(0).equals("deflate")) {
                        // get compression steam
                        inputStream = InputStreamReader(
                            InflaterInputStream(
                                connection.inputStream,
                                Inflater(true)
                            )
                        )
                    } else {
                        inputStream = InputStreamReader(connection.inputStream)
                    }

                    val reader: BufferedReader = BufferedReader(inputStream)
                    val output: String = reader.readText()

                    // send response to listener
                    communicationEventListener?.handleServerResponse(output)

                } catch (exception: Exception) {
                    exception.printStackTrace()
                    return
                }

            }
        }.start()
}


