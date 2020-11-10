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
                sm.sendRequest("http://sym.iict.ch/rest/xml/", serialisedData);
            } else  {
                serialisedData = "{'test','test'}";
                sm.sendRequest("http://sym.iict.ch/rest/json/", serialisedData);
            }




        }
    }

    override fun handleServerResponse(response: String) {
        if(radio_group.checkedRadioButtonId == R.id.radio_xml) {
            received_text.text = parseXML(response);
        } else { // JSON
            received_text.text = parseJSON(response);
        }
    }

    fun parseXML(data: String): String {
        return "<xml>";
    }

    fun parseJSON(data: String) : String {
        return "Json";
    }
}