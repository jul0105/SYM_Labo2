package ch.heigvd.iict.sym.labo2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject


class GraphQLActivity : AppCompatActivity(), CommunicationEventListener {

    private lateinit var list_view : ListView;

    val listItems = ArrayList<Author>()
    lateinit var adapter: ArrayAdapter<Author>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_q_l)

        list_view = findViewById(R.id.listView);

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)


        list_view.adapter = adapter


        val sm = SymComManager(this)

        sm.sendRequest("http://sym.iict.ch/api/graphql",
            "{\"query\":\"{allAuthors {id first_name last_name}}\"}"
        );

    }

    override fun handleServerResponse(response: String) {

        val reader = JSONObject(response)

        val data = reader.getJSONObject("data")
        val authors = data.getJSONArray("allAuthors")
        for (i in 0 until authors.length()) {
            val author = authors.getJSONObject(i)
            val aut = Author(
                author.getInt("id"),
                author.getString("first_name"),
                author.getString("last_name")
            )
            listItems.add(aut)
        }



        adapter.notifyDataSetChanged()
    }
}

data class AuthorList(val author_list: ArrayList<Author>)
data class Author(val id : Int, val first_name : String, val last_name : String) {
    override fun toString(): String {
        return this.first_name + " " + this.last_name;
    }
}

// data class