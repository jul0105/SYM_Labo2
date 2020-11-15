package ch.heigvd.iict.sym.labo2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject


class GraphQLActivity : AppCompatActivity(), CommunicationEventListener {

    private lateinit var list_view_authors: ListView;
    private lateinit var list_view_publication: ListView;

    val listItems = ArrayList<Author>()
    val listPublications = ArrayList<Publication>()
    lateinit var adapterAuthor: ArrayAdapter<Author>
    lateinit var adapterPublication: ArrayAdapter<Publication>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_q_l)

        list_view_authors = findViewById(R.id.listViewAuthors);
        list_view_publication = findViewById(R.id.listViewPublication);

        adapterAuthor = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        adapterPublication = ArrayAdapter(this, android.R.layout.simple_list_item_1, listPublications)

        list_view_authors.adapter = adapterAuthor
        list_view_publication.adapter = adapterPublication
        val sm = SymComManager(this)
        list_view_authors.setOnItemClickListener { parent, view, position, id ->

            sm.sendRequest(
                "http://sym.iict.ch/api/graphql",
                "{\"query\":\"{allPostByAuthor(authorId: " + listItems[position].id +" ){id title description content date}}\"}"
            );

        }


        sm.sendRequest(
            "http://sym.iict.ch/api/graphql",
            "{\"query\":\"{allAuthors {id first_name last_name}}\"}"
        );

    }

    override fun handleServerResponse(response: String) {
        val reader = JSONObject(response)

        val data = reader.getJSONObject("data")
        // get authors if allAuthors executed

        if(response.contains("allAuthors")) {
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
            adapterAuthor.notifyDataSetChanged()

        } else {
            val posts = data.getJSONArray("allPostByAuthor")
            for (i in 0 until posts.length()) {
                val author = posts.getJSONObject(i)
                val aut = Publication(
                    author.getInt("id"),
                    author.getString("title"),
                    author.getString("description")
                )
                listPublications.add(aut)
            }
            adapterPublication.notifyDataSetChanged()
        }
    }
}

data class AuthorList(val author_list: ArrayList<Author>)
data class Author(val id : Int, val first_name : String, val last_name : String) {
    override fun toString(): String {
        return this.first_name + " " + this.last_name;
    }
}
data class Publication(val id: Int, val title: String, val description: String) {
    override fun toString(): String {
        return this.title + " : " + this.description;
    }
}

// data class