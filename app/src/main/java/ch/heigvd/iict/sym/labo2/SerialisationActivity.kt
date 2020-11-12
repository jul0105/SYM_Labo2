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
                val person = Person("Daubresse", "Gaetan", "Joel", "male", "0796875432", "mobile");
                val people = listOf<Person>(person)
                serialisedData = SerializeRequest.parseXML(people)
                println(serialisedData)
                sm.sendRequest("http://sym.iict.ch/rest/xml/", serialisedData,"application/xml");
            } else  {
                val person = Person("Daubresse", "Gaetan", "Joel", "male", "0793345432", "mobile")
                val jsonString = Gson().toJson(person)
                sm.sendRequest("http://sym.iict.ch/rest/json/", jsonString,"application/json");
            }




        }
    }

    override fun handleServerResponse(response: String) {
        if(radio_group.checkedRadioButtonId == R.id.radio_xml) {
            val person = writeXmlToObject(response, Person::class.java)
            received_text.text = ""
        } else { // JSON
            val person = SerializeRequest.parseJSON(response);
            received_text.text = "Hello " +person.firstname + " ," + person.name;
        }
    }



    fun writeXmlToString(obj: Any): String {
        val xmlMapper = XmlMapper()
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
        return xmlMapper.writeValueAsString(obj)
    }

    fun writeXmlToObject(pathFile: String, cls: Class<Person>) : Person{
        val xmlMapper = XmlMapper()
        return xmlMapper.readValue(File(pathFile), cls)
    }
}