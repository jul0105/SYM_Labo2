package ch.heigvd.iict.sym.labo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.heigvd.iict.sym.lab.comm.SymComManager

class AsynchroneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asynchrone)

        val sm = SymComManager()
        sm.sendRequest("https://enlidghzzpq3.x.pipedream.net/", "")
    }
}