package ch.heigvd.iict.sym.labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class AsynchroneActivity : AppCompatActivity(), CommunicationEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asynchrone)

        val sm = SymComManager(this)
        sm.sendRequest("http://sym.iict.ch/rest/txt/", "les gros boloss")
    }

    override fun handleServerResponse(response: String) {
        println(response)
    }
}