package ch.heigvd.iict.sym.labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File


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
                val person = person("Daubresse", "Gaetan", "Joel", "male", Phone("mobile", "0793342321"))
                serialisedData = writeXmlToString(person)
                println(serialisedData)
                sm.sendRequest("http://sym.iict.ch/rest/xml/", serialisedData,"application/xml");
            } else  {
                
                serialisedData = "{'test','test'}";
                sm.sendRequest("http://sym.iict.ch/rest/json/", serialisedData,"application/json");
            }




        }
    }

    override fun handleServerResponse(response: String) {
        if(radio_group.checkedRadioButtonId == R.id.radio_xml) {
            val person = writeXmlToObject(response, person::class.java)
            received_text.text = parseXML(person.firstname + person.name);

        } else { // JSONPerson
            received_text.text = parseJSON(response);
        }
    }

    fun parseXML(data: String): String {
        return "<xml>";
    }

    fun parseJSON(data: String) : String {
        return "Json";
    }

    data class Phone(
        val phoneType: String = "",
        val phoneNumber: String = ""
    )

    data class person(
        val name: String = "",
        val firstname: String = "",
        val middlename: String = "",
        val gender: String = "",
        val phone: Phone? = null

    )

    fun writeXmlToString(obj: Any): String {
        val xmlMapper = XmlMapper()
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
        return xmlMapper.writeValueAsString(obj)
    }

    fun writeXmlToObject(pathFile: String, cls: Class<person>) : person{
        val xmlMapper = XmlMapper()
        return xmlMapper.readValue(File(pathFile), cls)
    }
}