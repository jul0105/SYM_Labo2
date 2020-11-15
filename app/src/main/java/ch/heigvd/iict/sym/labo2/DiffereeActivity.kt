/**
 * SYM : Labo 2 - Protocoles applicatifs
 * Auteurs : Julien Béguin, Robin Cuénoud & Gaëtan Daubresse
 * Date : 15.11.2020
 * Classe : B
 */

package ch.heigvd.iict.sym.labo2

import android.app.job.JobInfo
import android.app.job.JobService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.stream.DoubleStream.builder

class DiffereeActivity : AppCompatActivity(), CommunicationEventListener {
    private lateinit var send_button: Button
    private lateinit var text_input: EditText
    private lateinit var received_text: TextView
    private var receivedResponse = false
    private var messageToSend = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_asynchrone)

        send_button = findViewById(R.id.send_button);
        text_input = findViewById(R.id.send_text);
        received_text = findViewById(R.id.received_text);

        val sm = SymComManager(this)

        // if button pressed new thread send message to the server every 5 sec until response arrive
        send_button.setOnClickListener {

            messageToSend = text_input.text.toString()

            object: Thread(){
                override fun run(){
                    while(!receivedResponse){
                        try{
                            sm.sendRequest("http://sym.iict.ch/rest/txt/", messageToSend);
                        }catch(e: Exception){
                            e.printStackTrace()
                        }
                        Thread.sleep(5000);
                    }
                }
            }.start()
        }
    }

    override fun handleServerResponse(response: String) {
        receivedResponse = true
        received_text.text = response;
    }


}
