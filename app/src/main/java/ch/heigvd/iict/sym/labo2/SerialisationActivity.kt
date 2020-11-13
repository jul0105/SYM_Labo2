package ch.heigvd.iict.sym.labo2

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson


import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.io.File
import javax.xml.bind.annotation.XmlAccessorOrder


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

                val person1 = Person(name = "Cuenoud", firstname = "Robin", gender = "male", phone = "0795643255", phoneType = "mobile")
                val person2 = Person(name = "Beguin", firstname = "Julien", gender = "male", phone = "0786759988", phoneType = "mobile")
                val person3 = Person("Daubresse", "Gaetan", "Joel", "male", "0796875432", "mobile");
                val people = listOf<Person>(person1, person2, person3)

                serialisedData = SerializeRequest.parseXML(people)
                println(serialisedData)
                sm.sendRequest("http://sym.iict.ch/rest/xml/", serialisedData,"application/xml");
            }
            else  {

                val person = Person("Daubresse", "Gaetan", "Joel", "male", "0793345432", "mobile")
                val jsonString = Gson().toJson(person)
                sm.sendRequest("http://sym.iict.ch/rest/json/", jsonString,"application/json");
            }
        }
    }

    override fun handleServerResponse(response: String) {

        if(radio_group.checkedRadioButtonId == R.id.radio_xml) {
            //TODO Serialiser la response
            received_text.text = response
        }
        else { // JSON
            val person = SerializeRequest.parseJSON(response);
            received_text.text = "Hello " +person.firstname + " ," + person.name;
        }
    }
}