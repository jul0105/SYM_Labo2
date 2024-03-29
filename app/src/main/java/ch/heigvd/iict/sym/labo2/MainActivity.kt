/**
 * SYM : Labo 2 - Protocoles applicatifs
 * Auteurs : Julien Béguin, Robin Cuénoud & Gaëtan Daubresse
 * Date : 15.11.2020
 * Classe : B
 */

package ch.heigvd.iict.sym.labo2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // link UI
        setContentView(R.layout.activity_main)

        compressed_button.setOnClickListener {
            val intent = Intent(this@MainActivity, CompressedActivity::class.java)
            startActivity(intent)
        }

        async_button.setOnClickListener {
            val intent = Intent(this@MainActivity, AsynchroneActivity::class.java)
            startActivity(intent)
        }

        differed_button.setOnClickListener {
            val intent = Intent(this@MainActivity, DiffereeActivity::class.java)
            startActivity(intent)
        }

        graphql_button.setOnClickListener {
            val intent = Intent(this@MainActivity, GraphQLActivity::class.java)
            startActivity(intent)
        }

        ser_button.setOnClickListener {
            val intent = Intent(this@MainActivity, SerialisationActivity::class.java)
            startActivity(intent)
        }
    }

}