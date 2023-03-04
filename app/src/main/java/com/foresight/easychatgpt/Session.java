package com.foresight.easychatgpt;

import java.util.ArrayList;
import java.util.List;

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
        if(session.length()==0){
            String system_prompt=this.character_desc;
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

    public void saveSession(String query, String answer) throws JSONException {
        JSONObject gpt_item = new JSONObject();
        gpt_item.put("role", "assistant");
        gpt_item.put("content", answer);
        session.put(gpt_item);
        // discard exceed limit conversation
        discardExceedConversation(session, maxTokens);
    }

    private void discardExceedConversation(JSONArray session, int maxTokens) throws JSONException {
        int count = 0;
        List<Integer> countList = new ArrayList<>();
        for (int i = session.length() - 1; i >= 0; i--) {
            // count tokens of conversation list
            JSONObject historyConv = (JSONObject)session.get(i);
            count += historyConv.getString("role").length() + historyConv.getString("content").length()+15;
            countList.add(count);
        }
        for (int c : countList) {
            if (c > maxTokens) {
                // pop first conversation
                session.remove(0);
            }
        }
    }

    public void clearSession() {
        session=new JSONArray();
    }

}
