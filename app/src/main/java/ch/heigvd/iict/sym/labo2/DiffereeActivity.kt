package ch.heigvd.iict.sym.labo2

import android.app.job.JobInfo
import android.app.job.JobService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.stream.DoubleStream.builder

class DiffereeActivity : AppCompatActivity() {
    private lateinit var send_button: Button;
    private lateinit var text_input: EditText;
    private lateinit var received_text: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_asynchrone)

        send_button = findViewById(R.id.send_button);
        text_input = findViewById(R.id.send_text);
        received_text = findViewById(R.id.received_text);

        //JobInfo.Builder b = new JobInfo.Builder(1,new ComponentName(this,MyJobService.class))
    }
}