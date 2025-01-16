package ro.pub.cs.systems.eim.practicaltest02v9;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest02MainActivityv9 extends AppCompatActivity {

    EditText edit_text_word, edit_text_len;
    Button button_send;
    TextView text_view_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v9_main);

        edit_text_word = findViewById(R.id.edit_text_word);
        edit_text_len = findViewById(R.id.edit_text_len);
        button_send = findViewById(R.id.button_send);
        text_view_result = findViewById(R.id.text_view_result);

        

    }
}