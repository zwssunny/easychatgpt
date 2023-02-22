package com.foresight.easychatgpt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    private final String TAG = "ChatGPT";
    private Session mySession;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)       //设置连接超时
            .readTimeout(60, TimeUnit.SECONDS)          //设置读超时
            .writeTimeout(60, TimeUnit.SECONDS)          //设置写超时
            .build();                                   //构建OkHttpClient对象
    private final View.OnLongClickListener lc = v -> {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", ((TextView) v).getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "复制成功", Toast.LENGTH_SHORT).show();
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        //setup Session
        String character_desc = getString(R.string.character_desc);
        int conversation_max_tokens = Integer.parseInt(getString(R.string.conversation_max_tokens));
        mySession = new Session(conversation_max_tokens, character_desc);

        //setup recycler view
        messageAdapter = new MessageAdapter(messageList, lc);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    void callAPI(String question) {
        if (question == "#清除记忆") {
            mySession.clearSession();
            addToChat("记忆已清除", Message.SENT_BY_BOT);
            return;
        }
        //okhttp
        messageList.add(new Message(getString(R.string.Typing), Message.SENT_BY_BOT));
        String newQuestion = mySession.buildSessionQuery(question);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", newQuestion);
            jsonBody.put("max_tokens", 1200);
            jsonBody.put("temperature", 0.9);
            jsonBody.put("top_p", 1);
            jsonBody.put("frequency_penalty", 0.0);
            jsonBody.put("presence_penalty", 0.0);
            jsonBody.put("stop", "\n\n\n");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        String apiKey = getString(R.string.apiKey);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse(getString(R.string.failed_load_response) + e.getMessage());
                mySession.clearSession();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
//                        Log.i(TAG, jsonArray.toString());
                        String result = jsonArray.getJSONObject(0).getString("text");
                        mySession.saveSession(question, result);
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    addResponse(getString(R.string.failed_load_response) + response.body().toString());
                    mySession.clearSession();
                }
            }
        });


    }


}




















