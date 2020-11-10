package ch.heigvd.iict.sym.labo2

import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.InputStreamReader


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
                val Person = Person("Daubresse", "Gaetan", "Joel", "male", Phone("mobile", "0793342321"))
                serialisedData = "<xml>";
                sm.sendRequest("http://sym.iict.ch/rest/xml/", serialisedData,"application/xml");
            } else  {
                val person = Person("Daubresse", "Gaetan", "Joel", "male", Phone("mobile", "0793342321"))
                val jsonString = Gson().toJson(person)
                sm.sendRequest("http://sym.iict.ch/rest/json/", jsonString,"application/json");
            }




        }
    }

    override fun handleServerResponse(response: String) {
        if(radio_group.checkedRadioButtonId == R.id.radio_xml) {
            received_text.text = parseXML(response);

        } else { // JSON
            val person = parseJSON(response);
            received_text.text = "Hello " +person.firstname + " ," + person.name;
        }
    }

    fun parseXML(data: String): String {
        return "<xml>";
    }

    fun parseJSON(data: String) : Person {
        val person = Gson().fromJson(data,Person::class.java)
        return person
    }


    data class Phone(
        val phoneType: String = "",
        val phoneNumber: String = ""
    )

    data class Person(
        val name: String = "",
        val firstname: String = "",
        val middlename: String = "",
        val gender: String = "",
        val phone: Phone? = null

    )


}