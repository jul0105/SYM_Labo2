package ch.heigvd.iict.sym.labo2

import android.util.Xml
import androidx.annotation.Nullable
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.google.gson.Gson
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class ParseRequest {

    companion object{

        // take a list of Person and return it as a serialized XML String
        fun serializeXML(people: List<Person>): String{

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
            transformer.setOutputProperty(OutputKeys.INDENT, "no")
            transformer.setOutputProperty(OutputKeys.METHOD, "xml")
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://sym.iict.ch/directory.dtd")

            val source = DOMSource(doc)
            val sw = StringWriter()
            val result = StreamResult(sw)
            transformer.transform(source, result)

            return sw.toString().replace("\n", "")
        }

        @Throws(XmlPullParserException::class, IOException::class)
        fun deserializeXML(people: String): List<Person>{
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(people.byteInputStream(), null)
            parser.nextTag()

            val entries = mutableListOf<Person>()
            while(parser.next() != XmlPullParser.END_TAG){
                if(parser.eventType != XmlPullParser.START_TAG){
                    continue
                }
                if(parser.name == "person"){
                    entries.add(readPerson(parser))
                }else{
                    skip(parser)
                }
            }
            return entries
        }

        private fun readPerson(parser: XmlPullParser): Person{
            var name: String = ""
            var firstname: String = ""
            var gender: String = ""
            var phone: String = ""
            var phoneType: String = ""
            while(parser.next() != XmlPullParser.END_TAG){
                if(parser.eventType != XmlPullParser.START_TAG){
                    continue
                }
                when(parser.name){
                    "name" -> name = readName(parser)
                    "firstname" -> firstname = readFirstname(parser)
                    "gender" -> gender = readGender(parser)
                    "phone" -> phone = readPhone(parser)
                    "phoneType" -> phoneType = readPhoneType(parser)
                    else -> skip(parser)
                }
            }
            return Person(name=name, firstname = firstname, gender = gender, phone = phone, phoneType = phoneType)
        }

        @Throws(IOException::class, XmlPullParserException::class)
        private fun readName(parser: XmlPullParser): String {
            parser.require(XmlPullParser.START_TAG, null, "name")
            val title = readText(parser)
            parser.require(XmlPullParser.END_TAG, null, "name")
            return title
        }

        @Throws(IOException::class, XmlPullParserException::class)
        private fun readFirstname(parser: XmlPullParser): String {
            parser.require(XmlPullParser.START_TAG, null, "firstname")
            val title = readText(parser)
            parser.require(XmlPullParser.END_TAG, null, "firstname")
            return title
        }

        @Throws(IOException::class, XmlPullParserException::class)
        private fun readGender(parser: XmlPullParser): String {
            parser.require(XmlPullParser.START_TAG, null, "gender")
            val title = readText(parser)
            parser.require(XmlPullParser.END_TAG, null, "gender")
            return title
        }

        @Throws(IOException::class, XmlPullParserException::class)
        private fun readPhone(parser: XmlPullParser): String {
            parser.require(XmlPullParser.START_TAG, null, "phone")
            val title = readText(parser)
            parser.require(XmlPullParser.END_TAG, null, "phone")
            return title
        }

        @Throws(IOException::class, XmlPullParserException::class)
        private fun readPhoneType(parser: XmlPullParser): String {
            parser.require(XmlPullParser.START_TAG, null, "phoneType")
            val title = readText(parser)
            parser.require(XmlPullParser.END_TAG, null, "phoneType")
            return title
        }

        @Throws(IOException::class, XmlPullParserException::class)
        private fun readText(parser: XmlPullParser): String {
            var result = ""
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.text
                parser.nextTag()
            }
            return result
        }

        @Throws(XmlPullParserException::class, IOException::class)
        private fun skip(parser: XmlPullParser) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                throw IllegalStateException()
            }
            var depth = 1
            while (depth != 0) {
                when (parser.next()) {
                    XmlPullParser.END_TAG -> depth--
                    XmlPullParser.START_TAG -> depth++
                }
            }
        }


        // Take a Person object and return a serialized JSON String
        fun serializeJSON(person: Person): String{
            return Gson().toJson(person)
        }

        // Take a serialized JSON string and return a Person object
        fun deserializeJSON(data: String) : Person {
            val test = data
            val person = Gson().fromJson(data,Person::class.java)
            return person
        }

        // Take a String serialized object and return a Document
        fun getDocFromSerializedString(serializedString: String): Document{
            val byteArray = serializedString.toByteArray()
            val byteArrayInputStream = ByteArrayInputStream(byteArray)
            val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc = docBuilder.parse(byteArrayInputStream)
            return doc
        }

        fun NodeList.forEach(action: (Node) -> Unit) {
            (0 until this.length)
                .asSequence()
                .map { this.item(it) }
                .forEach { action(it) }
        }
    }
}