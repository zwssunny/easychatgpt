package com.foresight.easychatgpt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 *保存会话过程，进行上下文维护
 * */
public class Session {

    private JSONArray session = new JSONArray();
    private int maxTokens;
    private String character_desc;

    public Session(int maxTokens, String character_desc) {
        this.maxTokens = maxTokens;
        this.character_desc = character_desc;
    }

    /*
    *
    *   build query with conversation history
        e.g.  Q: xxx
        role: system, content: xxx
        role: user, content: xxx
        :param query: query content
        :return: query content with conversaction
    * */
    public JSONArray buildSessionQuery(String query) throws JSONException {
        if (session.length() == 0) {
            String system_prompt = this.character_desc;
            JSONObject system_item = new JSONObject();
            system_item.put("role", "system");
            system_item.put("content", system_prompt);
            session.put(system_item);
        }
        JSONObject user_item = new JSONObject();
        user_item.put("role", "user");
        user_item.put("content", query);
        session.put(user_item);
        return session;
    }

    public void saveSession(int total_tokens, String answer) throws JSONException {
        JSONObject gpt_item = new JSONObject();
        gpt_item.put("role", "assistant");
        gpt_item.put("content", answer);
        session.put(gpt_item);
        // discard exceed limit conversation
        discardExceedConversation(session, maxTokens, total_tokens);
    }

    private void discardExceedConversation(JSONArray session, int maxTokens, int total_tokens) {
        int dec_tokens = total_tokens;
        while (dec_tokens > maxTokens) {
            // pop first conversation
            if (session.length() > 0)
                session.remove(0);
            else
                break;
            dec_tokens = dec_tokens - maxTokens;
        }
    }

    public void clearSession() {
        session = new JSONArray();
    }

}
