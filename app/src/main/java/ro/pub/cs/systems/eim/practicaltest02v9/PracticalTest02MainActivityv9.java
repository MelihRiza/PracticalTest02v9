package ro.pub.cs.systems.eim.practicaltest02v9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PracticalTest02MainActivityv9 extends AppCompatActivity {

    private static final String TAG = "PracticalTest02";
    private static final String BROADCAST_ACTION = "ro.pub.cs.systems.eim.practicaltest02v9.UPDATE_TEXTVIEW";

    EditText edit_text_word, edit_text_len;
    Button button_send, button_map;
    TextView text_view_result;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            Log.d(TAG, "Received result: " + result);
            text_view_result.setText(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v9_main);

        edit_text_word = findViewById(R.id.edit_text_word);
        edit_text_len = findViewById(R.id.edit_text_len);
        button_send = findViewById(R.id.button_send);
        text_view_result = findViewById(R.id.text_view_result);
        button_map = findViewById(R.id.button_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ACTION), Context.RECEIVER_NOT_EXPORTED);
        }

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = edit_text_word.getText().toString();
                String len = edit_text_len.getText().toString();

                if (!word.isEmpty() && !len.isEmpty()) {
                    int minLength = Integer.parseInt(len);
                    new FetchAnagramsTask().execute(word, String.valueOf(minLength));
                }
            }
        });

        button_map.setOnClickListener(view -> {
            Intent intent = new Intent(PracticalTest02MainActivityv9.this, PracticalTest02SecondaryActivityv9.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private class FetchAnagramsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String word = params[0];
            String minLength = params[1];
            String urlString = "http://www.anagramica.com/all/" + word;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                Log.e(TAG, "Error fetching anagrams", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                Log.d(TAG, "Raw JSON Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray allAnagrams = jsonObject.getJSONArray("all");

                    int minLength = Integer.parseInt(edit_text_len.getText().toString());
                    List<String> filteredAnagrams = new ArrayList<>();

                    for (int i = 0; i < allAnagrams.length(); i++) {
                        String anagram = allAnagrams.getString(i);
                        if (anagram.length() >= minLength) {
                            filteredAnagrams.add(anagram);
                        }
                    }

                    Log.d(TAG, "Filtered Anagrams: " + filteredAnagrams);

                    // Send broadcast with the result
                    Intent broadcastIntent = new Intent(BROADCAST_ACTION).setAction(getPackageName());
                    broadcastIntent.putExtra("result", filteredAnagrams.toString());
                    sendBroadcast(broadcastIntent);

                } catch (Exception e) {
                    Log.e(TAG, "Error parsing JSON", e);
                }
            }
        }
    }
}