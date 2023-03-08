package com.foresight.tokenizers;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TokensCount {
    private GPT2Tokenizer tokenizer;

    public TokensCount(GPT2Tokenizer tokenizer) {
//        tokenizer = GPT2Tokenizer.fromPretrained(context, "tokenizers/gpt2");
        this.tokenizer=tokenizer;
    }

    public int num_tokens_from_message(JSONObject message) throws JSONException {
        int num_tokens = 4; //every message follows <im_start>{role/name}\\n{content}<im_end>\\n
        num_tokens += tokenizer.encode(message.getString("role")).size();
        num_tokens += tokenizer.encode(message.getString("content")).size();
        if (message.has("name")) //if there's a name, the role is omitted
        {
            num_tokens += tokenizer.encode(message.getString("name")).size();
            num_tokens += -1; //role is always required and always 1 token
        }
        return num_tokens;
    }

    public int num_tokens_from_messages(JSONArray messages) throws JSONException {
        int num_tokens = 2; //every reply is primed with <im_start>assistant
        for (int i = 0; i < messages.length(); i++) {
            num_tokens += num_tokens_from_message(messages.getJSONObject(i));
        }
        return num_tokens;
    }
}
