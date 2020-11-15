/**
 * SYM : Labo 2 - Protocoles applicatifs
 * Auteurs : Julien Béguin, Robin Cuénoud & Gaëtan Daubresse
 * Date : 15.11.2020
 * Classe : B
 */

package ch.heigvd.iict.sym.labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


class AsynchroneActivity : AppCompatActivity(), CommunicationEventListener {

    private lateinit var send_button: Button;
    private lateinit var text_input: EditText;
    private lateinit var received_text: TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_asynchrone)

        send_button = findViewById(R.id.send_button);
        text_input = findViewById(R.id.send_text);
        received_text = findViewById(R.id.received_text);



        val sm = SymComManager(this)

        send_button.setOnClickListener {
            sm.sendRequest("http://sym.iict.ch/rest/txt/", text_input.text.toString());

        }
    }

    override fun handleServerResponse(response: String) {
        received_text.text = response;
    }


}


