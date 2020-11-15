/**
 * SYM : Labo 2 - Protocoles applicatifs
 * Auteurs : Julien Béguin, Robin Cuénoud & Gaëtan Daubresse
 * Date : 15.11.2020
 * Classe : B
 */

package ch.heigvd.iict.sym.labo2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CompressedActivity : AppCompatActivity(), CommunicationEventListener {

    private lateinit var send_button: Button
    private lateinit var text_input: EditText
    private lateinit var received_text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // link UI
        setContentView(R.layout.activity_compressed)

        send_button = findViewById(R.id.send_button)
        text_input = findViewById(R.id.send_text)
        received_text = findViewById(R.id.received_text)

        // Send content
        val sm = SymComManager(this)

        send_button.setOnClickListener {
            sm.sendRequest(
                "http://sym.iict.ch/rest/txt/",
                text_input.text.toString(),
                compressed = true
            )
        }
    }

    override fun handleServerResponse(response: String) {
        received_text.text = response
    }


}