package ch.heigvd.iict.sym.labo2

import com.google.gson.Gson
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class SerializeRequest {

    companion object{

        fun parseXML(people: List<Person>): String{

            val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc: Document = docBuilder.newDocument()
            val rootElement: Element = doc.createElement("directory")

            for(itPerson in people){
                val person: Element = doc.createElement("person")
                val name: Element = doc.createElement("name")
                name.appendChild(doc.createTextNode(itPerson.name))
                person.appendChild(name)
                val firstname: Element = doc.createElement("firstname")
                firstname.appendChild(doc.createTextNode(itPerson.firstname))
                person.appendChild(firstname)
                val gender: Element = doc.createElement("gender")
                gender.appendChild(doc.createTextNode(itPerson.gender))
                person.appendChild(gender)
                val phone: Element = doc.createElement("phone")
                phone.setAttribute("type", itPerson.phoneType)
                phone.appendChild(doc.createTextNode(itPerson.phone))
                person.appendChild(phone)
                rootElement.appendChild(person)
            }
            doc.appendChild(rootElement)

            val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty(OutputKeys.METHOD, "xml")
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://sym.iict.ch/directory.dtd")

            val source = DOMSource(doc)
            val sw = StringWriter()
            val result = StreamResult(sw)
            transformer.transform(source, result)

            return sw.toString()
        }

        fun parseJSON(data: String) : Person {
            val person = Gson().fromJson(data,Person::class.java)
            return person
        }
    }
}