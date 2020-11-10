package ch.heigvd.iict.sym.labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class SerialisationActivity : AppCompatActivity() , CommunicationEventListener {
    private lateinit var send_button: Button;
    private lateinit var radio_group: RadioGroup;
    private lateinit var received_text: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_serialisation);

        send_button = findViewById(R.id.send_button);
        received_text = findViewById(R.id.received_text);
        radio_group = findViewById(R.id.radioGroup);



        val sm = SymComManager(this)


        send_button.setOnClickListener {
            var serialisedData: String;
            if(radio_group.checkedRadioButtonId == R.id.radio_xml) {
               serialisedData = "<xml>";
            } else  {
                serialisedData = "{'test','test'}";
            }


            sm.sendRequest("http://sym.iict.ch/rest/txt/", serialisedData);

        }
    }

    override fun handleServerResponse(response: String) {
        received_text.text = response;
    }

}